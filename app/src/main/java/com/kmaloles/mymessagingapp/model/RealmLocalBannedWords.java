package com.kmaloles.mymessagingapp.model;

import io.realm.RealmObject;

/**
 * Created by kevinmaloles on 12/17/17.
 */

public class RealmLocalBannedWords extends RealmObject {

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
