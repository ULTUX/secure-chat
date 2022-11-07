package org.example.json.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest extends Request {


    private SendMessageRequestBody body;
}

record SendMessageRequestBody(String conversation, String content) {
}
