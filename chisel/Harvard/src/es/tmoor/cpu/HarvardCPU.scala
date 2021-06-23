package es.tmoor.cpu

import chisel3._

class HarvardCPU extends Module {
  class HarvardIO extends Bundle {
    val clk_enable = Input(UInt(1.W))
    val active = Output(UInt(1.W))
    val data_write = Output(UInt(1.W))
    val data_read = Output(UInt(1.W))
    val instr_readdata = Input(UInt(32.W))
    val data_readdata = Input(UInt(32.W))
    val register_v0 = Output(UInt(32.W))
    val instr_address = Output(UInt(32.W))
    val data_address = Output(UInt(32.W))
    val data_writedata = Output(UInt(32.W))
  }
  val io = IO(new HarvardIO)
  import io._
  val regReadAddr0 = Reg(UInt(5.W))
  val regReadAddr1 = Reg(UInt(5.W))
  val regWriteAddr = Reg(UInt(5.W))
  val regReadData0 = Wire(UInt(32.W))
  val regReadData1 = Wire(UInt(32.W))
  val regWriteData = Reg(UInt(32.W))

  val memActive = Wire(Bool())
  val regActive = Wire(Bool())

  val stage = Reg(UInt(5.W))
  def fetch = stage(0)
  def decode = stage(1)
  def execute = stage(2)
  def memory = stage(3)
  def writeback = stage(4)

  // Registers
  val regFile = Module(new RegFile)
  regFile.io.readAddr0 := regReadAddr0
  regFile.io.readAddr1 := regReadAddr1
  regFile.io.writeAddr := regWriteAddr
  regReadData0 := regFile.io.readData0
  regReadData1 := regFile.io.readData1
  regFile.io.writeData := regWriteData
  register_v0 := regFile.io.registerV0
  regFile.io.writeEn := 1.U
  regFile.clock := clock
  regFile.reset := reset

  when (reset.asBool()) {
    stage := 16.U
  } otherwise {
    when (fetch) (stage := 8.U)
    when (decode) (stage := 4.U)
    when (execute) (when (memActive) (stage := 2.U) otherwise (when (regActive) (stage := 1.U) otherwise (stage := 16.U)))
    when (memory) (when (regActive) (stage := 1.U) otherwise (stage := 16.U))
  }

  val counter = RegInit(UInt(32.W),0.U)
  counter := counter + 1.U

  regWriteAddr := 2.U
  regWriteData := counter

  regWriteData := data_readdata //DELETE THIS
  data_writedata := stage //DELETE THIS

  regActive := instr_readdata(0) //DELETE THIS
  memActive := instr_readdata(1) //DELETE THIS
  
  
  // TEMP ASSIGNMENTS
  active := 1.U
  data_write := 0.U
  data_read := 0.U
  instr_address := 0.U
  data_address := 0.U
  
}

