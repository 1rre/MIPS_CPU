package es.tmoor.cpu

import chisel3._

object FunctionCodes {
  def add   = "b100000"U
  def addu  = "b100001"U
  def and   = "b100100"U
  def break = "b001101"U
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
  def nor   = "b100111"U
  def or    = "b100101"U
  def sll   = "b000000"U
  def sllv  = "b000100"U
  def slt   = "b101010"U
  def sltu  = "b101011"U
  def sra   = "b000011"U
  def srav  = "b000111"U
  def srl   = "b000010"U
  def srlv  = "b000110"U
  def sub   = "b100010"U
  def subu  = "b100011"U
  def sysc  = "b001100"U
  def xor   = "b100110"U
}