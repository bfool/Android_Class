package com.sn.decks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.sn.decks.utils.StaticVaribles;
import com.sn.decks.utils.Tools;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText_login_user_name)
    EditText editText_username;

    @BindView(R.id.editText_login_password)
    EditText editText_pwd;

    @OnClick(R.id.button_login)
    void button_login() {
        if (Tools.isNetworkAvailable(this)) {
            login();
        } else {
            Tools.clearEditText(editText_username);
            Tools.clearEditText(editText_pwd);
        }
    }

    @OnClick(R.id.button_login_register)
    void register() {
        if(Tools.isNetworkAvailable(this)) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Tools.isNetworkAvailable(this)) {
            isLogin();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    public void login() {
        String name = editText_username.getText().toString().trim();
        String pwd= editText_pwd.getText().toString().trim();

        if(name.equals("")) {
            editText_username.setError("用户名不能为空");
        } else if(pwd.equals("")) {
            editText_pwd.setError("密码不能为空");
        } else {
            // 清除缓存用户对象
            AVUser.logOut();
            AVUser.logInInBackground(name, pwd, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if(avUser != null) {
                        Tools.showToast(getApplicationContext(), "登录成功");
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (e.getCode() == 210) {
                        Tools.showToast(getApplicationContext(), "密码错误");
                        Tools.clearEditText(editText_pwd);
                    } else if(e.getCode() == 211){
                        Tools.showToast(getApplicationContext(), "用户不存在");
                        Tools.clearEditText(editText_username);
                        Tools.clearEditText(editText_pwd);
                    }
                }
            });
//            User user = new UserDao().getPwdByName(name);
//            if(TextUtils.isEmpty(user.getPwd())) {
//                Tools.showToast(this, "该用户不存在");
//                return StaticVaribles.RETURN_FAIL;
//            }
//            if (user.getPwd().equals(pwd)) {
//                Log.i("LoginActivity", "success");
//                return StaticVaribles.RETURN_SUCCESS;
//            } else {
//                Log.i("LoginActivity", " pwd fail");
//                editText_pwd.setError("密码错误");
//                return StaticVaribles.RETURN_FAIL;
//            }
        }
    }

    public void isLogin() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            //缓存用户对象为空时，可打开用户注册界面…
        }
    }
}
