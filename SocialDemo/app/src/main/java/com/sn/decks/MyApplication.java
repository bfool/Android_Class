package com.sn.decks;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

import org.litepal.LitePalApplication;


public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"Ooy5Y244Dy6rJOfCYfjOYtIB-gzGzoHsz","PIsvoT2R0Mbcu48N6I2imuyA");
    }
}
