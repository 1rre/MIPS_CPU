`timescale 1ps/1ps
module harvard_fetch (
  input
    clk, reset, branch_en, pc_en,
  output reg [31:0]
    pc,
  input [31:0]
    branch
);

always @(posedge clk) if (reset) begin
  pc <= 32'hBCE00000;
end else if (branch_en) begin
  pc <= branch;
end else if (pc_en) begin
  pc <= pc + 4;
end

endmodule