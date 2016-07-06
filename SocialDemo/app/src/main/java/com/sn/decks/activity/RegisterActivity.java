package com.sn.decks.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.sn.decks.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class RegisterActivity extends SwipeBackActivity {

    @BindView(R.id.editText_register_username)
    EditText editText_username;

    @BindView(R.id.editText_register_pwd)
    EditText editText_pwd;

    @BindView(R.id.editText_register_repwd)
    EditText editText_repwd;

    @OnClick(R.id.button_register)
    void button_register() {
       register();
    }

    private SwipeBackLayout mSwipeBackLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        /*
		 * 实现SwipeBackLayout
		 */
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 设置可以滑动的区域，推荐用屏幕像素的一半来指定
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSwipeBackLayout.setEdgeSize(dm.widthPixels / 3);
    }

    public void register() {
        String name = editText_username.getText().toString().trim();
        String pwd = editText_pwd.getText().toString().trim();
        String repwd = editText_repwd.getText().toString().trim();

        if(name.equals("") || pwd.equals("") || repwd.equals("")) {
            Tools.showToast(this, "不能为空");
        } else {

            if(pwd.equals(repwd)) {
//                new UserDao().save(name, pwd);
                AVUser user = new AVUser();// 新建 AVUser 对象实例
                user.setUsername(name);// 设置用户名
                user.setPassword(pwd);// 设置密码
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null) {
                            Tools.showToast(getApplicationContext(), "注册成功");
                            finish();

                        } else if (e.getCode() == 202) {
                            Tools.showToast(getApplicationContext(), "用户已存在");
                            Tools.clearEditText(editText_username);
                            Tools.clearEditText(editText_pwd);
                            Tools.clearEditText(editText_repwd);
                        }
                    }
                });
            }

        }
    }
}
