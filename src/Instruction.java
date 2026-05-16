public class Instruction {

    public enum Type {
        R, // Register - Register
        I, // Immediate
        S, // Store
        B, // Branch
        J // Jump
    }

    private Type type;
    private String name;
    private int rd; // Register destination
    private int rs1; // Source register 1
    private int rs2; // Source register 2
    private int immediate; // Immediate value

    public Instruction(Type type, String name, int rd, int rs1, int rs2, int immediate) {
        this.type = type;
        this.name = name;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.immediate = immediate;
    }

    public Type getType() {return type;}
    public String getName() {return name;}
    public int getRd() {return rd;}
    public int getRs1() {return rs1;}
    public int getRs2() {return rs2;}
    public int getImmediate() {return immediate;}

    @Override
    public String toString() {
        return String.format("%-6s rd=x%d rs1=x%d rs2=x%d imm=%d", name, rd, rs1, rs2, immediate);
    }
}
