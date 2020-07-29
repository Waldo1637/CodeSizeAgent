package inst.agent.codesize;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Timothy Hoffman
 */
public class ClassSizeLog {

    public static final String OUTPUT_FILE = "./codeSize.csv";

    private final StringBuilder data = new StringBuilder();
    private long total = 0;

    public void log(String className, int classSize) {
        total += classSize;
        data.append(className).append(',').append(classSize).append('\n');
    }

    public void printToFile() {
        data.append("TOTAL,").append(total).append('\n');

        byte[] bytes = data.toString().getBytes();
        try {
            Files.write(Paths.get(OUTPUT_FILE), bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
