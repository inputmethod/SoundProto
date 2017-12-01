package com.typany.soundproto;

import android.app.Application;

import com.typany.base.IMEThread;
import com.typany.ime.IMEApplicationContext;
import com.typany.sound.service.SoundStorage;

/**
 * Created by yangfeng on 2017/12/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        IMEApplicationContext.application = this;
        IMEApplicationContext.context = getBaseContext();

        SoundStorage.init(getBaseContext());
        IMEThread.initialize();
    }
}
