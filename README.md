# RISC-V CPU Simulator

A software implementation of a RISC-V processor written in Java. The simulator executes real RV32I assembly instructions through a complete fetch-decode-execute pipeline, with a built-in assembler that reads `.asm` files directly.

Built as a follow-up to an 8-bit ALU processor implemented in Verilog on an FPGA — this project bridges hardware architecture and software simulation.

---

## Features

- Full fetch → decode → execute pipeline
- 32-register file (x0 hardwired to zero per RISC-V spec)
- Byte-addressable memory model (64KB)
- Built-in assembler — write real assembly, no hex required
- Branch and loop support (BEQ, BNE)
- Register dump after execution for easy debugging

---

## Supported Instructions

| Type | Instructions |
|------|-------------|
| Arithmetic | ADD, SUB, ADDI |
| Logic | AND, OR, XOR, ANDI, ORI |
| Comparison | SLT, SLTI |
| Memory | LW, SW |
| Branch | BEQ, BNE |
| Jump | JAL |

---

## Project Structure

```
RISC-V-Simulator/
├── src/
│   ├── Main.java           # Entry point
│   ├── CPU.java            # Fetch-decode-execute loop, program counter
│   ├── RegisterFile.java   # 32 general-purpose registers
│   ├── Memory.java         # Byte-addressable memory with word read/write
│   ├── ALU.java            # Arithmetic and logic operations
│   ├── Instruction.java    # Decoded instruction representation
│   ├── Decoder.java        # Binary instruction decoder (opcode → Instruction)
│   ├── Assembler.java      # Text assembler (assembly → binary)
│   └── ProgramLoader.java  # Loads .asm files into memory
├── add.asm                 # Sample program: adds two numbers
└── countdown.asm           # Sample program: countdown loop using BNE
```

---

## How to Run

**Requirements:** Java JDK 17+, IntelliJ IDEA (or any Java IDE)

1. Clone the repository
2. Open in IntelliJ as a Java project
3. Set the program to load in `Main.java`:
```java
loader.load("add.asm", memory);
```
4. Run `Main.java`

---

## Sample Programs

### add.asm
```
ADDI x1, x0, 10   # x1 = 10
ADDI x2, x0, 20   # x2 = 20
ADD  x3, x1, x2   # x3 = 30
```
**Output:**
```
PC=0  | ADDI  rd=x1 rs1=x0 imm=10
PC=4  | ADDI  rd=x2 rs1=x0 imm=20
PC=8  | ADD   rd=x3 rs1=x1 rs2=x2
HALT - all-zero instructions reached
x1 = 10
x2 = 20
x3 = 30
```

### countdown.asm
```
ADDI x1, x0, 5    # x1 = 5 (counter)
ADDI x2, x0, 1    # x2 = 1 (step)
SUB  x1, x1, x2   # x1 = x1 - 1
BNE  x1, x0, -8   # loop until x1 == 0
```
**Output:**
```
PC=0  | ADDI  rd=x1 imm=5
PC=4  | ADDI  rd=x2 imm=1
PC=8  | SUB   rd=x1 rs1=x1 rs2=x2   ← loops 5 times
PC=12 | BNE   rs1=x1 rs2=x0 imm=-8
HALT - all-zero instructions reached
x1 = 0
```

---

## Architecture

```
         ┌─────────────┐
         │ ProgramLoader│  ← reads .asm file
         └──────┬──────┘
                │
         ┌──────▼──────┐
         │  Assembler   │  ← converts text to binary
         └──────┬──────┘
                │
         ┌──────▼──────┐
         │    Memory    │  ← stores binary instructions
         └──────┬──────┘
                │
    ┌───────────▼───────────┐
    │          CPU           │
    │  ┌──────────────────┐ │
    │  │  1. FETCH        │ │  ← reads word from Memory[PC]
    │  │  2. DECODE       │ │  ← Decoder extracts fields
    │  │  3. EXECUTE      │ │  ← ALU + RegisterFile + PC update
    │  └──────────────────┘ │
    └───────────────────────┘
```

---

## Related Projects

- [8-Bit Micro-coded ALU Processor](https://github.com/nwzs/8-Bit-ALU-Processor-Design) — hardware implementation of an ALU in Verilog, deployed on an Intel DE2 FPGA board

---

*Developed independently as a computer architecture portfolio project — Toronto Metropolitan University, 2025*
