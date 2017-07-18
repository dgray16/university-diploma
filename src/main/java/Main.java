import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.stream.IntStream;


/**
 *
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        /* Ideal cipher */
        FileProcessor fileProcessor = new FileProcessor();
        RandomAccessFile file = fileProcessor.generateFile("idea-cipher");
        byte[] result = fileProcessor.read(file);

        /* Workaround to reuse stream */
        Supplier<IntStream> intStream = () -> IntStream.range(0, result.length).map(i -> result[i]);

        BigDecimal zerosCount = BigDecimal.valueOf(intStream.get().filter(value -> value == 0).count());
        BigDecimal unitsCount = BigDecimal.valueOf(intStream.get().filter(value -> value == 1).count());

        LOG.info(
                "P1 = zeroCount / length = {} / {} = {}",
                zerosCount, fileProcessor.getFileLength(file), zerosCount.divide(fileProcessor.getFileLength(file)));

        /* TODO get statistics for idea cipher */
    }

}
