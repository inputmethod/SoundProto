package com.typany.lifemanagement;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ProcessLifecycleOwner;

/**
 * Created by dingbei on 11/19/2017.
 */

public class AppLifetime implements LifecycleOwner {
    @Override
    public Lifecycle getLifecycle() {
        return ProcessLifecycleOwner.get().getLifecycle();
    }
}
