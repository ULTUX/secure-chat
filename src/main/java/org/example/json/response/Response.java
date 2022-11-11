package org.example.json.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Response {

    Integer status;
    String response;

}