public class RegisterFile {
    private int[] registers;
    private static final int NUM_REGSITERS = 32;

    public RegisterFile() {
        registers = new int[NUM_REGSITERS];

    }

    public int read(int index) {
        if (index < 0 || index >= NUM_REGSITERS) {
            throw new IllegalArgumentException("Register index out of range: " + index);
        }
        return registers[index];
    }

    public void write(int index, int value) {
        if (index < 0 || index >= NUM_REGSITERS) {
            throw new IllegalArgumentException("Register index out of range: " + index);
        }

        if (index == 0) {
            return;
        }
        registers[index] = value;
    }

    public void dump() {
        System.out.println("Register File");
        for (int i = 0; i < NUM_REGSITERS; i++) {
            System.out.println("x" + i + " = " + registers[i]);
        }
    }
}
