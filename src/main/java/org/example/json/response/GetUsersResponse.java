package org.example.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.model.User;

import java.util.List;

@ToString
@Getter
@Setter
public class GetUsersResponse extends Response {

    GetUsersBody body;
    public record GetUsersBody(List<User> users){}
}

