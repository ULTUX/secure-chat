package org.example.json.request;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Users;

import java.util.List;

@Getter
@Setter
public class CreateConversationRequest extends Request {
    private String action ="createConversation";
    private CreateConversationRequestBody body;
}

record CreateConversationRequestBody(String name, List<Users>users){}