package org.example.json.response;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Conversations;

import java.util.List;

@Getter
@Setter
public class GetNewMessages extends Response {

    private GetNewMessagesBody body;
}

record GetNewMessagesBody(List<Conversations> conversations){}