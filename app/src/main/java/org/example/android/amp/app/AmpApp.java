package org.example.android.amp.app;

import android.app.Application;

import timber.log.Timber;

public class AmpApp extends Application {

    private static AmpApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Timber.plant(new Timber.DebugTree());
    }

    public static AmpApp getInstance() {
        return instance;
    }

}
