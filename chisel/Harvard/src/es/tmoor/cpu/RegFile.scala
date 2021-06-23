package es.tmoor.cpu

import chisel3._

class RegFile extends Module {
  class RegFileIO extends Bundle {
    val writeEn = Input(Bool())
    val readAddr0 = Input(UInt(5.W))
    val readAddr1 = Input(UInt(5.W))
    val writeAddr = Input(UInt(5.W))
    val readData0 = Output(UInt(32.W))
    val readData1 = Output(UInt(32.W))
    val registerV0 = Output(UInt(32.W))
    val writeData = Input(UInt(32.W))
  }
  val io = IO(new RegFileIO)
  import io._
  val r = RegInit(Vec(32, UInt(32.W)),VecInit(Array.fill(32)(0.U)))
  registerV0 := r(2)
  readData0 := r(readAddr0)
  readData1 := r(readAddr1)
  when(writeEn.asBool && (writeAddr =/= 0.U)) {
    r(writeAddr) := writeData
  }
}