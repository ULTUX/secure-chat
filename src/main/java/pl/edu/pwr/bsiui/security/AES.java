package pl.edu.pwr.bsiui.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.util.Base64;

public class AES {
    private final Key aesKey;

    public AES(Key aesKey) {
        this.aesKey = aesKey;
    }

    public String encryptAES(String data) throws Exception {
        var encryptionCipher = Cipher.getInstance("AES");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] decryptedMessage = encryptionCipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(decryptedMessage);
    }

    public String decryptAES(String encryptedData) throws Exception {
        Cipher decryptionCipher = Cipher.getInstance("AES");
        decryptionCipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedMessage = decryptionCipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedMessage);
    }

    public static Key generateAesKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        return key;

    }
}
