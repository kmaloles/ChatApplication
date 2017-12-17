package com.kmaloles.mymessagingapp.model;

import android.net.wifi.p2p.WifiP2pManager;

import java.io.Serializable;

/**
 * Created by kevinmaloles on 12/17/17.
 */

public class BannedWord implements Serializable {

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public BannedWord(String id, String value) {
        this.id = id;
        this.value = value;
    }

    String id;
    String value;

    public BannedWord(){}
}
