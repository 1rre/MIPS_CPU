package es.tmoor.cpu

import chisel3._
import stages._
import chisel3.util.MuxCase

class HarvardCPU extends Module {
  object HarvardIO extends Bundle {
    val clk_enable = Input(Bool())
    val active = Output(Bool())
    val data_write = Output(UInt(1.W))
    val data_read = Output(UInt(1.W))
    val instr_readdata = Input(UInt(32.W))
    val data_readdata = Input(UInt(32.W))
    val register_v0 = Output(UInt(32.W))
    val instr_address = Output(UInt(32.W))
    val data_address = Output(UInt(32.W))
    val data_writedata = Output(UInt(32.W))
  }
  val io = IO(HarvardIO)
  import io._

  // Clock logic
  val clk = Wire(Clock())
  clk := (clock.asBool & clk_enable).asClock()
  override_clock = Some(clk)

  val regWriteData = RegInit(UInt(32.W), 0.U)
  val branchAddr = RegInit(UInt(32.W), 0.U)

  // Inactive when instruction address becomes 0
  active := instr_address =/= 0.U

  // Current stage logic
  def sFetch = 16.U
  def sDecode = 8.U
  def sExecute = 4.U
  def sMemory = 2.U
  def sWriteback = 1.U
  val stage = RegInit(UInt(5.W), sFetch)
  def fetch = stage(4)
  def decode = stage(3)
  def execute = stage(2)
  def memory = stage(1)
  def writeback = stage(0)

  // Registers
  val regFile = Module(new RegFile)  
  val s0 = Module(new Fetch)
  val s1 = Module(new Decode)
  val s2 = Module(new Execute)
  val s3 = Module(new MemoryWrite)
  val s4 = Module(new WriteBack)

  // Decode directly routed signals
  import s1.io.{regActive, memActive,branchEn,destReg,srcReg0,srcReg1}

  // Register directly routed signals
  import regFile.io.registerV0
  def regReadData0 = regFile.io.readData0
  def regReadData1 = regFile.io.readData1
  
  // Register IO
  regFile.io.readAddr0 := srcReg0
  regFile.io.readAddr1 := srcReg1
  regFile.io.writeAddr := destReg
  regFile.io.writeData := regWriteData
  regFile.io.writeEn := writeback & regActive

  // Fetch IO
  instr_address := s0.io.pc
  s0.io.enable := fetch
  s0.io.branchAddr := branchAddr
  s0.io.branchEn := branchEn

  // Decode IO
  s1.io.enable := decode
  s1.io.instruction := instr_readdata

  // Execute IO
  s2.io.fun := s1.fun
  s2.io.input0 := s1.io.instruction
  s2.io.input1 := s1.io.instruction
  // Assuming rt <= readData1
  data_writedata := regFile.io.readData1
  regWriteData := Mux(memActive, 0.U, s2.io.output)
  data_address := s2.io.output
  import s2.io.hold

  // Memory write IO

  // Writeback IO

  // Module IO
  register_v0 := registerV0
  data_write := memory & memActive

  // Cyclical stages (ignoring multi-cycle instructions for now)
  stage := MuxCase (sFetch, Seq (
    fetch -> sDecode,
    (decode || execute && hold) -> sExecute,
    (execute && memActive) -> sMemory,
    ((memory || execute) && regActive) -> sWriteback
  ))
  
  // TEMP ASSIGNMENTS
  data_read := 0.U
  
}

