package pl.edu.pwr.bsiui.socket.json.request;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pwr.bsiui.socket.Requests;

@Setter
@Getter
public class RegisterRequest extends Request {

    private String action = Requests.REGISTER.toString();

    private RegisterRequestBody body;

    public record RegisterRequestBody(String login, String password) {
    }
}
