package com.typany.skin2.storage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.typany.base.IMEThread;
import com.typany.http.toolbox.RequestUtil;
import com.typany.ime.IMEApplicationContext;
import com.typany.network.Response;
import com.typany.skin.SkinPersistentRepository.SkinCategoryAllItems;
import com.typany.skin.SkinPersistentRepository.SkinCollectionAll;
import com.typany.skin.SkinPersistentRepository.SkinHeaderHome;
import com.typany.skin2.home.model.SkinViewEntity;
import com.typany.skin2.model.SkinPackage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbei on 11/19/2017.
 */

public class SkinStorage {
    private static SkinStorage _ins;

    private final Context context;
    private static final String SKIN_ASSERT_PATH = "skin";

    private final String resolution;
    private SkinPackage defaultSkinPackage;

    private final List<SkinViewEntity> homeViewEntitiesCache = new ArrayList<>();
    private final Map<String, List<SkinViewEntity>> categoryGroupCache = new HashMap<>();
    private final Map<String, List<SkinViewEntity>> categoryViewCache = new HashMap<>();

    private final String DEFAULT_SKIN_NAME = "1001001042";

    private final int PAGE_ITEM_COUNT = 5;

    private File[] skinStorageFolder;

    @MainThread
    public static void init(Context sAppContext, String resolution) {
        if (null == _ins) {
            _ins = new SkinStorage(sAppContext, resolution);
        }
    }
    @MainThread
    private SkinStorage(Context context, final String resolution){
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
    public LiveData<List<SkinViewEntity>> getHomePageList() {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(homeViewEntitiesCache);
        return mutableLiveData;
    }

    @MainThread
    public LiveData<List<SkinViewEntity>> getMockPageList() {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(SkinStorageMock.homeViewEntitiesCache);
        return mutableLiveData;
    }

//    @MainThread
//    private List<SkinViewEntity> takeFromCache(final int start) {
//        List<SkinViewEntity> pageData = new ArrayList<>();
//        for (int i = start; i < viewEntitiesCache.size() && pageData.size() < PAGE_ITEM_COUNT; i++)
//            pageData.add(viewEntitiesCache.get(i));
//        return pageData;
//    }

    @WorkerThread
    public void saveSkinInfoResult(SkinCategoryAllItems request) {
        // TODO: request add to cache
    }

    public LiveData<Response<SkinCategoryAllItems>> createSkinInfoRequest() {
        // TODO: complete the API URI with formal url
        return RequestUtil.observalbeRequestProtobuf("http://10.134.73.228/api/skinentry?proto=1", context,
                SkinCategoryAllItems.parser());
    }

    public static SkinStorage get() {
        if (null == _ins) {
            _ins = new SkinStorage(IMEApplicationContext.context, "1080");
        }

        return _ins;
    }

    public void saveCategoryGroupResult(String groupName, SkinHeaderHome request) {
        // TODO request add to cache
    }

    public LiveData<Response<SkinHeaderHome>> createCategoryGroupRequest(String bundleName) {
        // TODO: complete the API URI with formal url
        return RequestUtil.observalbeRequestProtobuf("http://10.134.73.228/api/skincollection?proto=1&id=" + bundleName, context,
                SkinHeaderHome.parser());
    }

    public void saveCategoryResult(String categoryName, SkinCollectionAll request) {
        // TODO request add to cache
    }

    public LiveData<Response<SkinCollectionAll>> createCategoryRequest(String bundleName) {
        // TODO: complete the API URI with formal url
        return RequestUtil.observalbeRequestProtobuf("http://10.134.73.228/api/skincategory?proto=1&id=" + bundleName, context,
                SkinCollectionAll.parser());
    }

    // todo: save and maintain cache
    public LiveData<List<SkinViewEntity>> getCategoryGroupPage(String groupName) {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(categoryGroupCache.get(groupName));
        return mutableLiveData;
    }

    // todo: save and maintain cache
    public LiveData<List<SkinViewEntity>> getCategoryPage(String categoryName) {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(categoryViewCache.get(categoryName));
        return mutableLiveData;
    }

    public LiveData<List<SkinViewEntity>> getMockCategoryGroupPage(String groupName) {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(SkinStorageMock.categoryGroupCache);
        return mutableLiveData;
    }

    public LiveData<List<SkinViewEntity>> getMockCategoryPage(String categoryName) {
        MutableLiveData<List<SkinViewEntity>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(SkinStorageMock.categoryViewCache);
        return mutableLiveData;
    }
}
