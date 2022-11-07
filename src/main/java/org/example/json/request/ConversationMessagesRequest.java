package org.example.json.request;

import lombok.Getter;
import lombok.Setter;
import org.example.Requests;

@Getter
@Setter
public class ConversationMessagesRequest extends Request {
    private String action = Requests.GET_MESSAGES_FOR_CONVERSATION.toString();
    private ConversationMessagesRequestBody body;
}

record ConversationMessagesRequestBody(String conversation){}
