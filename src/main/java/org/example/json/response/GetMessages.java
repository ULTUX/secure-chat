package org.example.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.model.Message;

import java.util.List;

@ToString
@Getter
@Setter
public class GetMessages extends Response {

    private GetMessagesBody body;

    public record GetMessagesBody(List<Message> messages){}
}

