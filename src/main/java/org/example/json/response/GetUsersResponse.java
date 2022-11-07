package org.example.json.response;

import org.example.model.Users;

import java.util.List;

public class GetUsersResponse extends Response {

    GetUsersBody body;
}
record GetUsersBody(List<Users> users){}
