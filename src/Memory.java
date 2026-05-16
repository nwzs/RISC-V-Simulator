import java.util.Map;
import java.util.HashMap;

public class Memory {
    private HashMap<Integer, Byte> memory;
    private static final int MEMORY_SIZE = 65536;

    public Memory() {
        memory = new HashMap<>();
    }

    public void writeByte(int address, byte value) {
        validateAddress(address);
        memory.put(address, value);
    }

    public byte readByte(int address) {
        validateAddress(address);
        return memory.getOrDefault(address, (byte) 0);
    }

    public void writeWord(int address, int value) {
        validateAddress(address);
        memory.put(address,     (byte)(value & 0xFF));
        memory.put(address + 1, (byte)((value >> 8) & 0xFF));
        memory.put(address + 2, (byte)((value >> 16) & 0xFF));
        memory.put(address + 3, (byte)((value >> 24) & 0xFF));
    }

    public int readWord(int address) {
        validateAddress(address);
        int b0 = readByte(address) & 0xFF;
        int b1 = readByte(address + 1) & 0xFF;
        int b2 = readByte(address + 2) & 0xFF;
        int b3 = readByte(address + 3) & 0xFF;
        return b0 | (b1 << 8) | (b2 << 16) | (b3 << 24);
    }

    private void validateAddress(int address) {
        if (address < 0 || address >= MEMORY_SIZE) {
            throw new IllegalArgumentException("Memory address out of range: " + address);
        }
    }

    public void dump() {
        System.out.println("Memory (non-zero)");
        memory.entrySet().stream()
                .filter(e -> e.getValue() !=0)
                .sorted(java.util.Map.Entry.comparingByKey())
                .forEach(e -> System.out.printf("0x%04X = 0x%02X%n", e.getKey(), e.getValue()));
    }

}
