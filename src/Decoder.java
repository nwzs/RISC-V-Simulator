public class Decoder {

    public Instruction decode(int raw) {

        int opcode = raw & 0x7F;

        int rd      = (raw >> 7) & 0x1F; // bits 11-7
        int funct3  = (raw >> 12) & 0x7; // bits 14-12
        int rs1     = (raw >> 15) & 0x1F; // bits 19-15
        int rs2     = (raw >> 20) & 0x1F; // bits 24-20
        int funct7  = (raw >> 25) & 0x7F; // bits 31-25

        switch (opcode) {

            case 0x33: // ADD, SUB, AND, OR, XOR, SLT
                return decodeRType(rd, rs1, rs2, funct3, funct7);

            case 0x13: // I-type: ADDI, ANDI, ORI, SLTI
                return decodeITypeALU(rd, rs1, funct3, raw);

            case 0x03: // I-type: LW (load word)
                return decodeLoad(rd, rs1, funct3, raw);

            case 0x23: // Store Word
                return decodeSType(rs1, rs2, funct3, raw);

            case 0x63: // BEQ, BNE
                return decodeBType(rs1, rs2, funct3, raw);

            case 0x6F: // JAL
                return decodeJType(rd, raw);

            default:
                throw new IllegalArgumentException("Unknown opcode: 0x" + Integer.toHexString(opcode));
        }
    }

    private Instruction decodeRType(int rd, int rs1, int rs2, int funct3, int funct7) {
        String name;
        switch (funct3) {
            case 0x0: name = (funct7 == 0x20) ? "SUB" : "ADD"; break;
            case 0x7: name = "AND"; break;
            case 0x6: name = "OR";  break;
            case 0x4: name = "XOR"; break;
            case 0x2: name = "SLT"; break;
            default:
                throw new IllegalArgumentException("Unknown R-Type funct3: " + funct3);
        }
        return new Instruction(Instruction.Type.R, name, rd, rs1, rs2, 0);
    }

    // I-Type ALU
    private Instruction decodeITypeALU(int rd, int rs1, int funct3, int raw) {
        int imm = (raw >> 20);
        imm = (imm << 20) >> 20; // Sign extended 12-bit immediate
        String name;
        switch (funct3) {
            case 0x0: name = "ADDI"; break;
            case 0x7: name = "ANDI";  break;
            case 0x6: name = "ORI"; break;
            case 0x2: name = "SLTI"; break;
            default:
                throw new IllegalArgumentException("Unknown I-Type funct3: " + funct3);
        }
        return new Instruction(Instruction.Type.I, name, rd, rs1, 0, imm);
    }

    // I-Type Load (LW)
    private Instruction decodeLoad(int rd, int rs1, int func3, int raw) {
        int imm = raw >> 20;
        return new Instruction(Instruction.Type.I, "LW", rd, rs1, 0, imm);
    }

    // S-Type (SW)
    private Instruction decodeSType(int rs1, int rs2, int funct3, int raw) {
        int imm = ((raw >> 25) << 5) | ((raw >> 7) & 0x1F);
        imm = (imm <<20) >> 20; // Sign extend
        return new Instruction(Instruction.Type.S, "SW", 0, rs1, rs2, imm);
    }

    // B-Type (BQE, BNE)
    private Instruction decodeBType(int rs1, int rs2, int funct3, int raw) {
        int imm = ((raw >> 31) << 12) |
                  (((raw >> 7) & 0x1) << 11) |
                  (((raw >> 25) & 0x3F) << 5) |
                  (((raw >> 8) & 0xF) << 1);
        imm = (imm << 19) >> 19;
        String name = (funct3 == 0x0) ? "BEQ" : "BNE";
        return new Instruction(Instruction.Type.B, name, 0, rs1, rs2, imm);
    }

    // J-Type (JAL)
    private Instruction decodeJType(int rd, int raw) {
        int imm = ((raw >> 31) << 20) |
                (((raw >> 12) & 0xFF) << 12) |
                (((raw >> 20) & 0x1) << 11) |
                (((raw >> 21) & 0x3FF) << 1);
        imm = (imm << 11) >> 11;
        return new Instruction(Instruction.Type.J, "JAL", rd, 0, 0, imm);
    }
}
