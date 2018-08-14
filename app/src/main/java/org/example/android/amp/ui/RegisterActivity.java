package org.example.android.amp.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.example.android.amp.R;
import org.example.android.amp.model.User;
import org.example.android.amp.util.db.UserDbUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_email)
    EditText etUsername;

    @BindView(R.id.register_password)
    EditText etPassword;

    @BindView(R.id.register_password_confirm)
    EditText etPasswordConfirm;

    @BindView(R.id.first_name)
    EditText etFirstName;

    @BindView(R.id.family_name)
    EditText etFamilyName;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.new_user_register, R.id.new_user_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_user_register:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                String firstName = etFirstName.getText().toString();
                String familyName = etFamilyName.getText().toString();

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
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                Observable.just(1)
                        .observeOn(Schedulers.io())
                        .flatMap(integer -> UserDbUtils.findByUsername(username))
                        .defaultIfEmpty(new User())
                        .flatMap(user -> {
                            if (user.getUsername() == null) {
                                // 说明用户不存在可以注册
                                User newUser = new User();
                                newUser.setUsername(username);
                                newUser.setPassword(password);
                                newUser.setFirstName(firstName);
                                newUser.setLastName(familyName);

                                return UserDbUtils.register(newUser);
                            } else {
                                return Observable.just(new User());
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            if (user.getUsername() != null) {
                                showToast("注册成功");
                                finish();
                            } else {
                                showToast("用户名已被使用");
                            }
                        });

                break;
            case R.id.new_user_back:
                finish();
                break;
        }
    }
}
