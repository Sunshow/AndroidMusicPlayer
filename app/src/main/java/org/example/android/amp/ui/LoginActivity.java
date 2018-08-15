package org.example.android.amp.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.example.android.amp.R;
import org.example.android.amp.model.User;
import org.example.android.amp.util.db.UserDbUtils;
import org.example.android.amp.util.pref.PrefUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user_index;
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Timber.e("Login button clicked");

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // 数据验证
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 密码验证
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                Observable.just(1)
                        .subscribeOn(Schedulers.io())
                        .flatMap(integer -> UserDbUtils.findByUsername(username))
                        .defaultIfEmpty(new User())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            if (user.getUsername() != null) {
                                // 用户存在判断登录
                                if (user.getPassword().equals(password)) {
                                    showToast("登录成功");
                                    PrefUtils.setUserLogin(true);
                                    finish();
                                } else {
                                    showToast("密码错误");
                                }
                            } else {
                                showToast("用户名不存在");
                            }
                        });

                break;
            case R.id.btn_register:
                Timber.e("Register button clicked");
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUsername.setText("Hello");
    }
}
