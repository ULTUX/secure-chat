package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.json.request.Request;
import org.example.json.response.Response;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;


@Getter
@Setter
@Slf4j
public class SocketManager {
    public static final String connectionMessage = "Hi!";
    private Socket socket;
    private final ObjectMapper mapper;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 16123;
    private static final int controlMessageLength = 100;
    private PrivateKey clientPrivateKey;
    private String clientPublicKeyString;
    private PrintWriter out;
    private BufferedReader in;
    private PublicKey serverPublicKey;
    private Key aesKey;


    public SocketManager() throws IOException {
        this.mapper = new ObjectMapper();
    }

    public <T extends Response, N extends Request> T sendRequest(N request, Class<T> classType) throws Exception {
        String requestJson = mapper.writeValueAsString(request);
        System.out.println(requestJson);
        String encodedMsg = encryptAES(requestJson);
        out.println(encodedMsg);
        String encryptedResponse = in.readLine();
        String response = decryptAES(encryptedResponse);
        return mapper.readValue(response, classType);
    }

    private void generateClientKeys() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey clientPublicKey = kp.getPublic();
        clientPrivateKey = kp.getPrivate();
        clientPublicKeyString = Base64.getEncoder().encodeToString(clientPublicKey.getEncoded());
    }

    private void establishServerConnection() throws IOException {
        Socket clientSocket = new Socket(HOST, PORT);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(connectionMessage);
        int currentPort = Integer.parseInt(in.readLine());
        System.out.println("port =" + currentPort);
        clientSocket.close();

        Socket connectedSocket = new Socket(HOST, currentPort);
        out = new PrintWriter(connectedSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
    }

    private PublicKey getServerKey() throws Exception {
        out.println(clientPublicKeyString);
        String serverPublicKeyMessage = in.readLine();
        String decodedServerKey = Encryption.decryptHugeTextRSA(serverPublicKeyMessage, clientPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(decodedServerKey));

        return keyFactory.generatePublic(keySpecX509);
    }

    private boolean verifyServer() throws Exception {
        // send control message
        String controlMessage = controlMessageGenerator();
        log.info("CONTROL MESSAGE = " + controlMessage);
        String encodedControlMessage = Encryption.encryptHugeTextRSA(controlMessage, serverPublicKey);
        log.info("ENCODED CONTROL MESSAGE = " + encodedControlMessage);
        out.println(encodedControlMessage);

        // get control message hash from server
        String controlHashFromServer = in.readLine();
        log.info("CONTROL HASH = " + controlHashFromServer);

        // decode hash
        String decodedHashFromServer = Encryption.decryptHugeTextRSA(controlHashFromServer, clientPrivateKey);
        log.info("ENCODED CONTROL HASH = " + decodedHashFromServer);

        // hash control message in client
        String controlHashTest = Hashing
                .sha256()
                .hashString(controlMessage + clientPublicKeyString, StandardCharsets.UTF_8)
                .toString();
        log.info("CLIENT CODED HASH = " + controlHashTest);

        // check is hash the same
        if (controlHashTest.equals(decodedHashFromServer)) return true;
        return false;

    }


    public void connectToServer() throws Exception {

        generateClientKeys();
        establishServerConnection();

        serverPublicKey = getServerKey();
        if (!verifyServer()) {
            log.error("Server not verified");
            return;
        }
        this.aesKey = generateAesKey();
    }

    private Key generateAesKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key aesKey = keyGenerator.generateKey();
        String aesKeyString = Base64.getEncoder().encodeToString(aesKey.getEncoded());
        out.println(Encryption.encryptHugeTextRSA(aesKeyString, serverPublicKey));
        return aesKey;

    }

    private static String controlMessageGenerator() {
        byte[] array = new byte[controlMessageLength];
        new Random().nextBytes(array);
        return Base64.getEncoder().encodeToString(array);
    }

    public String encryptAES(String data) throws Exception {
        var encryptionCipher = Cipher.getInstance("AES");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] encryptedMessageBytes = encryptionCipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedMessageBytes);
    }

    public String decryptAES(String encryptedData) throws Exception {
        Cipher decryptionCipher = Cipher.getInstance("AES");
        decryptionCipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedMessageBytes = decryptionCipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedMessageBytes);
    }


}
