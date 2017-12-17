package com.kmaloles.mymessagingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kmaloles.mymessagingapp.model.RealmLocalBannedWords;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmLocalBannedWordsRealmProxy;
import io.realm.RealmResults;

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

    @Override
    public void saveBannedWord(Realm realm,String value, Context context) {
        if(!getBannedWords(context).contains(value)) {
            realm.beginTransaction();
            RealmLocalBannedWords word = realm.createObject(RealmLocalBannedWords.class);
            word.setValue(value);
            realm.commitTransaction();
        }
    }

    @Override
    public List<String> getBannedWords(Context context) {
        Realm.init(context);
        Realm r = Realm.getDefaultInstance();

        //this method returns an array of accountNames as String, not the entire Account object
        RealmResults<RealmLocalBannedWords> results = r.where(RealmLocalBannedWords.class).findAll();
        //fetch results into string array
        List<String> listOfBannedWords = new ArrayList<>(results.size());
        for(RealmLocalBannedWords value: results){
            listOfBannedWords.add(value.getValue());
        }
        return listOfBannedWords;
    }
}
