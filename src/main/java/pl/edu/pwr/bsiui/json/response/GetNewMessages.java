package pl.edu.pwr.bsiui.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pwr.bsiui.model.Conversation;

import java.util.List;

@ToString
@Getter
@Setter
public class GetNewMessages extends Response {

    private GetNewMessagesBody body;
    public record GetNewMessagesBody(List<Conversation> conversations){}
}

