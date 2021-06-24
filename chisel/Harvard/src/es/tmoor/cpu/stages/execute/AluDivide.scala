package es.tmoor.cpu.stages.execute

import chisel3._

class AluDivide extends Module {
  object AluDivideIO extends Bundle {
    val beginOp = Input(Bool())
    val dataIn0 = Input(UInt(32.W))
    val dataIn1 = Input(UInt(32.W))
    val hiReady = Output(Bool())
    val loReady = Output(Bool())
    val hiOut = Output(UInt(32.W))
    val loOut = Output(UInt(32.W))
    val divBy0 = Output(Bool())
  }
  val io = IO(AluDivideIO)
  import io._

  val opDone = RegInit(Bool(), 0.B)
  hiReady := opDone
  loReady := opDone

  val hi = RegInit(UInt(32.W), 0.U)
  val lo = RegInit(UInt(32.W), 0.U)
  hiOut := hi
  loOut := lo
  
  val dataIn0Reg = RegInit(UInt(32.W), 0.U)
  val dataIn1Reg = RegInit(UInt(32.W), 0.U)
  val dataIn0Wire = WireInit(UInt(32.W),0.U)
  val dataIn1Wire = WireInit(UInt(32.W),0.U)
  
  divBy0 := dataIn1Wire === 0.U

  when (beginOp) (dataIn0Reg := dataIn0)
  when (beginOp) (dataIn1Reg := dataIn1)

  when (beginOp) (dataIn0Wire := dataIn0) otherwise (dataIn0Wire := dataIn0Reg)
  when (beginOp) (dataIn1Wire := dataIn1) otherwise (dataIn1Wire := dataIn1Reg)

  val s = RegInit(UInt(6.W), 63.U)
  when (beginOp) {
    hi := 0.U
    lo := 0.U
    s := 31.U
  }
  when ((s < 32.U)) {
    s := s - 1.U
    printf(p"s = $s, hi = $hi, lo = $lo\n")
  }
  when (s === 0.U) {
    opDone := 1.B
  } otherwise {
    opDone := 0.B
  }

  val hiCalc = WireInit(UInt(32.W),0.U)
  val loCalc = WireInit(UInt(32.W),0.U)
  hiCalc := (hi << 1.U) | ((dataIn0Wire & (1.U << s)) >> s)
  loCalc := lo << 1.U

  when (hiCalc >= dataIn1Wire) {
    hi := hiCalc - dataIn1Wire
    lo := (loCalc | 1.U)
  } otherwise {
    hi := hiCalc
    lo := loCalc
  }
}
