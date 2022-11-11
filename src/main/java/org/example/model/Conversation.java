package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Conversation {

    String name;
    List<User> names;
    List<Message> messages;

    @Override
    public String toString() {
        return name;
    }
}
