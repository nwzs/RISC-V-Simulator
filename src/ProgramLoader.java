import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramLoader {
    private Assembler assembler;

    public ProgramLoader() {
        this.assembler = new Assembler();
    }

    // Read a .asm file, assemble each line, and load into memory
    public void load(String filename, Memory memory) {
        List<Integer> instructions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0; // Start at address 0

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip blank lines and comments
                if (line.isEmpty() || line.startsWith("#")) continue;

                try {
                    int binary = assembler.assemble(line);
                    if (binary != -1) {
                        instructions.add(binary);
                    }
                } catch (Exception e) {
                    System.err.println("Error on line " + lineNumber + ": " + line);
                    System.err.println("  " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Could not open file: " + filename);
            e.printStackTrace();
            return;
        }

        // Write all assembled instructions into memory
        for (int i = 0; i < instructions.size(); i++) {
            memory.writeWord(i * 4, instructions.get(i));
        }

        System.out.println("Loaded " + instructions.size() + " instructions from " + filename);
    }
}