package com.kmaloles.mymessagingapp.model;

import com.kmaloles.mymessagingapp.data.DefaultDataManager;

import java.io.Serializable;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public class User implements Serializable {
    String username;
    String email;
    String userType;

    public User(){}

    public User(String username, String email, String userType) {
        this.username = username;
        this.email = email;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
