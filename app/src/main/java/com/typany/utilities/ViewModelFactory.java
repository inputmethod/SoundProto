package com.typany.utilities;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

import com.typany.base.lifecycle.ProcessScopeViewModelProviders;

/**
 * Created by yangfeng on 2017/12/14.
 */

public class ViewModelFactory {
    private ViewModelFactory() {
        // no instance for helper class
    }

    public static <T extends ViewModel> T from(Application application, Class<T> cls) throws IllegalArgumentException {
        if (null == application) {
            throw new IllegalArgumentException("Illegal application instance");
        }

        return ProcessScopeViewModelProviders.of(application).get(cls);
    }

    public static <T extends ViewModel> T from(Activity activity, Class<T> cls) throws IllegalArgumentException {
        if (null == activity) {
            throw new IllegalArgumentException("Illegal activity instance");
        }
        return from(activity.getApplication(), cls);
    }

    public static <T extends ViewModel> T from(Fragment fragment, Class<T> cls) throws IllegalArgumentException {
        if (null == fragment) {
            throw new IllegalArgumentException("Illegal fragment instance");
        }
        return from(fragment.getActivity(), cls);
    }
}
