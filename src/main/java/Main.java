import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.log;


/**
 * TODO
 * 1. Get some plaintext.
 * 2. Encrypt it with AES.
 * 3. Calculate entropy of cipher text.
 * 4. Fill cipher text with 0/1.
 * 5. Calculate entropy of idea cipher.
 * 6. Compare.
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static FileProcessor fileProcessor;

    private static Entropy entropyCalculator;

    public static void main(String[] args) {
        fileProcessor = new FileProcessor();
        entropyCalculator = new Entropy();
        aes();
    }

    private static void aes() {
        String plainText = fileProcessor.getPlainText();

        /* AES block */
        /* TODO if enough time */
        String cipherText = "29v0r5/lm7YeAk7HR6yY3lLJQZogNuhcidaZ3kyJ5ZvN5hAuJDHCVXzqQzI0tyKjixjx6jV0Bx8+JBAu4GoG1xofy8UahS0nEGi54tahiUo=";
        BigDecimal h0 = entropyCalculator.calculate("AES-pure", cipherText);

        /* Ideal cipher */
        List<String> letters = entropyCalculator.getCipherTextAsList(cipherText);
        setupListOfIdeaCipher(letters);
        BigDecimal h1 = entropyCalculator.calculate("AES-idea", letters.stream().collect(Collectors.joining(StringUtils.EMPTY)));

        LOG.info("Ideal AES - AES = {} - {} = {}", h1, h0, h1.subtract(h0));
    }

    private static void setupListOfIdeaCipher(List<String> letters) {
        for (int i = 0; i < letters.size() / 2; i++) {
            letters.set(i, "0");
        }
        for (int i = letters.size() / 2; i < letters.size(); i++) {
            letters.set(i, "1");
        }
    }

    /**
     * Example
     */
    private static void idealCipher() {
        FileProcessor fileProcessor = new FileProcessor();
        RandomAccessFile file = fileProcessor.generateFile("ideal-cipher");
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
