package com.sn.decks.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.sn.decks.utils.Tools;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SharingActivity extends SwipeBackActivity{

    private final static int ADDSHARING_SUCCESS = 1;

    private SwipeBackLayout mSwipeBackLayout;
    private EditText editText_addSharing_context;

    Handler myhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ADDSHARING_SUCCESS:
                    SharingActivity.this.finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        /*
		 * 实现SwipeBackLayout
		 */
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 设置可以滑动的区域，推荐用屏幕像素的一半来指定
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSwipeBackLayout.setEdgeSize(dm.widthPixels / 3);

        Button button_send = (Button) findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_addSharing_context.getText().toString().equals("")){
                    Tools.showToast(getApplicationContext(), "说点什么吧!");
                } else {
                    send();
                }
            }
        });

        editText_addSharing_context = (EditText) findViewById(R.id.editText_addSharing_context);

    }

    public void send() {
        AVObject object = new AVObject("SharingWords");
        object.put("context", editText_addSharing_context.getText().toString());
        object.put("uploadname",  AVUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Tools.showToast(getApplicationContext(), "发送成功");
                Message msg = new Message();
                msg.what = SharingActivity.ADDSHARING_SUCCESS;
                SharingActivity.this.myhandler.sendMessage(msg);
            }
        });
    }
}
