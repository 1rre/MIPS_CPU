`timescale 1ps/1ps
module mips_cpu_harvard (
input
  clk, reset, clk_enable,
output
  active, data_write, data_read,
input [31:0]
  instr_readdata, data_readdata,
output [31:0]
  register_v0, instr_address, data_address, data_writedata
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

// Submodules

harvard_fetch stage0_fetch (
  .clk(clk),
  .reset(reset),
  .branch_en(1'b0),
  .pc(instr_address),
  .enable(fetch),
  .branch(32'b0)
);

harvard_decode stage1_decode (
  .clk(clk),
  .reset(reset),
  .enable(decode)
);

harvard_execute stage2_execute (

);

harvard_memory stage3_memory (

);

harvard_writeback stage4_writeback (

);

reg_file submod_registers (
  // Ctrl Ports
  .clk(clk),
  .reset(reset),
  .write_en(1'b1),
  // Addressing
  .read_addr0(read_addr0),
  .read_addr1(read_addr1),
  .write_addr(write_addr),
  // Data
  .read_data0(read_data0),
  .read_data1(read_data1),
  .register_v0(register_v0),
  .write_data(write_data)
);

endmodule