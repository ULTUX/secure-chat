package pl.edu.pwr.bsiui.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {

    String author;
    String dateTime;
    String content;
}
