import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * AES Workflow:
 * 1. Get some plaintext.
 * 2. Encrypt it with AES.
 * 3. Calculate entropy of cipher text.
 * 4. Fill cipher text with alphabet letters uniformly.
 * 5. Calculate entropy of ideal cipher.
 * 6. Compare.
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static FileProcessor fileProcessor;

    private static Entropy entropyCalculator;

    public static void main(String[] args) {
        fileProcessor = new FileProcessor();
        entropyCalculator = new Entropy();

        Cipher cipher = Cipher.DES;
        List<File> files = fileProcessor.getTextFiles(cipher.getPath());

        files.forEach(file -> {
            String cipherText = fileProcessor.getText(file);
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(cipherText.getBytes().length));

            BigDecimal h0 = entropyCalculator.calculate(cipher.getPure(), cipherText);

            List<String> letters = entropyCalculator.getCipherTextAsList(cipherText);
            setupListOfIdeaCipher(letters, entropyCalculator.getAlphabet(cipherText));
            BigDecimal h1 = entropyCalculator.calculate(cipher.getIdeal(), letters.parallelStream().collect(joining(EMPTY)));

            LOG.info("{} - {} = {} - {} = {}", cipher.getIdeal(), cipher.getPure(), h1, h0, h1.subtract(h0));
            System.out.println("\n");
        });
    }

    private static void setupListOfIdeaCipher(List<String> letters, Set<String> alphabet) {
        int lettersSize = letters.size();
        int alphabetSize = alphabet.size();
        letters.clear();

        alphabet.parallelStream().forEach(letter -> {
            if ( lettersSize > alphabetSize ) {
                int iterations = findIterations(alphabetSize, lettersSize);

                if ( alphabetSize * iterations >= lettersSize ) {
                    for (int i = 0; i < 2; i++) {
                        letters.add(letter);
                    }
                }
            }
        });
    }

    private static int findIterations(int alphabetSize, int lettersSize) {
        int value = 1;
        boolean shouldWork = true;

        while (shouldWork) {
            if ( alphabetSize * value >= lettersSize ) {
                shouldWork = false;
            }
            value = shouldWork ? value + 1 : value;
        }
        return value;
    }

}
