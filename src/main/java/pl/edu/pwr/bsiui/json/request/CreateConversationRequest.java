package pl.edu.pwr.bsiui.json.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pwr.bsiui.Requests;

import java.util.List;

@ToString
@Getter
@Setter
public class CreateConversationRequest extends Request {
    private String action = Requests.CREATE_CONVERSATION.toString();
    private CreateConversationRequestBody body;

    public record CreateConversationRequestBody(String name, List<String> users) {
    }
}