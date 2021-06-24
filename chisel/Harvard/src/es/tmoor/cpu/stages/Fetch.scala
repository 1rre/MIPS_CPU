package es.tmoor.cpu.stages

import chisel3._

class Fetch extends Module {
  object FetchIO extends Bundle {
    val pcChangeEn = Input(Bool())
    val enable = Input(Bool())
    val pc = Output(UInt(32.W))
    val branchAddr = Input(UInt(32.W))
  }
  val io = IO(FetchIO)
  import io._
  val _pc = RegInit(UInt(32.W), 0xBCE00000l.U)
  pc := _pc
  when (enable && pcChangeEn) {
    _pc := branchAddr
  } otherwise {
    _pc := _pc + 4.U
  }
}