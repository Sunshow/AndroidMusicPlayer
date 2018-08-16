package org.example.android.amp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.example.android.amp.R;
import org.example.android.amp.constant.GlobalConstants;
import org.example.android.amp.data.MusicDataProvider;
import org.example.android.amp.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MusicListActivity extends BaseActivity {

    public static final String ARG_GENRE = "genre";

    @BindView(R.id.rv_music_list)
    RecyclerView mRecyclerView;

    MusicRecycleViewAdapter mRecycleViewAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_music_list;
    }

    @Override
    protected String getCustomTitle() {
        return "Music List";
    }

    @Override
    protected void initView() {
        super.initView();

        mRecycleViewAdapter = new MusicRecycleViewAdapter(this, new ArrayList<>());

        mRecyclerView.setAdapter(mRecycleViewAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String genre = intent.getStringExtra(ARG_GENRE);

        if (genre != null) {
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .flatMap(integer -> MusicDataProvider.getMusicListByGenre(genre))
                    .map(musicList -> {
                        List<MusicDetailViewItem> viewItemList = new ArrayList<>();
                        for (Music music : musicList) {
                            MusicDetailViewItem viewItem = new MusicDetailViewItem();
                            viewItem.title = music.getTitle();
                            viewItem.artist = music.getArtist();
                            viewItem.musicUrl = GlobalConstants.MUSIC_LIST_API + music.getSource();
                            viewItemList.add(viewItem);
                        }
                        return viewItemList;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viewItemList -> {
                        mRecycleViewAdapter.setViewItemList(viewItemList);
                        mRecycleViewAdapter.notifyDataSetChanged();
                    });
        }
    }

    class MusicDetailViewItem {
        String title;

        String artist;

        String musicUrl;

        boolean isPlaying;
    }

    class MusicRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<MusicDetailViewItem> viewItemList;

        protected LayoutInflater mLayoutInflater;

        private MediaPlayer mediaPlayer;

        private MusicDetailViewItem playingViewItem;

        MusicRecycleViewAdapter(Context context, @NonNull List<MusicDetailViewItem> viewItemList) {
            this.viewItemList = viewItemList;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        public void setViewItemList(@NonNull List<MusicDetailViewItem> viewItemList) {
            this.viewItemList = viewItemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_detail_music, viewGroup, false);

            return new MusicDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MusicDetailViewHolder holder = (MusicDetailViewHolder) viewHolder;

            MusicDetailViewItem viewItem = viewItemList.get(i);

            holder.tvTitle.setText(viewItem.title);
            holder.tvArtist.setText(viewItem.artist);

            holder.itemView.setOnClickListener(v -> {
                Timber.e("Clicked, play music: %s, isPlaying: %s", viewItem.musicUrl, viewItem.isPlaying);
                if (!viewItem.isPlaying) {
                    viewItem.isPlaying = true;
                    playingViewItem = viewItem;
                    // play music
                    try {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            playingViewItem.isPlaying = false;
                        }
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(viewItem.musicUrl);
                        //mediaPlayer.prepare();
                        //mediaPlayer.start();
                        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        Timber.e(e, "Play music error, url=%s", viewItem.musicUrl);
                    }
                } else {
                    // stop playing
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    viewItem.isPlaying = false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return viewItemList.size();
        }

    }

    class MusicDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_artist)
        TextView tvArtist;

        public MusicDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
