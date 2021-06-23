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

  val waitHi = RegInit(Bool(), 0.B)
  val waitLo = RegInit(Bool(), 0.B)

  val lastOp = RegInit(Bool(), opIn)
  when (beginOp) {
    lastOp := opIn
    waitHi := 1.B
    waitLo := 1.B
  }

  val hi = RegInit(UInt(32.W), 0.U)
  val lo = RegInit(UInt(32.W), 0.U)

  hiOut := hi
  loOut := lo

  val mul = Module(new AluMultiply)
  mul.io.beginOp := beginOp & opIn
  mul.io.dataIn0 := dataIn0
  mul.io.dataIn1 := dataIn1

  val div = Module(new AluDivide)
  div.io.beginOp := beginOp & !opIn
  div.io.dataIn0 := dataIn0
  div.io.dataIn1 := dataIn1

  when (lastOp) {
    hiReady := mul.io.loReady
    loReady := mul.io.hiReady
  }

  hiReady := mul.io.hiReady

  // Only overwrite hi/lo with mul.hi/mul.lo if the last op was mul
  when (mul.io.hiReady & lastOp) {
    hi := mul.io.hiOut
    waitHi := 0.B
  }
  when (mul.io.loReady & lastOp) {
    lo := mul.io.loOut
    waitLo := 0.B
  }

  // Only overwrite hi/lo with div.hi/div.lo if the last op was div
  when (div.io.hiReady & !lastOp) {
    hi := div.io.hiOut
    waitHi := 0.B
  }
  when (div.io.loReady & !lastOp) {
    hi := div.io.loOut
    waitLo := 0.B
  }
  

}