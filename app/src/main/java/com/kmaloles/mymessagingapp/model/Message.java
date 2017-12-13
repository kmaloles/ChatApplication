package com.kmaloles.mymessagingapp.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by kevinmaloles on 12/13/17.
 */

public class Message implements IMessage, Serializable {

    String id;
    String text;
    Author author;
    Date createdAt;

    public Message(){}

    public Message(String id, String text, Author author, Date createdAt) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }


}
