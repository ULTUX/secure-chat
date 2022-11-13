package pl.edu.pwr.bsiui.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.edu.pwr.bsiui.socket.json.request.Request;
import pl.edu.pwr.bsiui.socket.json.response.Response;
import pl.edu.pwr.bsiui.security.AES;
import pl.edu.pwr.bsiui.security.Decryptor;
import pl.edu.pwr.bsiui.security.Encryptor;
import pl.edu.pwr.bsiui.security.SecurityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;


@Getter
@Setter
@Slf4j
public class SocketManager {
    public static final String HANDSHAKE_MESSAGE = "Hi!";
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 16123;
    private static Random random = new Random();
    private Socket socket;
    private final ObjectMapper mapper;
    private String clientPublicKeyString;
    private PrintWriter out;
    private BufferedReader in;
    private Encryptor encryptor;
    private Decryptor decryptor;
    private AES aes;


    public SocketManager() throws IOException {
        this.mapper = new ObjectMapper();
    }

    public <T extends Response, N extends Request> T sendRequest(N request, Class<T> classType) throws Exception {
        String requestJson = mapper.writeValueAsString(request);
        String encodedMsg = aes.encryptAES(requestJson);
        out.println(encodedMsg);
        String encryptedResponse = in.readLine();
        String response = aes.decryptAES(encryptedResponse);
        return mapper.readValue(response, classType);
    }

    private void establishServerConnection() throws IOException {
        Socket clientSocket = new Socket(HOST, PORT);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(HANDSHAKE_MESSAGE);
        int currentPort = Integer.parseInt(in.readLine());
        clientSocket.close();

        Socket connectedSocket = new Socket(HOST, currentPort);
        out = new PrintWriter(connectedSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));
    }

    private PublicKey getServerKey() throws Exception {
        out.println(clientPublicKeyString);
        String serverPublicKeyMessage = in.readLine();
        String decodedServerKey = decryptor.decrypt(serverPublicKeyMessage);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(decodedServerKey));

        return keyFactory.generatePublic(keySpecX509);
    }

    private boolean verifyServer() throws Exception {
        // send control message
        String controlMessage = controlMessageGenerator();
        String encodedControlMessage = encryptor.encrypt(controlMessage);
        out.println(encodedControlMessage);

        // get control message hash from server
        String controlHashFromServer = in.readLine();

        // decode hash
        String decodedHashFromServer = decryptor.decrypt(controlHashFromServer);

        // hash control message in client
        String controlHashTest = Hashing
                .sha256()
                .hashString(controlMessage + clientPublicKeyString, StandardCharsets.UTF_8)
                .toString();

        // check is hash the same
        if (controlHashTest.equals(decodedHashFromServer)) return true;
        return false;

    }


    public void connectToServer() throws Exception {

        var clientKeyPair = SecurityUtils.generateRSAKeyPair();
        clientPublicKeyString = Base64.getEncoder().encodeToString(clientKeyPair.getPublic().getEncoded());

        this.decryptor = new Decryptor(clientKeyPair.getPrivate());
        establishServerConnection();

        var serverPublicKey = getServerKey();
        this.encryptor = new Encryptor(serverPublicKey);
        if (!verifyServer()) {
            log.error("Invalid server found.");
            return;
        }
        var aesKey = AES.generateAesKey();
        String aesKeyString = Base64.getEncoder().encodeToString(aesKey.getEncoded());
        out.println(encryptor.encrypt(aesKeyString));
        this.aes = new AES(aesKey);
    }


    private static String controlMessageGenerator() {
        byte[] array = new byte[100];
        random.nextBytes(array);
        return Base64.getEncoder().encodeToString(array);
    }

}
