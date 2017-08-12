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
    public static String des(byte[] key, byte[] plainText) {
        SecretKey secretKey = new SecretKeySpec(key, "DES");

        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encypted = desCipher.doFinal(plainText);
        return Base64.getEncoder().encodeToString(encypted);
    }

}
