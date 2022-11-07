package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.json.request.Request;
import org.example.json.response.Response;

import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketManager {
    private static final String HOSTNAME = "";
    private static final int PORT = 1234;
    private final Socket socket;
    private final ObjectMapper mapper;
    private final Encryptor encryptor;

    public SocketManager() throws IOException {
        this.encryptor = new Encryptor();
        this.socket = new Socket(HOSTNAME, PORT);
        this.mapper = new ObjectMapper();
    }

    public <T extends Response, N extends Request>T sendRequest(N request, Class<T> classType) throws IOException {
        mapper.writeValue(socket.getOutputStream(), request);
        return mapper.readValue(socket.getInputStream(), classType);
    }

}
