package application;

import dictionary.Cipher;
import dictionary.TestType;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.EncryptionHelper;
import utils.Entropy;
import utils.FileProcessor;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

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
 * File sizes:
 * <ul>
 *     <li>1 MB</li>
 *     <li>2 MB</li>
 *
 *     <li>3 MB</li>
 *     <li>4 MB</li>
 *
 *     <li>5 MB</li>
 *     <li>6 MB</li>
 *
 *     <li>7 MB</li>
 *     <li>8 MB</li>
 *
 *     <li>10 MB</li>
 *     <li>12 MB</li>
 * </ul>
 */
public class Main {

    public static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final FileProcessor fileProcessor = new FileProcessor();

    private static final Entropy entropyCalculator = new Entropy();


    public static void main(String[] args) {
        //encrypt();

        /*List<File> files = fileProcessor.getFiles(dictionary.Cipher.DES.getPath().concat(dictionary.TestType.TEXT_DOUBLE.getType()));
        files.forEach(file -> {
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(file.length()));
        });*/
        
        runTextTest();
        /*runAudioTest();*/
        /*runVideoTest();*/
        /*runImageTest();*/
    }

    private static void runTextTest() {
        createBinaryTest(Cipher.DES, TestType.TEXT_DOUBLE);
        //createBinaryTest(Cipher.AES, TestType.TEXT_DOUBLE);
        //createBinaryTest(Cipher.BLOWFISH, TestType.TEXT_DOUBLE);
        //createBinaryTest(Cipher.RAW, TestType.TEST);

        //createBinaryTest(Cipher.XOR_WEAK, TestType.TEXT_DOUBLE);
        //createBinaryTest(Cipher.XOR_STRONG, TestType.TEXT_DOUBLE);

        // createTestWithoutIdeal(Cipher.RAW, TestType.TEST);

        //weakPseudorandomNumberGenerator(Cipher.RAW, TestType.TEXT_DOUBLE);
        //strongPseudorandomNumberGenerator(Cipher.RAW, TestType.TEXT_DOUBLE);
    }

    private static void runAudioTest() {
        /*createStringTest(dictionary.Cipher.DES, dictionary.TestType.AUDIO);*/
        createBinaryTest(Cipher.DES, TestType.AUDIO);
    }

    private static void runVideoTest() {
        /*createStringTest(dictionary.Cipher.DES, dictionary.TestType.VIDEO);*/
        createBinaryTest(Cipher.DES, TestType.VIDEO);
    }

    private static void runImageTest() {
        /*createStringTest(dictionary.Cipher.DES, dictionary.TestType.IMAGE);*/
        createBinaryTest(Cipher.DES, TestType.IMAGE);
    }

    @SneakyThrows
    private static void weakPseudorandomNumberGenerator(Cipher cipher, TestType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));

        File weakPseudorandom = fileProcessor.getFile(Cipher.RAW.getPath().concat(TestType.TEST.getType()).concat("0.txt"));
        byte[] randomBytes = fileProcessor.getBytesFromBinaryFile(weakPseudorandom);

        for (int i = INTEGER_ONE; i <= files.size(); i++) {
            Integer index = i - INTEGER_ONE;

            File file = files.get(index);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] encryptedText = IOUtils.toByteArray(fileInputStream);

            if (randomBytes.length < encryptedText.length) {
                throw new RuntimeException("There are less random bytes than incoming text, this case is not handled");
            }

            byte[] xoredBytes = new byte[encryptedText.length];

            for (int iterator = INTEGER_ZERO; iterator < encryptedText.length; iterator++) {
                xoredBytes[iterator] = (byte) (encryptedText[iterator] ^ randomBytes[iterator]);
            }

            String rootPath = Main.class.getResource("/encrypted/").getPath().toString();
            File file1 = new File(rootPath.concat(String.format("%s.txt", i)));

            Files.write(Paths.get(file1.toURI()), xoredBytes);
        }
    }

    @SneakyThrows
    private static void strongPseudorandomNumberGenerator(Cipher cipher, TestType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));
        SecureRandom randomValue = SecureRandom.getInstanceStrong();

        for (int i = 1; i <= files.size(); i++) {
            Integer index = i - 1;

            File file = files.get(index);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] encryptedText = IOUtils.toByteArray(fileInputStream);

            byte[] randomBytes = new byte[encryptedText.length];
            randomValue.nextBytes(randomBytes);

            byte[] xoredBytes = new byte[encryptedText.length];

            for (int iterator = 0; iterator < encryptedText.length; iterator++) {
                xoredBytes[iterator] = (byte) (encryptedText[iterator] ^ randomBytes[iterator]);
            }

            String rootPath = Main.class.getResource("/encrypted/").getPath().toString();
            File file1 = new File(rootPath.concat(String.format("%s.txt", i)));
            /* Binary file */
            Files.write(Paths.get(file1.toURI()), xoredBytes);
        }
    }

    /**
     * This test is created to work with text like: "Hello worlds, I am Vova and this is some text I am typing."
     */
    private static void createStringTest(Cipher cipher, TestType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));

        files.forEach(file -> {
            String cipherText = fileProcessor.getTextFromFile(file);
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

    /**
     * This test is created to work with binary files in byte format like: [12, 545, 24, -12].
     */
    private static void createBinaryTest(Cipher cipher, TestType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));

        files.forEach(file -> {
            byte[] plainBytes = fileProcessor.getBytesFromBinaryFile(file);
            LOG.info("Type: {}", fileType.getType());
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(plainBytes.length));

            BigDecimal h0 = entropyCalculator.calculate(cipher.getPure(), plainBytes);

            List<Integer> letters = entropyCalculator.getCipherTextAsList(plainBytes);
            setupListOfIdealCipherForBinary(letters, entropyCalculator.getAlphabet(plainBytes));

            byte[] lettersInBytes = new byte[letters.size()];
            for (int i = 0; i < letters.size(); i++) {
                lettersInBytes[i] = letters.get(i).byteValue();
            }

            BigDecimal h1 = entropyCalculator.calculate(cipher.getIdeal(), lettersInBytes);

            LOG.info("{} - {} = {} - {} = {}", cipher.getIdeal(), cipher.getPure(), h1, h0, h1.subtract(h0));
            System.out.println("\n");
        });
    }

    /**
     * It shows data only for current result, ideal sequence is not calculating.
     */
    private static void createTestWithoutIdeal(Cipher cipher, TestType fileType) {
        List<File> files = fileProcessor.getFiles(cipher.getPath().concat(fileType.getType()));

        files.forEach(file -> {
            byte[] plainBytes = fileProcessor.getTextFromFile(file).getBytes();
            LOG.info("Type: {}", fileType.getType());
            LOG.info("File size: {}", FileUtils.byteCountToDisplaySize(plainBytes.length));

            BigDecimal h0 = entropyCalculator.calculate(cipher.getPure(), plainBytes);
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

    private static void setupListOfIdealCipherForBinary(List<Integer> letters, Set<Integer> alphabet) {
        int lettersSize = letters.size();
        int alphabetSize = alphabet.size();
        letters.clear();

        alphabet.forEach(letter -> {
            if ( lettersSize > alphabetSize ) {
                /* Means that letters collection has duplicates and may be not be steady */
                int iterations = findIterations(alphabetSize, lettersSize);

                if ( alphabetSize * iterations >= lettersSize ) {
                    for (int i = 0; i < iterations; i++) {
                        letters.add(letter);
                    }
                }
            } else {
                /* Means that letters collection has no duplicates */
                letters.add(letter);
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
        List<File> files = fileProcessor.getFiles("/raw/text/twobooks/");
        String rootPath = Main.class.getResource("/encrypted/").getPath().toString();

        for (int i = 1; i <= files.size(); i++) {
            Integer index = i - 1;
            File file = files.get(index);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            fileInputStream.close();

            /* Do not forget to change ecryption method below! */
            //KeyGenerator keyGenerator = KeyGenerator.getInstance(Cipher.DES.getJavaCipherNotation());
            //KeyGenerator keyGenerator = KeyGenerator.getInstance(Cipher.AES.getJavaCipherNotation());
            KeyGenerator keyGenerator = KeyGenerator.getInstance(Cipher.BLOWFISH.getJavaCipherNotation());

            byte[] generatedKey = keyGenerator.generateKey().getEncoded();
            byte[] encryptedBytes = EncryptionHelper.blowfishToBytes(generatedKey, bytes);

            File file1 = new File(String.format(rootPath.concat("%s.txt"), i));

            Files.write(Paths.get(file1.toURI()), encryptedBytes);
        }

        /*File file = fileProcessor.getFile(String.format("/raw/text/%s.txt", 0));
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        fileInputStream.close();

        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        byte[] generatedKey = keyGenerator.generateKey().getEncoded();
        byte[] encryptedBytes = EncryptionHelper.desToBytes(generatedKey, bytes);

        File file1 = new File(String.format(rootPath.concat("%s.txt"), 0));

        Files.write(Paths.get(file1.toURI()), encryptedBytes);*/

        /* Now, you can take file from classes folder */
        System.out.println();
    }

}