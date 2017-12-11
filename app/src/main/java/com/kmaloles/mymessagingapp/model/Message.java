package com.kmaloles.mymessagingapp.model;

/**
 * Created by kevinmaloles on 12/11/17.
 */

public class Message {

    String body;
    String sender;
    String created;

    public Message(String body, String sender, String created) {
        this.body = body;
        this.sender = sender;
        this.created = created;
    }
}
