package org.example.android.amp.api;

import org.example.android.amp.model.MusicListResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GoogleMusicRxApi {

    @GET("music.json")
    Observable<MusicListResult> getMusicListResult();

}
