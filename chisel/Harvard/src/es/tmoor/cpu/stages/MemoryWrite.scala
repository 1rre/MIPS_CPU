package es.tmoor.cpu
package stages

import Util._
import chisel3._

class MemoryWrite extends Module {
  object MemoryWriteIO extends Bundle {
    val sExt = Input(Bool())
    val dataIn = Input(UInt(32.W))
    val dataOut = Output(UInt(32.W))
    val byteEn = Input(UInt(4.W))
  }
  val io = IO(MemoryWriteIO)
  import io._
  dataOut := dataIn
}