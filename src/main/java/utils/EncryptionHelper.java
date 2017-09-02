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
    public static String des(byte[] key, byte[] plainText) {
        SecretKey secretKey = new SecretKeySpec(key, "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypted = desCipher.doFinal(plainText);
        return Base64.getEncoder().encodeToString(encrypted);
    }

}
