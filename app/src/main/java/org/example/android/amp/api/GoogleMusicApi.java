package org.example.android.amp.api;

import org.example.android.amp.model.MusicListResult;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GoogleMusicApi {

    @GET("automotive-media/music.json")
    Call<MusicListResult> getMusicListResult();

}
