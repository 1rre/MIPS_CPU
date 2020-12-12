module Program_Counter(
	input	logic clk,
	input 	logic [31:0] address_input,
	input	logic enable, //active low
	input	logic reset,
	output	logic halt,
	output	logic [31:0] address_output

);

	always_ff @(posedge clk, posedge reset) begin
		if(reset) begin
			address_output <= 32'hBFC00000;
			halt <= 0;
		end
		if (!enable && !reset && !halt) begin
			address_output <= address_input;
			if(address_input == 0) begin
				halt <= 1;
			end
		end
	end

endmodule