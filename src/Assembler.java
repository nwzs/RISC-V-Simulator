public class Assembler {

    // Convert a single assembly instruction string to a 32-bit binary integer
    public int assemble(String line) {
        line = line.trim();

        // Remove inline comments
        if (line.contains("#")) {
            line = line.substring(0, line.indexOf("#")).trim();
        }

        if (line.isEmpty()) return -1; // Skip blank and comment lines

        String[] parts = line.split("[,\\s]+"); // Split on spaces and commas
        String op = parts[0].toUpperCase();

        switch (op) {

            // R-Type ALU
            case "ADD":  return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x0, 0x00);
            case "SUB":  return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x0, 0x20);
            case "AND":  return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x7, 0x00);
            case "OR":   return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x6, 0x00);
            case "XOR":  return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x4, 0x00);
            case "SLT":  return encodeR(0x33, reg(parts[1]), reg(parts[2]), reg(parts[3]), 0x2, 0x00);

            // I-Type ALU
            case "ADDI": return encodeI(0x13, reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x0);
            case "ANDI": return encodeI(0x13, reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x7);
            case "ORI":  return encodeI(0x13, reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x6);
            case "SLTI": return encodeI(0x13, reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x2);

            // Load
            case "LW":   return encodeI(0x03, reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x2);

            // Store
            case "SW":   return encodeS(reg(parts[1]), reg(parts[2]), imm(parts[3]));

            // Branch
            case "BEQ":  return encodeB(reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x0);
            case "BNE":  return encodeB(reg(parts[1]), reg(parts[2]), imm(parts[3]), 0x1);

            // Jump
            case "JAL":  return encodeJ(reg(parts[1]), imm(parts[2]));

            default:
                throw new IllegalArgumentException("Unknown instruction: " + op);
        }
    }

    // Parse register name: accepts "x1", "x2", etc.
    private int reg(String s) {
        s = s.trim().toLowerCase();
        if (s.startsWith("x")) {
            return Integer.parseInt(s.substring(1));
        }
        throw new IllegalArgumentException("Invalid register: " + s);
    }

    // Parse immediate value
    private int imm(String s) {
        return Integer.parseInt(s.trim());
    }

    // Encoding

    // R-type: funct7 | rs2 | rs1 | funct3 | rd | opcode
    private int encodeR(int opcode, int rd, int rs1, int rs2, int funct3, int funct7) {
        return (funct7 << 25) | (rs2 << 20) | (rs1 << 15) | (funct3 << 12) | (rd << 7) | opcode;
    }

    // I-type: imm[11:0] | rs1 | funct3 | rd | opcode
    private int encodeI(int opcode, int rd, int rs1, int imm, int funct3) {
        return ((imm & 0xFFF) << 20) | (rs1 << 15) | (funct3 << 12) | (rd << 7) | opcode;
    }

    // S-type: imm[11:5] | rs2 | rs1 | funct3 | imm[4:0] | opcode
    private int encodeS(int rs1, int rs2, int imm) {
        int imm11_5 = (imm >> 5) & 0x7F;
        int imm4_0  = imm & 0x1F;
        return (imm11_5 << 25) | (rs2 << 20) | (rs1 << 15) | (0x2 << 12) | (imm4_0 << 7) | 0x23;
    }

    // B-type: imm[12|10:5] | rs2 | rs1 | funct3 | imm[4:1|11] | opcode
    private int encodeB(int rs1, int rs2, int imm, int funct3) {
        int b12   = (imm >> 12) & 0x1;
        int b11   = (imm >> 11) & 0x1;
        int b10_5 = (imm >> 5)  & 0x3F;
        int b4_1  = (imm >> 1)  & 0xF;
        return (b12 << 31) | (b10_5 << 25) | (rs2 << 20) | (rs1 << 15) |
                (funct3 << 12) | (b4_1 << 8) | (b11 << 7) | 0x63;
    }

    // J-type: imm[20|10:1|11|19:12] | rd | opcode
    private int encodeJ(int rd, int imm) {
        int b20    = (imm >> 20) & 0x1;
        int b19_12 = (imm >> 12) & 0xFF;
        int b11    = (imm >> 11) & 0x1;
        int b10_1  = (imm >> 1)  & 0x3FF;
        return (b20 << 31) | (b10_1 << 21) | (b11 << 20) | (b19_12 << 12) | (rd << 7) | 0x6F;
    }
}
