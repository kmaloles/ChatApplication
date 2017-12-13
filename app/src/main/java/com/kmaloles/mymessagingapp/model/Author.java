package com.kmaloles.mymessagingapp.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by kevinmaloles on 12/13/17.
 */

public class Author implements IUser {

    String id;
    String name;
    String avatar;
    String email;
    String userType;

    public Author(String id, String name, String avatar, String email, String userType) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.email = email;
        this.userType = userType;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
