package es.tmoor.cpu.alu

import chisel3._

// 0 => Divide
// 1 => Multiply
class AluMulDiv extends Module {
  object AluMulDivIO extends Bundle {
    val opIn = Input(Bool())
    val beginOp = Input(Bool())
    val dataIn0 = Input(UInt(32.W))
    val dataIn1 = Input(UInt(32.W))
    val hiReady = Output(Bool())
    val loReady = Output(Bool())
    val hiOut = Output(UInt(32.W))
    val loOut = Output(UInt(32.W))
  }
  val io = IO(AluMulDivIO)
  import io._

  val hi = RegInit(UInt(32.W), 0.U)
  val lo = RegInit(UInt(32.W), 0.U)

  hiOut := hi
  loOut := lo

  val mul = Module(new AluMultiply)
  mul.clock := clock
  mul.reset := reset
  mul.io.beginOp := opIn & beginOp
  mul.io.dataIn0 := dataIn0
  mul.io.dataIn1 := dataIn1
  hiReady := mul.io.hiReady
  loReady := mul.io.loReady

  when (mul.io.hiReady) (hi := mul.io.hiOut)
  when (mul.io.loReady) (lo := mul.io.loOut)

}