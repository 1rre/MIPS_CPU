package es.tmoor.cpu

import chisel3._

object Util {
  def oneOf(wire: UInt, seq: Seq[UInt]) = seq.foldLeft(0.B)((acc,v) => acc || (v === wire))
}