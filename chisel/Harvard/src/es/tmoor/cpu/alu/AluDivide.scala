package es.tmoor.cpu.alu

import chisel3._

class AluDivide extends Module {
  object AluDivideIO extends Bundle {
    val hiReady = Output(Bool())
    val loReady = Output(Bool())
  }
  val io = IO(AluDivideIO)
  import io._
  
}