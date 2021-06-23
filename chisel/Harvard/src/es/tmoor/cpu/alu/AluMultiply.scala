package es.tmoor.cpu.alu

import chisel3._

// TODO: Split this across multiple operations
class AluMultiply extends Module {
  object AluMultiplyIO extends Bundle {
    val beginOp = Input(Bool())
    val dataIn0 = Input(UInt(32.W))
    val dataIn1 = Input(UInt(32.W))
    val hiReady = Output(Bool())
    val loReady = Output(Bool())
    val hiOut = Output(UInt(32.W))
    val loOut = Output(UInt(32.W))
  }
  val io = IO(AluMultiplyIO)
  import io._

  val dataIn0Reg = Reg(UInt(32.W))
  val dataIn1Reg = Reg(UInt(32.W))
  val dataIn0Wire = Wire(UInt(32.W))
  val dataIn1Wire = Wire(UInt(32.W))
  val s = RegInit(UInt(6.W), 63.U)
  val result64 = RegInit(UInt(64.W), 0.U)
  hiOut := result64(63,32)
  loOut := result64(31,0)

  when (beginOp) (dataIn0Reg := dataIn0)
  when (beginOp) (dataIn1Reg := dataIn1)

  when (beginOp) (dataIn0Wire := dataIn0) otherwise (dataIn0Wire := dataIn0Reg)
  when (beginOp) (dataIn1Wire := dataIn1) otherwise (dataIn1Wire := dataIn1Reg)

  val loDone = RegInit(Bool(), 0.B)
  val hiDone = RegInit(Bool(), 0.B)

  loReady := loDone
  hiReady := hiDone

  when (s < 63.U) {
    printf(p"hi: $hiOut\nlo: $loOut\ns: $s\n")
    s := s - 1.U
    when (dataIn1Reg(s)) {
      result64 := (result64 << 1) + dataIn0Wire
    } otherwise {
      result64 := (result64 << 1)
    }
  }
  when (s === 0.U) {
    loDone := 1.B
    hiDone := 1.B
  }

  when (beginOp) {
    s := 31.U
    loDone := 0.B
    hiDone := 0.B
    result64 := 0.U
  }

}