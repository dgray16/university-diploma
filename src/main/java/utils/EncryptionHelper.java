package utils;

import lombok.SneakyThrows;
import sun.security.jca.ProviderList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;

/**
 * Class used to encrypt data with different ciphers
 */
public class EncryptionHelper {

    @SneakyThrows
    public static String des(String key, byte[] plainText) {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        /* TODO so, how other services use 5 bytes key? */
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encypted = desCipher.doFinal(plainText);
        return Base64.getEncoder().encodeToString(encypted);
    }

}
