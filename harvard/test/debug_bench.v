`timescale 1ps/1ps
module debug_bench();

reg clk, reset;

wire [31:0]
  reg_v0, instr_address;

initial repeat (100) begin
  clk = 1;
  #10;
  clk = 0;
  #10;
  $display("PC: %h", instr_address);
end

initial begin
  reset <= 1;
  @(posedge clk);
  @(posedge clk);
  reset <= 0;
end

mips_cpu_harvard cpu (
  .clk(clk),
  .reset(reset),
  .clk_enable(1'b1),
  .register_v0(reg_v0),
  .instr_address(instr_address)
);



endmodule