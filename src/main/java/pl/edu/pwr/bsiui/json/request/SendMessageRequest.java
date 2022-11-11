package pl.edu.pwr.bsiui.json.request;

import lombok.*;
import pl.edu.pwr.bsiui.Requests;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SendMessageRequest extends Request {

    private String action = Requests.SEND_MESSAGE.toString();

    private SendMessageRequestBody body;

   public record SendMessageRequestBody(String conversation, String content) {
    }
}
