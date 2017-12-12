package com.kmaloles.mymessagingapp.model;

import java.io.Serializable;

/**
 * Created by kevinmaloles on 12/11/17.
 */

public class Message implements Serializable {

    String id;
    String body;
    String sender;
    String created;

    public Message(){}

    public Message(String id, String body, String sender, String created) {
        this.id = id;
        this.body = body;
        this.sender = sender;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public String getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", sender='" + sender + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
