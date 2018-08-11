package org.example.android.amp.app;

import android.app.Application;

import timber.log.Timber;

public class AmpApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }

}
