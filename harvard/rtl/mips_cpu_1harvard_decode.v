`timescale 1ps/1ps
module harvard_decode (
input
  clk, reset, enable,
input [31:0]
  instruction,
output reg
  mem_active, reg_active,
);

wire _mem_active, _reg_active;

wire[25:0] address;
wire[15:0] immediate;
wire [5:0] opcode, fun;
wire [4:0] rs, rt, rd, shift;
assign address[25:0] = instruction[25:0];
assign immediate[15:0] = instruction[15:0];
assign opcode[5:0] = instruction[31:26];
assign fun[5:0] = instruction[5:0];
assign rs = instruction[25:21];
assign rd = instruction[20:15];
assign rt = instruction[15:10];
assign shift = instruction[10:5];

wire j_type, r_type, i_type;

assign j_type = opcode[5:1] == 5'b1;
assign r_type = opcode == 6'b0;
// i type has a lot of opcodes so this is much simpler
assign i_type = !(j_type | r_type);

// Update registers on a decode cycle
always @(posedge clk) if (enable) begin
  mem_active <= _mem_active;
  reg_active <= _reg_active;
end

endmodule