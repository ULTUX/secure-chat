package pl.edu.pwr.bsiui.socket.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.pwr.bsiui.socket.model.User;

import java.util.List;

@ToString
@Getter
@Setter
public class GetUsersResponse extends Response {

    GetUsersBody body;
    public record GetUsersBody(List<User> users){}
}

