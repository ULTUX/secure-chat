package org.example.json.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.Requests;

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