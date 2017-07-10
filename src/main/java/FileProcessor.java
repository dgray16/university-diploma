
import lombok.SneakyThrows;

import java.io.RandomAccessFile;

/**
 *
 */
public class FileProcessor {

    /**
     * {@link RandomAccessFile#length()} returns value measure in bytes!
     */
    @SneakyThrows
    public RandomAccessFile generateFile() {
        RandomAccessFile randomAccessFile = new RandomAccessFile("idea-cipher", "rw");
        randomAccessFile.setLength(125);

        /* TODO fill file with 50% of zeros and 50% of ones*/

        Main.LOG.info("File generated: {}", randomAccessFile);
        return randomAccessFile;
    }

}
