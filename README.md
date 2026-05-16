# RISC-V-Simulator
A software implementation of a RISC-V processor written in Java. This simulator executes RV32I assembly instructions through a fetch-decode-execute pipeline. It also includes a built-in assembler that reads `.asm` files directly.

I built this as a follow-up to an 8-bit ALU processor implemented in Verilog on an FPGA. This project serves to bridge hardware architecture and software simulation.

---
### Features

* Full fetch &rarr; decode &rarr; execute pipeline
* 32-register file (x0 hardwired to zero)
* Byte-addressable memory model (64KB)
* Built-in assembler, no hex required
* Branch and loop support (BEQ, BNE)
* Register dump after execution (easy debugging)
--

### Supported Instructions

