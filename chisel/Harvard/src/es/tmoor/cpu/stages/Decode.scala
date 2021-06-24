package es.tmoor.cpu
package stages

import Util._
import Opcodes._,JType._,RType._,IType._
import chisel3._

class Decode extends Module {
  object DecodeIO extends Bundle {
    val enable = Input(Bool())
    val instructionIn = Input(UInt(32.W))
    val memActive = Output(Bool())
    val regActive = Output(Bool())
    val jumpEn = Output(Bool())
    val srcReg0 = Output(UInt(5.W))
    val srcReg1 = Output(UInt(5.W))
    val destReg = Output(UInt(5.W))
    val funcode = Output(UInt(6.W))
    val writeEn = Output(Bool())
    val readEn = Output(Bool())
    val memSExt = Output(Bool())
    val memByteEn = Output(UInt(4.W))
  }
  val io = IO(DecodeIO)
  import io._
  val instructionReg = RegInit(UInt(32.W), 0.U)
  val instruction = Mux(enable, instructionIn, instructionReg)
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

  srcReg0 := rs
  srcReg1 := rt
  destReg := rd
  
  // TODO: fun code on non i-type instructions
  funcode := fun
  
  // JumpEn will only be high here for jumps, not branches
  jumpEn := oneOf(opcode, Seq(j,jal,jalr,jr))
  memActive := oneOf(opcode, Seq(lb,lbu,lh,lhu,lw,lwl,lwr,sb,sh,sw))
  // I believe these are all valid?
  regActive := oneOf(opcode, Seq(
    jalr,lb,lbu,lh,lhu,lw,lwl,lwr,addiu,addu,and,andi,lui,
    mfhi,mflo,or,ori,sll,sllv,sra,srav,srl,srlv,subu,xor,xori
  ))

  writeEn := oneOf(opcode, Seq(sb,sh,sw))
  readEn := oneOf(opcode, Seq(lb,lbu,lh,lhu,lw,lwl,lwr))

  memSExt := oneOf(opcode, Seq(lbu,lhu))
  memByteEn := 15.U

  when (enable) {
    instructionReg := instruction
    when (iType) (dr := rt) otherwise (dr := rd)
  }
}