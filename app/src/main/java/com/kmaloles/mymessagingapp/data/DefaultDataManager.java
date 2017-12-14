package com.kmaloles.mymessagingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public class DefaultDataManager implements DataManager {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_TYPE = "type";
    public static final String USER_TYPE_ADMIN = "Admin";
    public static final String USER_TYPE_COMMON = "Common";

    public SharedPreferences mPreferences;

    public DefaultDataManager(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void persistUserLogin(String user) {
        mPreferences.edit().putString(KEY_USERNAME, user).apply();
    }

    @Override
    public String getUserLogin() {
        return mPreferences.getString(KEY_USERNAME, null);
    }

    @Override
    public void setUserType(String userType) {
        if (userType.equals(USER_TYPE_ADMIN)){
            mPreferences.edit().putString(KEY_USER_TYPE, USER_TYPE_ADMIN).apply();
        }else{
            mPreferences.edit().putString(KEY_USER_TYPE, USER_TYPE_COMMON).apply();
        }

    }
    @Override
    public String getUserType() {
        return mPreferences.getString(KEY_USER_TYPE, null);
    }
}
