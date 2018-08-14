package org.example.android.amp.data;

import org.example.android.amp.app.AmpApp;
import org.example.android.amp.model.Music;
import org.example.android.amp.model.MusicListResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class MusicDataProvider {

    private static Map<String, List<Music>> genreListMapCache;

    private static Map<String, List<Music>> groupMusicList(MusicListResult result) {
        Map<String, List<Music>> genreListMap = new HashMap<>();

        for (Music music : result.getMusic()) {
            String genre = music.getGenre();

            if (!genreListMap.containsKey(genre)) {
                genreListMap.put(genre, new ArrayList<>());
            }

            genreListMap.get(genre).add(music);
        }

        return genreListMap;
    }

    private static Observable<Map<String, List<Music>>> getGenreListMapInMemory() {
        if (genreListMapCache == null) {
            return Observable.empty();
        }
        return Observable.just(genreListMapCache);
    }

    private static Observable<Map<String, List<Music>>> getGenreListMapInDisk() {
        return Observable.empty();
    }

    private static Observable<Map<String, List<Music>>> getGenreListMapInRemote() {
        return AmpApp.getMusicApi().getMusicListResult()
                .map(MusicDataProvider::groupMusicList)
                .map(genreListMap -> genreListMapCache = genreListMap);
    }

    private static Observable<Map<String, List<Music>>> getGenreListMap() {
        return Observable.concat(getGenreListMapInMemory(), getGenreListMapInDisk(), getGenreListMapInRemote())
                .firstElement().toObservable();
    }

    public static Observable<List<String>> getGenreList() {
        return getGenreListMap()
                .map(genreListMap -> new ArrayList<>(genreListMap.keySet()));
    }

    public static Observable<List<Music>> getMusicListByGenre(String genre) {
        return getGenreListMap()
                .map(genreListMap -> genreListMap.get(genre));
    }
}
