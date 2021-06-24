package es.tmoor.cpu

import chisel3._

object Opcodes {
  object IType {
    def addiu = "b001001"U
    def andi  = "b001100"U
    def beq   = "b000100"U
    def bgez  = "b000001"U // Also BGEZAL
    def bgtz  = "b000111"U
    def blez  = "b000110"U
    def bltz  = "b000001"U // Also BLTZAL
    def bne   = "b000101"U
    def lb    = "b100000"U
    def lbu   = "b100100"U
    def lh    = "b100001"U
    def lhu   = "b100101"U
    def lui   = "b001111"U
    def lw    = "b100011"U
    def lwl   = "b100010"U
    def lwr   = "b100110"U
    def ori   = "b001101"U
    def sb    = "b101000"U
    def sh    = "b101001"U
    def sw    = "b101011"U
    def slti  = "b001010"U
    def sltiu = "b001011"U
    def xori  = "b001110"U
  }

  object RType {
    def addu  = "b100001"U
    def and   = "b100100"U
    def div   = "b011010"U
    def divu  = "b011011"U
    def jalr  = "b001001"U
    def jr    = "b001001"U
    def mfhi  = "b010000"U
    def mflo  = "b010010"U
    def mthi  = "b010001"U
    def mtlo  = "b010011"U
    def mult  = "b011000"U
    def multu = "b011001"U
    def or    = "b100101"U
    def sll   = "b000000"U
    def sllv  = "b000100"U
    def slt   = "b101010"U
    def sltu  = "b101011"U
    def sra   = "b000011"U
    def srav  = "b000111"U
    def srl   = "b000010"U
    def srlv  = "b000110"U
    def subu  = "b100011"U
    def xor   = "b100110"U
  }

  object JType {
    def j     = "b000010"U
    def jal   = "b000011"U
  }
}