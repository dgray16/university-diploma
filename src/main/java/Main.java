import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.EncryptionHelper;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
 * Info:
 * <ul>
 *     <li>https://ru.wikipedia.org/wiki/%D0%A1%D1%82%D0%B0%D1%82%D0%B8%D1%81%D1%82%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B5_%D1%82%D0%B5%D1%81%D1%82%D1%8B_NIST</li>
 *     <li>http://nvlpubs.nist.gov/nistpubs/Legacy/SP/nistspecialpublication800-22r1a.pdf</li>
 *     <li>https://github.com/kravietz/nist-sts</li>
 *     <li>https://ru.wikipedia.org/wiki/%D0%A4%D0%BE%D1%80%D0%BC%D1%83%D0%BB%D0%B0_%D0%A5%D0%B0%D1%80%D1%82%D0%BB%D0%B8</li>
 *     <li>http://www.binaryhexconverter.com/decimal-to-binary-converter</li>
 * </ul>
 *
 Test text data:

 20:56:13.832 [main] INFO  Main - File size: 96 bytes
 20:56:13.861 [main] INFO  Main - DES-pure ( H = 5.514192 )
 20:56:13.873 [main] INFO  Main - DES-ideal ( H = 5.727921 )
 20:56:13.873 [main] INFO  Main - DES-ideal - DES-pure = 5.727921 - 5.514192 = 0.213729


 20:56:14.015 [main] INFO  Main - File size: 1 MB
 20:56:17.520 [main] INFO  Main - DES-pure ( H = 5.999995 )
 20:56:20.011 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:56:20.011 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999995 = 0.022373


 20:56:20.031 [main] INFO  Main - File size: 2 MB
 20:56:24.005 [main] INFO  Main - DES-pure ( H = 5.999984 )
 20:56:30.234 [main] INFO  Main - DES-ideal ( H = 6.000000 )
 20:56:30.234 [main] INFO  Main - DES-ideal - DES-pure = 6.000000 - 5.999984 = 0.000016


 20:56:30.270 [main] INFO  Main - File size: 3 MB
 20:56:36.472 [main] INFO  Main - DES-pure ( H = 6.000001 )
 20:56:45.714 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:56:45.714 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 6.000001 = 0.022367


 20:56:45.744 [main] INFO  Main - File size: 5 MB
 20:56:54.178 [main] INFO  Main - DES-pure ( H = 5.999998 )
 20:57:09.162 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:57:09.162 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999998 = 0.022370


 20:57:09.219 [main] INFO  Main - File size: 6 MB
 20:57:20.637 [main] INFO  Main - DES-pure ( H = 6.000001 )
 20:57:36.319 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:57:36.319 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 6.000001 = 0.022367


 20:57:36.439 [main] INFO  Main - File size: 7 MB
 20:57:54.154 [main] INFO  Main - DES-pure ( H = 5.999999 )
 20:58:14.442 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:58:14.442 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999999 = 0.022369


 20:58:14.495 [main] INFO  Main - File size: 9 MB
 20:58:35.374 [main] INFO  Main - DES-pure ( H = 5.999997 )
 20:59:05.190 [main] INFO  Main - DES-ideal ( H = 6.000000 )
 20:59:05.190 [main] INFO  Main - DES-ideal - DES-pure = 6.000000 - 5.999997 = 0.000003


 20:59:05.258 [main] INFO  Main - File size: 10 MB
 20:59:24.531 [main] INFO  Main - DES-pure ( H = 5.999999 )
 20:59:53.765 [main] INFO  Main - DES-ideal ( H = 6.022368 )
 20:59:53.765 [main] INFO  Main - DES-ideal - DES-pure = 6.022368 - 5.999999 = 0.022369


 20:59:53.826 [main] INFO  Main - File size: 12 MB
 21:00:14.047 [main] INFO  Main - DES-pure ( H = 5.999998 )
 21:00:52.550 [main] INFO  Main - DES-ideal ( H = 6.000000 )
 21:00:52.550 [main] INFO  Main - DES-ideal - DES-pure = 6.000000 - 5.999998 = 0.000002
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final FileProcessor fileProcessor = new FileProcessor();

    private static final Entropy entropyCalculator = new Entropy();


    public static void main(String[] args) {
        List<File> files = fileProcessor.getFiles(Cipher.DES.getPath().concat(FileType.TEXT.getType()));

        files.forEach(file -> {
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(file.length()));
        });
        encrypt();
        /*runTextTest();*/
        /*runAudioTest();*/
        /*runVideoTest();*/
        /*runImageTest();*/
    }

    private static void runTextTest() {
        /* TODO there is strange workflow with 2, 8, 10 MB files? */
        /*List<File> files = fileProcessor.getTextFilesWithName(cipher.getPath(), 10);*/
        createTest(Cipher.DES, FileType.TEXT);
    }

    private static void runAudioTest() {
        createTest(Cipher.DES, FileType.AUDIO);
    }

    private static void runVideoTest() {
        createTest(Cipher.DES, FileType.VIDEO);
    }

    private static void runImageTest() {

        createTest(Cipher.DES, FileType.IMAGE);
    }

    private static void createTest(Cipher cipher, FileType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));

        files.forEach(file -> {
            String cipherText = fileProcessor.getText(file);
            LOG.info("Type: {}", fileType.getType());
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

    @SneakyThrows
    private static void encrypt() {
        /* TODO generate new raw, encrypted files */
        File file = fileProcessor.getFile("/raw/text/10.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        fileInputStream.close();

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        byte[] generatedKey = keyGenerator.generateKey().getEncoded();

        File file1 = fileProcessor.getFile("/encrypted/des/test.txt");

        /* Binary file */
        Files.write(Paths.get(file1.toURI()), EncryptionHelper.desWithoutBase64ToBytes(generatedKey, bytes));

        /* Now, you can take file from classes folder */
        System.out.println();
    }

}
