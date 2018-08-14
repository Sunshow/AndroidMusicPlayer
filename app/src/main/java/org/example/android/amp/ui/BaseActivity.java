package org.example.android.amp.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.example.android.amp.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        ButterKnife.bind(this);

        initView();
    }

    @LayoutRes
    abstract protected int getLayoutRes();

    protected void initView() {

    }

    protected void showToast(String message) {
        runOnUiThread(() -> {
            final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}
