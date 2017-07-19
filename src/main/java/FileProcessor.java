
import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;


public class FileProcessor {

    /** Length of file */
    private static final int length = 128;

    /**
     * {@link RandomAccessFile#length()} returns value measure in bytes!
     * 126 bytes == 1008 bits
     */
    @SneakyThrows
    public RandomAccessFile generateFile(String filename) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "rw");
        randomAccessFile.setLength(length);

        byte[] zeroArray = generateByteArray(length / 2, 0);
        byte[] unitArray = generateByteArray(length / 2, 1);

        write(randomAccessFile, zeroArray, 0);
        write(randomAccessFile, unitArray, length / 2);

        Main.LOG.info("File generated: {}", randomAccessFile);

        return randomAccessFile;
    }

    @SneakyThrows
    private void write(RandomAccessFile file, byte[] array, int startPosition) {
        file.seek(startPosition);
        file.write(array);
    }

    @SneakyThrows
    public byte[] read(RandomAccessFile file) {
        file.seek(0);
        byte[] data = new byte[(int) file.length()];
        file.readFully(data);
        return data;
    }

    private byte[] generateByteArray(int arraySize, int value) {
        byte[] array = new byte[arraySize];
        Arrays.fill(array, (byte) value);
        return array;
    }

    @SneakyThrows
    public BigDecimal getFileLength(RandomAccessFile file) {
        return BigDecimal.valueOf(file.length());
    }

}
