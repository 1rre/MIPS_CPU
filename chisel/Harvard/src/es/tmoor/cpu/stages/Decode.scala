package es.tmoor.cpu.stages

import chisel3._

class Decode extends Module {
  object DecodeIO extends Bundle {
    val enable = Input(Bool())
    val instruction = Input(UInt(32.W))
    val memActive = Output(Bool())
    val regActive = Output(Bool())
    val branchEn = Output(Bool())
    val srcReg0 = Output(UInt(5.W))
    val srcReg1 = Output(UInt(5.W))
    val destReg = Output(UInt(5.W))
  }
  val io = IO(DecodeIO)
  import io._
  def address = instruction(25,0)
  def immediate = instruction(15,0)
  def opcode = instruction(31,26)
  def fun = instruction(5,0)
  def rs = instruction(25,21)
  def rt = instruction(20,16)
  def rd = instruction(15,11)
  def shift = instruction(10,6)
  def jType = (opcode(5,1) === 1.U)
  def rType = (opcode === 0.U)
  def iType = !(jType | rType)

  val sr0 = RegInit(UInt(5.W), 0.U)
  val sr1 = RegInit(UInt(5.W), 0.U)
  val dr = RegInit(UInt(5.W), 0.U)

  srcReg0 := sr0
  srcReg1 := sr1
  destReg := dr

  val useMem = RegInit(Bool(), 0.U)
  val useReg = RegInit(Bool(), 0.U)

  regActive := useReg
  memActive := useMem
  
  branchEn := jType // DELETE

  when (enable) {
    useMem := jType // DELETE
    useReg := rType // DELETE
    sr0 := rs
    sr1 := rt
    when (iType) (dr := rt) otherwise (dr := rd)
  }
}