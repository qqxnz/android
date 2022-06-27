package com.qqxnz.webscokettest;

public interface NetStateChangeObserver {

    void onDisconnect();

    void onMobileConnect();

    void onWifiConnect();
}
