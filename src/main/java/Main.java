import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.Math.log;


/**
 *
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        idealCipher();
    }

    /**
     * Idea cipher
     */
    private static void idealCipher() {
        FileProcessor fileProcessor = new FileProcessor();
        RandomAccessFile file = fileProcessor.generateFile("idea-cipher");
        byte[] result = fileProcessor.read(file);

        /* Workaround to reuse stream */
        Supplier<IntStream> intStream = () -> IntStream.range(0, result.length).map(i -> result[i]);

        BigDecimal zerosCount = BigDecimal.valueOf(intStream.get().filter(value -> value == 0).count());
        BigDecimal unitsCount = BigDecimal.valueOf(intStream.get().filter(value -> value == 1).count());
        BigDecimal fileLenght = fileProcessor.getFileLength(file);

        double p1 = zerosCount.divide(fileLenght).doubleValue();
        LOG.info("P1 = zeroCount / length = {} / {} = {}", zerosCount, fileLenght, p1);

        double p2 = unitsCount.divide(fileLenght).doubleValue();
        LOG.info("P2 = unitCount / length = {} / {} = {}", unitsCount, fileLenght, p2);

        Double h0 = - (p1 * ( log(p1) / log(2) ) + p2 * ( log(p2) / log(2) ));
        LOG.info("H0 = -(p1 * log2p1 + p2 * log2p2) = {}", h0);
    }

}
