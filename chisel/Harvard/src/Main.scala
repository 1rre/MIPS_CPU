import chisel3.stage.ChiselStage
import es.tmoor.cpu._

object Main extends App with chisel3.BackendCompilationUtilities {
/*  new ChiselStage().emitVerilog (
    new HarvardCPU,
    Array(
      //"-d", "",
      "-o", "mips_cpu/HarvardCPU.v"
    )
  )*/
  new ChiselStage().emitVerilog(new alu.AluDivide, Array("-o", "test/AluDivide.v"))
}
