
import ch.qos.logback.core.encoder.ByteArrayUtil;
import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;


public class FileProcessor {

    /**
     * {@link RandomAccessFile#length()} returns value measure in bytes!
     * 126 bytes == 1008 bits
     */
    @SneakyThrows
    public RandomAccessFile generateFile() {
        RandomAccessFile randomAccessFile = new RandomAccessFile("idea-cipher", "rw");
        randomAccessFile.setLength(4);

        byte[] zeroArray = generateByteArray(2, 0);
        byte[] unitArray = generateByteArray(2, 1);

        write(randomAccessFile, zeroArray, 0);

        randomAccessFile = new RandomAccessFile("idea-cipher", "rw");
        byte[] result = read(randomAccessFile);

        /* TODO everything is ready to feed file with data */

        Main.LOG.info("File generated: {}", randomAccessFile);
        return randomAccessFile;
    }

    @SneakyThrows
    private void write(RandomAccessFile file, byte[] array, int startPosition) {
        file.seek(startPosition);
        file.write(array);
        file.close();
    }

    @SneakyThrows
    private byte[] read(RandomAccessFile file) {
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

}
