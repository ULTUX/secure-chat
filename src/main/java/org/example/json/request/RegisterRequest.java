package org.example.json.request;

import lombok.Getter;
import lombok.Setter;
import org.example.Requests;

@Setter
@Getter
public class RegisterRequest extends Request {

    private String action = Requests.REGISTER.toString();

    private RegisterRequestBody body;

    public record RegisterRequestBody(String login, String password) {
    }
}
