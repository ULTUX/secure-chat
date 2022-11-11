package org.example.json.request;

import lombok.Getter;
import lombok.Setter;
import org.example.Requests;

@Getter
@Setter
public class LoginRequest extends Request {

    private String action = Requests.LOGIN.toString();
    private LoginRequestBody body;

    public record LoginRequestBody(String login, String password) {
    }
}