package org.example.android.amp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.example.android.amp.R;
import org.example.android.amp.data.MusicDataProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GenreListActivity extends BaseActivity {

    @BindView(R.id.rv_genre_name)
    RecyclerView mRecyclerView;

    GenreRecycleViewAdapter mRecycleViewAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_genre_list;
    }

    @Override
    protected void initView() {
        super.initView();

        setTitle("Genre List");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecycleViewAdapter = new GenreRecycleViewAdapter(this, new ArrayList<>());

        mRecyclerView.setAdapter(mRecycleViewAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .flatMap(integer -> MusicDataProvider.getGenreList())
                .map(genreList -> {
                    List<GenreDetailViewItem> viewItemList = new ArrayList<>();
                    for (String genre : genreList) {
                        GenreDetailViewItem viewItem = new GenreDetailViewItem();
                        viewItem.title = genre;
                        viewItem.detail = genre;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class GenreDetailViewItem {
        String title;

        String detail;
    }

    class GenreRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<GenreDetailViewItem> viewItemList;

        protected LayoutInflater mLayoutInflater;

        GenreRecycleViewAdapter(Context context, @NonNull List<GenreDetailViewItem> viewItemList) {
            this.viewItemList = viewItemList;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        public void setViewItemList(@NonNull List<GenreDetailViewItem> viewItemList) {
            this.viewItemList = viewItemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_detail_genre, viewGroup, false);

            return new GenreDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            GenreDetailViewHolder holder = (GenreDetailViewHolder) viewHolder;

            GenreDetailViewItem viewItem = viewItemList.get(i);

            holder.tvTitle.setText(viewItem.title);
            holder.tvDetail.setText(viewItem.detail);

            holder.itemView.setOnClickListener(v -> {
                Timber.e("Clicked");
            });
        }

        @Override
        public int getItemCount() {
            return viewItemList.size();
        }

    }

    class GenreDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_detail)
        TextView tvDetail;

        public GenreDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
