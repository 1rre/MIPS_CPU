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
  val d0s = RegInit(Bool(), 0.B)
  val d1s = RegInit(Bool(), 0.B)
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
  
  val mulIn0 = WireInit(UInt(16.W), 0.U)
  val mulIn1 = WireInit(UInt(16.W), 0.U)
  val multiplyOut = WireInit(UInt(32.W), 0.U)

  multiplyOut:= mulIn0 * mulIn1

  d0s := ~d1s
  d1s := d0s

  printf(p"$d0s $d1s\n${Binary(result64)}\n")

  when (beginOp) {
    d0s := 0.B
    d1s := 0.B
    result64 := 0.U
    mulIn0 := dataIn0Wire(15,0)
    mulIn1 := dataIn1Wire(15,0)
    loDone := 0.B
    hiDone := 0.B
  }

  when (d0s & d1s) {
    hiDone := 1.B
  } otherwise when (d1s) {
    loDone := 1.B
  }

  when (d0s) {
    mulIn0 := dataIn0Wire(31,16)
  } otherwise {
    mulIn0 := dataIn0Wire(15,0)
  }
  
  when (d1s) {
    mulIn1 := dataIn1Wire(31,16)
  } otherwise {
    mulIn1 := dataIn1Wire(15,0)
  }

  when (d0s & d1s) {
    result64 := result64 + (multiplyOut << 32)
  } otherwise (when (d1s) {
    result64 := result64 + (multiplyOut << 16)
  } otherwise (when (d0s) {
    result64 := result64 + (multiplyOut << 16)
  } otherwise ({
    result64 := multiplyOut
  })))
}
