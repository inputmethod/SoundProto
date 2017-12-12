package com.typany.skin2.storage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.typany.base.IMEThread;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.home.network.SkinRequest;
import com.typany.skin2.model.SkinPackage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinStorage {
    private final Context context;
    private static final String SKIN_ASSERT_PATH = "skin";

    private final String resolution;
    private SkinPackage defaultSkinPackage;

    private List<SkinViewEntity> viewEntitiesCache = new ArrayList<>();

    private final String DEFAULT_SKIN_NAME = "1001001042";

    private final int PAGE_ITEM_COUNT = 5;

    private File[] skinStorageFolder;

    @MainThread
    public SkinStorage(Context context, final String resolution){
        this.context = context;
        this.resolution = resolution;
        skinStorageFolder = new File[2];

    }

    @MainThread
    public LiveData<SkinPackage> loadSkin(final String name) {
        final MutableLiveData<SkinPackage> loading = new MutableLiveData<>();

        IMEThread.postTask(IMEThread.ID.IO, new Runnable() {
            @Override
            public void run() {
                SkinPackage sp = loadSkinFromDisk(name);
                loading.postValue(sp);
            }
        });

        return loading;
    }

    @WorkerThread
    public SkinPackage loadSkinFromDisk(final String name) {
        SkinPackage sp = null;
        try {
            initSkinStorageFolders();
            // TODO fill zipfolder
            sp = new SkinStorageEntry(name, "builtintheme", resolution, skinStorageFolder).load();
        } catch (Exception e) {
            if (name == DEFAULT_SKIN_NAME)
                sp = SkinPackage.emptySkin();
            else
                sp = getDefaultSkinPackage();
        }
        return sp;
    }

    @WorkerThread
    private SkinPackage getDefaultSkinPackage() {
        if (defaultSkinPackage == null) {
            loadSkinFromDisk(DEFAULT_SKIN_NAME);
        }
        return defaultSkinPackage;
    }

    @WorkerThread
    private void initSkinStorageFolders() throws IOException {
        skinStorageFolder[1] = new File(new File(context.getFilesDir(), ".theme"), "typany_custom");
    }





    // for home page
    @MainThread
    public LiveData<List<SkinViewEntity>> getHomePageList(final int pageNo) {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        if (pageNo * PAGE_ITEM_COUNT < viewEntitiesCache.size())
        {
            List<SkinViewEntity> pageData = takeFromCache(pageNo*PAGE_ITEM_COUNT);
            mutableLiveData.setValue(pageData);
        }
        return mutableLiveData;
    }

    @MainThread
    private List<SkinViewEntity> takeFromCache(final int start) {
        List<SkinViewEntity> pageData = new ArrayList<>();
        for (int i = start; i < viewEntitiesCache.size() && pageData.size() < PAGE_ITEM_COUNT; i++)
            pageData.add(viewEntitiesCache.get(i));
        return pageData;
    }

    @WorkerThread
    public void saveNewRequestResult(SkinRequest request) {
        // TODO request add to cache
    }
}
