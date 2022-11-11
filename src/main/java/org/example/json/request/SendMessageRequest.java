package org.example.json.request;

import lombok.*;
import org.example.Requests;

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
