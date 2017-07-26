import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        Arrays.stream(Cipher.values()).forEach(cipher -> {
            String cipherText = cipher.getCipherText();
            BigDecimal h0 = entropyCalculator.calculate(cipher.getPure(), cipherText);

            List<String> letters = entropyCalculator.getCipherTextAsList(cipherText);
            setupListOfIdeaCipher(letters, entropyCalculator.getAlphabet(cipherText));
            BigDecimal h1 = entropyCalculator.calculate(cipher.getIdeal(), letters.stream().collect(Collectors.joining(StringUtils.EMPTY)));

            LOG.info("{} - {} = {} - {} = {}", cipher.getIdeal(), cipher.getPure(), h1, h0, h1.subtract(h0));
            System.out.println("\n");
        });
    }

    private static void setupListOfIdeaCipher(List<String> letters, Set<String> alphabet) {
        int lettersSize = letters.size();
        int alphabetSize = alphabet.size();
        letters.clear();

        alphabet.forEach(letter -> {
            if (lettersSize > alphabetSize) {
                if (alphabetSize * 2 >= lettersSize) {
                    for (int i = 0; i < 2; i++) {
                        letters.add(letter);
                    }
                }
            }
        });
    }

}
