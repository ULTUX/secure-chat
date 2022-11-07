package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

import static org.example.Encryption.decryptHugeText;
import static org.example.Encryption.encryptHugeText;

public class Trash {
    public static final String connectionMessage = "Hi!";

    public static void main(String[] args) throws Exception {

        // generate client keys
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey clientPublicKey = kp.getPublic();
        PrivateKey clientPrivateKey = kp.getPrivate();
        String clientPublicKeyString = Base64.getEncoder().encodeToString(clientPublicKey.getEncoded());

        // connect to server
        Socket clientSocket = new Socket("127.0.0.1", 16123);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.println(connectionMessage);
        int currentPort = Integer.parseInt(in.readLine());
        System.out.println("port =" + currentPort);
        clientSocket.close();

        // connection on new port
        Socket connectedSocket = new Socket("127.0.0.1", currentPort);
        out = new PrintWriter(connectedSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(connectedSocket.getInputStream()));

        // send user public key
        out.println(clientPublicKeyString);

        // get encoded server key
        String serverPublicKeyMessage = in.readLine();
        String decodedServerKey = decryptHugeText(serverPublicKeyMessage, clientPrivateKey);

        // get server public key as object
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(decodedServerKey));
        PublicKey serverPublicKey = keyFactory.generatePublic(keySpecX509);

        // send control message
        String controlMessage = controlMessageGenerator(100);
        String encodedControlMessage = encryptHugeText(controlMessage, serverPublicKey);
        out.println(encodedControlMessage);

        // get control message hash from server
        String controlHashFromServer = in.readLine();

        // decode hash
        String decodedHashFromServer = decryptHugeText(controlHashFromServer, clientPrivateKey);

        // hash control message in client
        String controlHashTest = Hashing
                .sha256()
                .hashString(controlMessage + clientPublicKeyString, StandardCharsets.UTF_8)
                .toString();

        // check is hash the same
        if (controlHashTest.equals(decodedHashFromServer)) {
            out.println("{\"action\": \"login\", \"body\":{\"login\":\"login2\",\"password\":\"haslo2\"}}");
            String status = in.readLine();

            // create conversation
            File file = new File("create_conversation_json.txt");
            CharSource source = Files.asCharSource(file, Charsets.UTF_8);
            String createConversationMessage = source.read();
            System.out.println(createConversationMessage);

            out.println(createConversationMessage);

            File file2 = new File("message_json.txt");
            CharSource source2 = Files.asCharSource(file2, Charsets.UTF_8);
            String messageJson = source2.read();

            File file3 = new File("user_list_json.txt");
            CharSource source3 = Files.asCharSource(file3, Charsets.UTF_8);
            String messageJson3 = source3.read();

            out.println(messageJson3);
            String listUsers = in.readLine();
            System.out.println(listUsers);

            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                String currentMessage = messageJson
                        .replace("[1]", "testConversation")
                        .replace("[2]", "test test test");
                out.println(currentMessage);
                String response = in.readLine();
            }

            File file4 = new File("get_messages.txt");
            CharSource source4 = Files.asCharSource(file4, Charsets.UTF_8);
            String messageJson4 = source4.read();

            File file5 = new File("get_all_conversations.txt");
            CharSource source5 = Files.asCharSource(file5, Charsets.UTF_8);
            String messageJson5 = source5.read();

            out.println(messageJson5);
            String messagesList = in.readLine();
            System.out.println(messagesList);


            while (true) {
                Thread.sleep(5000);
                out.println(messageJson4);
                String listMessages = in.readLine();
                System.out.println(listMessages);
            }

        }
    }

    private static String controlMessageGenerator(int length) {
        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return Base64.getEncoder().encodeToString(array);
    }
}
