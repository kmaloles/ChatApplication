package com.kmaloles.mymessagingapp.data;

import android.content.Context;

import java.util.List;

import io.realm.Realm;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public interface DataManager {

    void persistUserLogin(String user);
    String getUserLogin();
    void setUserType(String userType);
    String getUserType();
    void persistBannedWords(String value);
    List<String> getBannedWords();
}
