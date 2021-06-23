package es.tmoor.cpu

import chisel3._
import stages._

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
  val clk = Wire(Clock())
  val _clk = Wire(Bool())
  _clk := (clock.asBool & clk_enable)
  clk := _clk.asClock()
  override_clock = Some(clk)

  val regReadAddr0 = WireInit(UInt(5.W), 0.U)
  val regReadAddr1 = WireInit(UInt(5.W), 0.U)
  val regWriteAddr = WireInit(UInt(5.W), 0.U)
  val regReadData0 = WireInit(UInt(32.W), 0.U)
  val regReadData1 = WireInit(UInt(32.W), 0.U)
  val regWriteData = RegInit(UInt(32.W), 0.U)
  val branchAddr = RegInit(UInt(32.W), 0xBCE00000l.U)

  val memActive = WireInit(Bool(), 0.U)
  val regActive = WireInit(Bool(), 0.U)

  val stage = RegInit(UInt(5.W), 16.U)
  def fetch = stage(0)
  def decode = stage(1)
  def execute = stage(2)
  def memory = stage(3)
  def writeback = stage(4)

  // Registers
  val regFile = Module(new RegFile)
  regFile.clock := clk
  regFile.reset := reset
  regFile.io.readAddr0 := regReadAddr0
  regFile.io.readAddr1 := regReadAddr1
  regFile.io.writeAddr := regWriteAddr
  regReadData0 := regFile.io.readData0
  regReadData1 := regFile.io.readData1
  regFile.io.writeData := regWriteData
  register_v0 := regFile.io.registerV0
  regFile.io.writeEn := writeback

  val branchEn = WireInit(Bool(), 0.U)

  // Fetch stage
  val s0 = Module(new Fetch)
  s0.clock := clk
  instr_address := s0.io.pc
  s0.io.branchEn := branchEn
  s0.io.enable := fetch
  s0.io.branchAddr := branchAddr

  // Decode stage
  val s1 = Module(new Decode)
  s1.clock := clk
  s1.reset := reset
  s1.io.enable := decode
  regWriteAddr := s1.io.destReg
  regReadAddr0 := s1.io.srcReg0
  regReadAddr1 := s1.io.srcReg1
  memActive := s1.io.memActive
  regActive := s1.io.regActive
  branchEn := s1.io.branchEn
  s1.io.instruction := instr_readdata

  // Stages aren't skipped so pipelining can work better
  when (reset.asBool()) {
    stage := 16.U
  } otherwise {
    when (writeback) {
      stage := stage << 4
    } otherwise {
      stage := stage >> 1
    }
  }

  regWriteData := data_readdata | regReadAddr0 | regReadAddr1 //DELETE THIS
  data_writedata := stage //DELETE THIS  
  
  // TEMP ASSIGNMENTS
  active := 1.U
  data_write := 0.U
  data_read := 0.U
  data_address := 0.U
  
}

