`timescale 1ps/1ps
module mips_cpu_harvard (
  /* Standard signals */
  input logic     clk,
  input logic     reset,
  output logic    active,
  output logic [31:0] register_v0,

  /* New clock enable. See below. */
  input logic     clk_enable,

  /* Combinatorial read access to instructions */
  output logic[31:0]  instr_address,
  input logic[31:0]   instr_readdata,

  /* Combinatorial read and single-cycle write access to instructions */
  output logic[31:0]  data_address,
  output logic        data_write,
  output logic        data_read,
  output logic[31:0]  data_writedata,
  input logic[31:0]  data_readdata
);

reg   [4:0] read_addr0, read_addr1, write_addr;
wire [31:0] read_data0, read_data1;
reg  [31:0] write_data, counter;

reg [0:4] stage;
wire fetch, decode, execute, memory, writeback;

assign fetch = stage[0];
assign decode = stage[1];
assign execute = stage[2];
assign memory = stage[3];
assign writeback = stage[4];

initial begin
  counter = 0;
  stage = 5'b1;
end

always_ff @(posedge clk) begin
  if (reset) begin
    stage[0] <= 1;
    stage[1:4] <= 4'b0;
    // Reset operations
  end else if (clk_enable) begin
    // Clock tick operations
    stage[0:4] <= {stage[4], stage[0:3]};
    $display("%b", stage);
  end
end

harvard_fetch submod_fetch (
  .clk(clk),
  .reset(reset),
  .branch_en(1'b0),
  .pc(instr_address),
  .pc_en(1'b1),
  .branch(32'b0)
);

reg_file submod_registers (
  // Ctrl Ports
  .clk(clk),
  .reset(reset),
  .write_en(1'b1),
  // Addressing
  .read_addr0(read_addr0),
  .read_addr1(read_addr1),
  .read_addr2(5'd2),
  .write_addr(write_addr),
  // Data
  .read_data0(read_data0),
  .read_data1(read_data1),
  .read_data2(register_v0),
  .write_data(write_data)
);

endmodule