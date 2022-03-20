package com.kaya.basicmodeluione;

import android.app.Application;

public class ModelUIApp extends Application {
    private static ModelUIApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ModelUIApp getsInstance() {
        return sInstance;
    }
}
