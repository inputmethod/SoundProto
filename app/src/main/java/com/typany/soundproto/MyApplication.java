package com.typany.soundproto;

import android.app.Application;

import com.typany.base.IMEThread;
import com.typany.ime.IMEApplicationContext;
import com.typany.skin2.storage.SkinStorage;
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

        SkinStorage.init(getBaseContext(), "1080");
        SoundStorage.init(getBaseContext());
        IMEThread.initialize();
    }
}
