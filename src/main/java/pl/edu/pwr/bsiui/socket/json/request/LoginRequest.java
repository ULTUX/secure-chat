package pl.edu.pwr.bsiui.socket.json.request;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.bsiui.socket.Requests;

@Getter
@Setter
public class LoginRequest extends Request {

    private String action = Requests.LOGIN.toString();
    private LoginRequestBody body;

    public record LoginRequestBody(String login, String password) {
    }
}