package utils;

import lombok.SneakyThrows;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Class used to encrypt data with different ciphers
 */
public class EncryptionHelper {

    @SneakyThrows
    public static String desWithBase64(byte[] key, byte[] plainText) {
        SecretKey secretKey = new SecretKeySpec(key, "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypted = desCipher.doFinal(plainText);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Converts byte value (-59) into
     * binary format (1111111111111111111111111111111111111111111111111111111111000101)
     * and appends to StringBuilder.
     */
    @SneakyThrows
    public static String desWithoutBase64(byte[] key, byte[] plainText) {
        SecretKey secretKey = new SecretKeySpec(key, "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] bytes = desCipher.doFinal(plainText);
        StringBuilder stringBuilder = new StringBuilder();

        for (Byte byteValue : bytes) {
            int intValue = byteValue.intValue();
            stringBuilder.append(Integer.toBinaryString(intValue));
        }
        return stringBuilder.toString();
    }

    @SneakyThrows
    public static byte[] desWithoutBase64ToBytes(byte[] key, byte[] plainText) {
        SecretKey secretKey = new SecretKeySpec(key, "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return desCipher.doFinal(plainText);
    }

}
