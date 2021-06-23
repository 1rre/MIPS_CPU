package es.tmoor.cpu.stages

import chisel3._

class Execute extends Module {
  object ExecuteIO extends Bundle {
  }
  
  val io = IO(ExecuteIO)
  import io._
}