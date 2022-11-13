package pl.edu.pwr.bsiui;

import lombok.SneakyThrows;
import pl.edu.pwr.bsiui.socket.json.request.LoginRequest;
import pl.edu.pwr.bsiui.socket.json.request.RegisterRequest;
import pl.edu.pwr.bsiui.socket.json.response.Response;
import pl.edu.pwr.bsiui.socket.SocketManager;

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
