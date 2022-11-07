package org.example.json.request;

public class RegisterRequest extends Request {

   private String action ="register";
   private RegisterRequestBody body;
}
record RegisterRequestBody(String login, String password){}
