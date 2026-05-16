public class Main {
    public static void main(String[] args) {
        RegisterFile rf = new RegisterFile();
        Memory memory = new Memory();
        CPU cpu = new CPU(rf, memory);
        ProgramLoader loader = new ProgramLoader();

        loader.load("countdown.asm", memory);
        cpu.run();
    }
}