package org.example.android.amp.ui;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    protected void showToast(String message) {
        runOnUiThread(() -> {
            final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}
