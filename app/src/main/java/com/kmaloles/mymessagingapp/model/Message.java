package com.kmaloles.mymessagingapp.model;

import java.io.Serializable;

/**
 * Created by kevinmaloles on 12/11/17.
 */

public class Message implements Serializable {

    String id;
    String body;
    String sender;
    String recipient;
    String created;

    public Message(){}

    public Message(String id, String body, String sender, String created, String recipient) {
        this.id = id;
        this.body = body;
        this.sender = sender;
        this.created = created;
        this.recipient = recipient;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getRecipient() {
        return recipient;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", body='" + body + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
