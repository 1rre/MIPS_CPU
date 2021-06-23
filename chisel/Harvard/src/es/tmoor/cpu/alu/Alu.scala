package es.tmoor.cpu.alu

import chisel3._
import chisel3.util.MuxCase

class Alu extends Module {
  def add = 0x0.U // or sub
  def mul = 0x1.U // or mfhi
  def div = 0x2.U // or mflo
  def shiftL = 0x3.U
  def shiftA = 0x4.U
  def or = 0x5.U
  def and = 0x6.U
  def xor = 0x7.U
  object AluIO extends Bundle {
    val enable = Input(Bool())
    val aluOp = Input(UInt(6.W))
    val data0 = Input(UInt(32.W))
    val data1 = Input(UInt(32.W))
    val result = Output(UInt(32.W))
    val hold = Output(Bool())
  }
  val io = IO(AluIO)
  import io._

  val md = Module(new AluMulDiv)
  hold := !(md.io.hiReady | md.io.loReady)

  md.io.dataIn0 := data0
  md.io.dataIn1 := data1
  
  when (aluOp === add) {
    result := data0 + data1
  }
  when (aluOp === mul) {
    result := md.hi
    md.io.beginOp := 1.B
  }
  when (aluOp === div) {
    result := md.lo
    md.io.beginOp := 1.B
  }
  when (aluOp === shiftL) {
    when (data1.asSInt < 0.S) {
      result := data0 >> (-data1.asSInt).asUInt
    } otherwise {
      result := data0 << data1
    }
  }
  when (aluOp === shiftA) {
    when (data1.asSInt < 0.S) {
      result := data0.asSInt >> (-data1.asSInt).asUInt
    } otherwise {
      result := (data0.asSInt) << data1
    }
  }
  when (aluOp === or) {
    result := data0 | data1
  }
  when (aluOp === and) {
    result := data0 & data1
  }
  when (aluOp === xor) {
    result := data0 ^ data1
  }
}
