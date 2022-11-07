package org.example;

public enum Requests {
    REGISTER("register"),
    LOGIN("login"),
    SEND_MESSAGE("message"),
    GET_USER_LIST("listUsers"),
    GET_CONVERSATION_LIST("listConversations"),
    CREATE_CONVERSATION("createConversations"),
    GET_UNREAD("getMessages"),
    GET_MESSAGES_FOR_CONVERSATION("getConversationMessages");

    private final String name;
    Requests(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
