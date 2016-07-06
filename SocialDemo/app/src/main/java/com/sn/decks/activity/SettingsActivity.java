package com.sn.decks.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;
import com.sn.decks.utils.Tools;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SettingsActivity extends SwipeBackActivity{

    private SwipeBackLayout mSwipeBackLayout;
    private ImageView imageView;
    private Button button_send;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

		/*
		 * 实现SwipeBackLayout
		 */
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 设置可以滑动的区域，推荐用屏幕像素的一半来指定
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mSwipeBackLayout.setEdgeSize(dm.widthPixels / 3);

        imageView = (ImageView) findViewById(R.id.imageView_update_head);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        button_send = (Button) findViewById(R.id.button_update_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sendSetting()) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory
                    .decodeFile(picturePath));

        }
    }

    public boolean sendSetting() {
        if(imageView.getDrawable() != null) {
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();
            byte[] bytes = Tools.getBitmapByte(bitmap);

            AVUser avUser = AVUser.getCurrentUser();
            AVFile avFile = new AVFile("head", bytes);
            avFile.saveInBackground();
            avUser.put("userhead", avFile);
            avUser.saveInBackground();
            return true;
        } else {
            Tools.showToast(this, "当前没有选择图片");
            return false;
        }

    }


}
