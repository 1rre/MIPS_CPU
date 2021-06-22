`timescale 1ps/1ps
module reg_file (
  input
    clk, reset, write_en,
  input       [4:0]
    read_addr0, read_addr1, read_addr2, write_addr,
  output [31:0]
    read_data0, read_data1, read_data2,
  input      [31:0]
    write_data
);

reg [31:0] _reg[32];

// MUXes used to select - easy to resolve during synthesis
assign read_data0 = _reg[read_addr0];
assign read_data1 = _reg[read_addr1];
assign read_data2 = _reg[read_addr2];

always_ff @(posedge clk) if (reset) begin
  for (int i = 0; i < 32; i = i + 1) begin
    _reg[i] <= 0;
  end
end else if (write_en) begin
  _reg[write_addr] <= write_data;
end

endmodule