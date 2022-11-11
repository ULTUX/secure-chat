package org.example;

import lombok.SneakyThrows;
import org.example.json.request.LoginRequest;
import org.example.json.request.RegisterRequest;
import org.example.json.response.Response;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        SocketManager socketManager = new SocketManager();
        socketManager.connectToServer();
        var request = new RegisterRequest();
        request.setBody(new RegisterRequest.RegisterRequestBody("12334", "1234"));
        System.out.println(socketManager.sendRequest(request, Response.class));

        var login = new LoginRequest();
        login.setBody(new LoginRequest.LoginRequestBody("12334", "1234"));
        System.out.println(socketManager.sendRequest(login, Response.class));
    }
}
