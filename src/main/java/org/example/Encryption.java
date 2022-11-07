package org.example;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {

    public static final int encodeWindowSize = 64;
    public static final int decodeWindowSize = 172;

    public static String encryptHugeText(String text, Key publicKey) throws Exception {
        var textBytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (text.getBytes().length > encodeWindowSize * (i + 1)) {
            var toEncrypt = Arrays.copyOfRange(textBytes, i * encodeWindowSize, (i + 1) * encodeWindowSize);
            result.append(encryptText(toEncrypt, publicKey));
            i++;
        }
        result.append(encryptText(Arrays.copyOfRange(textBytes, encodeWindowSize * (i), textBytes.length), publicKey));
        return result.toString();
    }

    public static String decryptHugeText(String text, Key privateKey) throws Exception {
        var textBytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (textBytes.length > decodeWindowSize * i + 1) {
            var toDecrypt = Arrays.copyOfRange(textBytes, i * decodeWindowSize, (i + 1) * decodeWindowSize);
            result.append(decryptText(toDecrypt, privateKey));
            i++;
        }
        return result.toString();
    }

    private static String encryptText(byte[] contentBytes, Key pubKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(contentBytes));
    }

    private static String decryptText(byte[] cipherContent, Key privKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherContent)));
    }
}
