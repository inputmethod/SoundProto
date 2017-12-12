package com.typany.skin2.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;

import com.typany.lifemanagement.AppLifetime;
import com.typany.skin2.storage.SkinStorage;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinModel extends AppLifetime {
    private final Context context;
    private final SharedPreferences preferences;
    private final SkinStorage skinStorage;
    private final MutableLiveData<SkinPackage> skinInUse;

    @MainThread
    public SkinModel(Context context, SkinStorage skinStorage) {
        this.context = context;
        this.skinStorage = skinStorage;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        skinInUse = new MutableLiveData<>();
    }

    @MainThread
    public LiveData<SkinPackage> getSkinInUse() {
        // TODO
        String skinName = preferences.getString("", "");
        skinName = "7000000864";
        loadSkin(skinName);
        return skinInUse;
    }

    @MainThread
    public void changeSkin(final String skinName) {
        loadSkin(skinName);
    }

    @MainThread
    private void loadSkin(final String name) {
        LiveData<SkinPackage> skinLoading = skinStorage.loadSkin(name);
        skinLoading.observe(this, new Observer<SkinPackage>() {
            @Override
            public void onChanged(@Nullable SkinPackage skinPackage) {
                skinInUse.setValue(skinPackage);
            }
        });
    }
}
