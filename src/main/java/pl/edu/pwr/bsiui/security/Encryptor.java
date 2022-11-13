package pl.edu.pwr.bsiui.security;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static pl.edu.pwr.bsiui.security.SecurityUtils.getCipher;

public class Encryptor {
    private final Key pubKey;

    public Encryptor(Key pubKey) {
        this.pubKey = pubKey;
    }

    public String encrypt(String text) throws Exception {
        var bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (text.getBytes().length > 64 * (i + 1)) {
            var toEncrypt = Arrays.copyOfRange(bytes, i * 64, (i + 1) * 64);
            result.append(encrypt(toEncrypt));
            i++;
        }
        result.append(encrypt(Arrays.copyOfRange(bytes, 64 * (i), bytes.length)));
        return result.toString();
    }

    private String encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

}
