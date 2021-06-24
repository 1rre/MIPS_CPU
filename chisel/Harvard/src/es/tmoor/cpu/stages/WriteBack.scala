package es.tmoor.cpu.stages

import chisel3._

class WriteBack extends Module {
  object WriteBackIO extends Bundle {
  
  }
  val io = IO(WriteBackIO)
  import io._

}