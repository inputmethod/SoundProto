package com.typany.base.lifecycle;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * Created by liuyan on 17-11-22.
 */

public class DefaultViewModelFactory {
    private static ViewModelProvider.Factory sDefaultFactory;

    @MainThread
    public static ViewModelProvider.Factory get(@NonNull Application app) {
        if (sDefaultFactory == null)
            sDefaultFactory = new ViewModelProviders.DefaultFactory(app);
        return sDefaultFactory;
    }
}
