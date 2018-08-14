package org.example.android.amp.app;

import android.app.Application;

import org.example.android.amp.api.GoogleMusicRxApi;
import org.example.android.amp.constant.GlobalConstants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class AmpApp extends Application {

    private static AmpApp instance;

    private static GoogleMusicRxApi musicApi;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Timber.plant(new Timber.DebugTree());

        initMusicApi();
    }

    private void initMusicApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.MUSIC_LIST_API)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        musicApi = retrofit.create(GoogleMusicRxApi.class);
    }

    public static AmpApp getInstance() {
        return instance;
    }

    public static GoogleMusicRxApi getMusicApi() {
        return musicApi;
    }

}
