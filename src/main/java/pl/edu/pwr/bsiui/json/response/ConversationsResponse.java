package pl.edu.pwr.bsiui.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pwr.bsiui.model.Conversation;

import java.util.List;

@ToString
@Getter
@Setter
public class ConversationsResponse extends Response {

    ConversationResponseBody body;

    public record ConversationResponseBody(List<Conversation> conversations){}
}

