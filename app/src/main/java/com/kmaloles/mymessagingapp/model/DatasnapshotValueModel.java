package com.kmaloles.mymessagingapp.model;

import java.io.Serializable;

/**
 * Created by kevinmaloles on 12/13/17.
 */

public class DatasnapshotValueModel implements Serializable {

    String key;
    User user;

    public DatasnapshotValueModel(){}

    public DatasnapshotValueModel(String key, User user) {
        this.key = key;
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
