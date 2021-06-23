package es.tmoor.cpu
package stages
import alu._
import FunctionCodes._

import chisel3._

class Execute extends Module {
  object ExecuteIO extends Bundle {
    val fun = Input(UInt(6.W))
    val input0 = Input(UInt(32.W))
    val input1 = Input(UInt(32.W))
    val output = Output(UInt(32.W))
    val hold = Output(Bool())
  }
  val io = IO(ExecuteIO)
  import io._
  val alu = Module(new AluMulDiv)
  alu.io.opIn := ((fun === mult) | (fun === multu))
  alu.io.dataIn0 := input0
  alu.io.dataIn1 := input1
  hold := ((fun === mfhi) && !alu.io.hiReady) || ((fun === mflo) && !alu.io.loReady)
  alu.io.beginOp := (!hold) && (alu.io.opIn || ((fun === div) | (fun === divu)))
  val result = RegInit(UInt(32.W), 0.U)
  output := result
  def resultWhen(f: UInt, res: UInt) = when (fun === f) (result := res)
  resultWhen(add | addu, input0 + input1)
  resultWhen(and, input0 & input1)
  resultWhen(mfhi, alu.io.hiOut)
  resultWhen(mflo, alu.io.loOut)
  resultWhen(or, input0 | input1)
  resultWhen(sll | sllv, input0 << Mux(input1 > 32.U, 32.U(6.W), input1(5,0)))
  resultWhen(srl | srlv, input0 >> input1)
  resultWhen(sra | srav, (input0.asSInt >> input1).asUInt)
  resultWhen(sltu, (input0 < input1).asUInt)
  resultWhen(slt, (input0.asSInt < input1.asSInt).asUInt)
  resultWhen(sub | subu, input0 - input1)
  resultWhen(xor, input0 ^ input1)
}