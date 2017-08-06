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
 *
 Test data:

 19:19:25.744 [main] INFO  Main - File size: 96 bytes
 19:19:25.767 [main] INFO  Main - DES-pure ( H = 5.514192 )
 19:19:25.778 [main] INFO  Main - DES-ideal ( H = 5.727921 )
 19:19:25.778 [main] INFO  Main - DES-ideal - DES-pure = 5.727921 - 5.514192 = 0.213729


 19:19:25.846 [main] INFO  Main - File size: 1 MB
 19:19:29.379 [main] INFO  Main - DES-pure ( H = 5.999995 )
 19:19:29.741 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:19:29.741 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999995 = 0.022373


 19:19:29.765 [main] INFO  Main - File size: 2 MB
 19:19:33.951 [main] INFO  Main - DES-pure ( H = 5.999984 )
 19:19:35.812 [main] INFO  Main - DES-ideal ( H = 6.000000 )
 19:19:35.812 [main] INFO  Main - DES-ideal - DES-pure = 6.000000 - 5.999984 = 0.000016


 19:19:35.834 [main] INFO  Main - File size: 3 MB
 19:19:44.676 [main] INFO  Main - DES-pure ( H = 6.000001 )
 19:19:45.393 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:19:45.393 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 6.000001 = 0.022367


 19:19:45.424 [main] INFO  Main - File size: 5 MB
 19:19:55.574 [main] INFO  Main - DES-pure ( H = 5.999998 )
 19:19:59.378 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:19:59.378 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999998 = 0.022370


 19:19:59.513 [main] INFO  Main - File size: 6 MB
 19:20:14.720 [main] INFO  Main - DES-pure ( H = 6.000001 )
 19:20:18.794 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:20:18.794 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 6.000001 = 0.022367


 19:20:19.126 [main] INFO  Main - File size: 7 MB
 19:20:36.623 [main] INFO  Main - DES-pure ( H = 5.999999 )
 19:20:42.264 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:20:42.264 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999999 = 0.022369


 19:20:42.318 [main] INFO  Main - File size: 9 MB
 19:20:59.970 [main] INFO  Main - DES-pure ( H = 5.999997 )
 19:21:06.509 [main] INFO  Main - DES-ideal ( H = 6.000000 )
 19:21:06.509 [main] INFO  Main - DES-ideal - DES-pure = 6.000000 - 5.999997 = 0.000003


 19:21:06.938 [main] INFO  Main - File size: 10 MB
 19:21:31.792 [main] INFO  Main - DES-pure ( H = 5.999999 )
 19:21:38.656 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 19:21:38.656 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999999 = 0.022369


 19:21:39.062 [main] INFO  Main - File size: 12 MB
 19:22:01.485 [main] INFO  Main - DES-pure ( H = 5.999998 )
 19:22:13.934 [main] INFO  Main - DES-ideal ( H = 5.989785 )
 19:22:13.934 [main] INFO  Main - DES-ideal - DES-pure = 5.989785 - 5.999998 = -0.010213
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static FileProcessor fileProcessor;

    private static Entropy entropyCalculator;


    public static void main(String[] args) {
        fileProcessor = new FileProcessor();
        entropyCalculator = new Entropy();

        Cipher cipher = Cipher.DES;

        /* TODO there is strange workflow with 2, 8, 10 MB files? */
        List<File> files = fileProcessor.getTextFilesWithName(cipher.getPath(), 1);

        /*List<File> files = fileProcessor.getTextFiles(cipher.getPath());*/

        files.forEach(file -> {
            String cipherText = fileProcessor.getText(file);
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(cipherText.getBytes().length));

            BigDecimal h0 = entropyCalculator.calculate(cipher.getPure(), cipherText);

            List<String> letters = entropyCalculator.getCipherTextAsList(cipherText);
            setupListOfIdealCipher(letters, entropyCalculator.getAlphabet(cipherText));
            BigDecimal h1 = entropyCalculator.calculate(cipher.getIdeal(), letters.parallelStream().collect(joining(EMPTY)));

            LOG.info("{} - {} = {} - {} = {}", cipher.getIdeal(), cipher.getPure(), h1, h0, h1.subtract(h0));
            System.out.println("\n");
        });
    }

    private static void setupListOfIdealCipher(List<String> letters, Set<String> alphabet) {
        int lettersSize = letters.size();
        int alphabetSize = alphabet.size();
        letters.clear();

        alphabet.forEach(letter -> {
            if ( lettersSize > alphabetSize ) {
                int iterations = findIterations(alphabetSize, lettersSize);

                if ( alphabetSize * iterations >= lettersSize ) {
                    for (int i = 0; i < iterations; i++) {
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
