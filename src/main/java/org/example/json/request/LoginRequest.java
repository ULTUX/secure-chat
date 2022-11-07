package org.example.json.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest extends Request {

    private String action ="login";
    private RegisterRequestBody body;
}
record LoginRequestBody(String login, String password){}
