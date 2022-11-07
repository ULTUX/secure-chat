package org.example.json.response;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Message;

import java.util.List;

@Getter
@Setter
public class GetMessages extends Response {
    private GetMessagesBody body;

}
record GetMessagesBody(List<Message> messageList){}
