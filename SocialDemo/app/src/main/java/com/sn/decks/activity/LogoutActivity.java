package com.sn.decks.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.sn.decks.utils.Tools;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class LogoutActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout = null;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

           /*
		 * 实现SwipeBackLayout
		 */
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 设置可以滑动的区域，推荐用屏幕像素的一半来指定
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSwipeBackLayout.setEdgeSize(dm.widthPixels / 3);

        buttonLogout = (Button) findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                AVUser avUser = AVUser.getCurrentUser();
                if(avUser == null) {
                    Tools.showToast(getApplicationContext(), "用户已登出");

                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
