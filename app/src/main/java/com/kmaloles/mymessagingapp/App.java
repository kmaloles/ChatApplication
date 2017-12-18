package com.kmaloles.mymessagingapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by kevinmaloles on 12/18/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
