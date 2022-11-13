package pl.edu.pwr.bsiui.socket.json.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pwr.bsiui.socket.Requests;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationMessagesRequest extends Request {
    private String action = Requests.GET_MESSAGES_FOR_CONVERSATION.toString();
    private ConversationMessagesRequestBody body;

    public record ConversationMessagesRequestBody(String conversation){}
}