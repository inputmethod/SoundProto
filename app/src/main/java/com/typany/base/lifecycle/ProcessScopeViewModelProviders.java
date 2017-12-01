package com.typany.base.lifecycle;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * Created by liuyan on 17-11-17.
 */

public class ProcessScopeViewModelProviders {
    private static ViewModelStore sStore;

    private static ViewModelStore getViewModelStore() {
        if (sStore == null)
            sStore = new ViewModelStore();
        return sStore;
    }

    @MainThread
    public static ViewModelProvider of(@NonNull Application app) {
        return of(DefaultViewModelFactory.get(app));
    }

    @MainThread
    public static ViewModelProvider of(@NonNull ViewModelProvider.Factory factory) {
        return new ViewModelProvider(getViewModelStore(), factory);
    }
}
