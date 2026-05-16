public class ALU {

    public enum Operation {
        ADD, SUB,
        AND, OR, XOR,
        SLT,
        SLTU,
        SLL, SRL, SRA
    }

    public int execute(Operation op, int a, int b) {
        switch(op) {
            case ADD: return a + b;
            case SUB: return a - b;
            case AND: return a & b;
            case OR: return a | b;
            case XOR: return a ^ b;
            case SLT: return (a < b) ? 1 : 0;
            case SLTU: return (Integer.compareUnsigned(a, b) < 0) ? 1 : 0;
            case SLL: return a << (b & 0x1F);
            case SRL: return a >>> (b & 0x1F);
            case SRA: return a >> (b & 0x1F);
            default:
                throw new IllegalArgumentException("Unknown ALU Operation: " + op);
        }
    }
}
