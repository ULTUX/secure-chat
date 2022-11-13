package pl.edu.pwr.bsiui.socket.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {

    String author;
    String dateTime;
    String content;
}
