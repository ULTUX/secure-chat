package org.example.json.response;

import org.example.model.Conversations;

import java.util.List;

public class ConversationsResponse extends Response {
    ConversationResponseBody body;
}

record ConversationResponseBody(List<Conversations> conversations){}