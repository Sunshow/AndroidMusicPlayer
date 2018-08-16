package org.example.android.amp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.example.android.amp.BuildConfig;
import org.example.android.amp.R;
import org.example.android.amp.data.MusicDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.rv_group_name)
    RecyclerView mRecyclerView;

    MainRecycleViewAdapter mRecycleViewAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected String getCustomTitle() {
        return getString(R.string.app_name);
    }

    @Override
    @SuppressLint("CheckResult")
    protected void initView() {
        super.initView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RxView.clicks(findViewById(R.id.fab))
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .flatMap(integer -> MusicDataProvider.getGenreList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(genre -> Timber.e("Music genre: %s", genre));

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
                */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        List<MainDetailViewItem> viewItemList = new ArrayList<>();
        {
            MainDetailViewItem viewItem = new MainDetailViewItem();
            viewItem.title = "Genres";
            viewItem.detail = "Songs by genre";
            viewItemList.add(viewItem);
        }

        {
            MainDetailViewItem viewItem = new MainDetailViewItem();
            viewItem.title = "Genres";
            viewItem.detail = "Songs by genre";
            viewItemList.add(viewItem);
        }

        mRecycleViewAdapter = new MainRecycleViewAdapter(this, viewItemList);

        mRecyclerView.setAdapter(mRecycleViewAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_me) {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.navigation_allmusic) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MainDetailViewItem {
        String title;

        String detail;
    }

    class MainRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<MainDetailViewItem> viewItemList;

        protected LayoutInflater mLayoutInflater;

        MainRecycleViewAdapter(Context context, @NonNull List<MainDetailViewItem> viewItemList) {
            this.viewItemList = viewItemList;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.item_detail_main, viewGroup, false);

            return new MainDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MainDetailViewHolder holder = (MainDetailViewHolder) viewHolder;

            MainDetailViewItem viewItem = viewItemList.get(i);

            holder.tvTitle.setText(viewItem.title);
            holder.tvDetail.setText(viewItem.detail);

            holder.itemView.setOnClickListener(v -> {
                Timber.e("Clicked");
                startActivity(new Intent(MainActivity.this, GenreListActivity.class));
            });
        }

        @Override
        public int getItemCount() {
            return viewItemList.size();
        }

    }

    class MainDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_detail)
        TextView tvDetail;

        public MainDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
