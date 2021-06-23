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

  val mulIn0 = Wire(UInt(16.W))
  val mulIn1 = Wire(UInt(16.W))
  val dataIn0Reg = Reg(UInt(32.W))
  val dataIn1Reg = Reg(UInt(32.W))
  val dataIn0Wire = Wire(UInt(32.W))
  val dataIn1Wire = Wire(UInt(32.W))
  val s0 = WireInit(Bool(), 0.B)
  val s1 = RegInit(Bool(), 0.B)
  val s2 = RegInit(Bool(), 0.B)
  val s3 = RegInit(Bool(), 0.B)

  when(beginOp) (dataIn0Reg := dataIn0)
  when(beginOp) (dataIn1Reg := dataIn1)

  when(beginOp) (dataIn0Wire := dataIn0) otherwise (dataIn0Wire := dataIn0Reg)
  when(beginOp) (dataIn1Wire := dataIn1) otherwise (dataIn1Wire := dataIn1Reg)

  s0 := beginOp
  s1 := s0
  s2 := s1
  s3 := s2

  val r = RegInit(UInt(64.W), 0.U)

  when(s0) {
    mulIn0 := dataIn0Wire(15,0)
    mulIn1 := dataIn1Wire(15,0)
    r := mulIn0 * mulIn1
  } otherwise (when (s1) {
    mulIn0 := dataIn0Wire(31,16)
    mulIn1 := dataIn1Wire(15,0)
    r := r + ((mulIn0 * mulIn1) << 8)
  } otherwise (when (s2) {
    mulIn0 := dataIn0Wire(15,0)
    mulIn1 := dataIn1Wire(31,16)
    r := r + ((mulIn0 * mulIn1) << 8)
  } otherwise (when (s3) {
    mulIn0 := dataIn0Wire(31,16)
    mulIn1 := dataIn1Wire(31,16)
    r := r + ((mulIn0 * mulIn1) << 16)
  } otherwise {
    mulIn0 := 0.U
    mulIn1 := 0.U
    r := 0.U
  })))
  
  
  hiOut := r(63,32)
  loOut := r(31,0)

  val loDone = RegInit(Bool(), 0.B)
  val hiDone = RegInit(Bool(), 0.B)
  loDone := s2
  hiDone := s3
  hiReady := hiDone
  loReady := loDone
}