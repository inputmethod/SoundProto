package com.typany.soundproto;

import android.app.Application;

import com.typany.ime.IMEApplicationContext;

/**
 * Created by yangfeng on 2017/12/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IMEApplicationContext.application = this;
        IMEApplicationContext.context = getBaseContext();
    }
}
