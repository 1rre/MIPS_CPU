package es.tmoor.cpu.stages

import chisel3._

class MemoryWrite extends Module {
  object MemoryWriteIO extends Bundle {

  }
  val io = IO(MemoryWriteIO)
  import io._
  
}