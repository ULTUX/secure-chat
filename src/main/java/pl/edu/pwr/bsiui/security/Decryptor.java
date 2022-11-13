package pl.edu.pwr.bsiui.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static pl.edu.pwr.bsiui.security.SecurityUtils.getCipher;

public class Decryptor {
    private final Key privKey;

    public Decryptor(Key privKey) {
        this.privKey = privKey;
    }

    public String decrypt(String text) throws Exception {
        var textBytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (textBytes.length > 172 * i + 1) {
            var toDecrypt = Arrays.copyOfRange(textBytes, i * 172, (i + 1) * 172);
            result.append(decrypt(toDecrypt));
            i++;
        }
        return result.toString();
    }


    private String decrypt(byte[] cipherContent) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, privKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherContent)));
    }
}
