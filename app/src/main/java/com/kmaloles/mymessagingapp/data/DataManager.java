package com.kmaloles.mymessagingapp.data;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public interface DataManager {

    void persistUserLogin(String user);
    String getUserLogin();
    void setUserType(String userType);
    String getUserType();
}
