public class CPU {
    private RegisterFile rf;
    private Memory memory;
    private  ALU alu;
    private Decoder decoder;
    private  int pc; //Program Counter - tracks instructions

    public CPU(RegisterFile rf, Memory memory) {
        this.rf = rf;
        this.memory = memory;
        this.alu = new ALU();
        this.decoder = new Decoder();
        this.pc = 0;
    }

    public void run() {
        System.out.println("Starting CPU Execution");
        while (true) {
            int raw = fetch();
            if (raw == 0) {
                System.out.println("HALT - all-zero instructions reached");
                break;
            }
            Instruction instr = decoder.decode(raw);
            System.out.println("PC=" + pc + " | " + instr);
            int oldPC = pc;
            execute(instr);
            if (pc == oldPC) {
                pc += 4; // Advances only if the execute did not change the PC
            }
        }
        System.out.println();
        rf.dump();
    }

    private int fetch() {
        return memory.readWord(pc);
    }

    private void execute(Instruction instr) {
        int a;
        int b;
        int result;

        switch (instr.getName()) {

            // R-Type ALU
            case "ADD":
                result = alu.execute(ALU.Operation.ADD, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            case "SUB":
                result = alu.execute(ALU.Operation.SUB, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            case "AND":
                result = alu.execute(ALU.Operation.AND, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            case "OR":
                result = alu.execute(ALU.Operation.OR, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            case "XOR":
                result = alu.execute(ALU.Operation.XOR, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            case "SLT":
                result = alu.execute(ALU.Operation.SLT, rf.read(instr.getRs1()), rf.read(instr.getRs2()));
                rf.write(instr.getRd(), result);
                break;

            // I-Type ALU
            case "ADDI":
                result = alu.execute(ALU.Operation.ADD, rf.read(instr.getRs1()), instr.getImmediate());
                rf.write(instr.getRd(), result);
                break;

            case "ANDI":
                result = alu.execute(ALU.Operation.AND, rf.read(instr.getRs1()), instr.getImmediate());
                rf.write(instr.getRd(), result);
                break;

            case "ORI":
                result = alu.execute(ALU.Operation.OR, rf.read(instr.getRs1()), instr.getImmediate());
                rf.write(instr.getRd(), result);
                break;

            case "SLTI":
                result = alu.execute(ALU.Operation.SLT, rf.read(instr.getRs1()), instr.getImmediate());
                rf.write(instr.getRd(), result);
                break;

            // Load
            case "LW":
                int loadAddr = rf.read(instr.getRs1()) + instr.getImmediate();
                rf.write(instr.getRd(), memory.readWord(loadAddr));
                break;

            // Store
            case "SW":
                int storeAddr = rf.read(instr.getRs1()) + instr.getImmediate();
                memory.writeWord(storeAddr, rf.read(instr.getRs2()));
                break;

            // Branch
            case "BEQ":
                if (rf.read(instr.getRs1()) == rf.read(instr.getRs2())) {
                    pc += instr.getImmediate();
                }
                break;

            case "BNE":
                if (rf.read(instr.getRs1()) != rf.read(instr.getRs2())) {
                    pc += instr.getImmediate();
                }
                break;

            // Jump
            case "JAL":
                rf.write(instr.getRd(), pc + 4);
                pc += instr.getImmediate();
                break;

            default:
                throw new IllegalArgumentException("Unknown Instruction: " + instr.getName());
        }
    }

    public int getPc() { return pc; }
}
