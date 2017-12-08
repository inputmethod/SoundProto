package com.typany.sound.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.typany.base.IMEThread;
import com.typany.base.storage.ProtobufBasedStorage;
import com.typany.debug.SLog;
import com.typany.http.toolbox.RequestUtil;
import com.typany.network.Response;
import com.typany.sound.SoundPersistentRepository;
import com.typany.utilities.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Created by wujian on 2017/11/9.
 */

public class SoundStorage {
    private static final String TAG = SoundStorage.class.getSimpleName();

    private final Context context;

    private ProtobufBasedStorage<SoundPersistentRepository.SoundInfoRepository> pbStorage = new ProtobufBasedStorage<>();

    private static AssetManager assetManager;

    private static final String DEFAULT_LAST_SOUND_FOLDER = "1-system";
    private static final String LAST_SOUND_FOLDER = "LAST_SOUND_FOLDER";
    private static final String SET_USING_SKIN_TONE = "SET_USING_SKIN_TONE";

    private static String SOUND_ASSERT_PATH = "sound";
    private static String SOUND_CONF_FILE_NAME = "sound.pb";

    private SharedPreferences preferences;

    private boolean remoteItemsChanged = false;

    @MainThread
    public static void init(Context sAppContext) {
        if (ins == null) {
            ins = new SoundStorage(sAppContext);
            ins.preferences = PreferenceManager.getDefaultSharedPreferences(sAppContext);
        }

        assetManager = sAppContext.getAssets();
    }

    @MainThread
    public SoundStorage(Context context) {
        this.context = context;
    }

    private MutableLiveData<List<SoundBoundItem>> localItems = null;
    private MutableLiveData<List<SoundBoundItem>> fullItems = null;

    private static SoundStorage ins = null;

    public static SoundStorage get() {
        if (ins == null)
            throw new RuntimeException("init must be called before get");

        return ins;
    }

    public boolean getUsingSkinTone() {
        boolean data = preferences.getBoolean(SET_USING_SKIN_TONE, false);
        SLog.d(TAG, "getUsingSkinTone, " + data);
        return data;
    }

    private String getLastSelectedName() {
        String data = "";
        if (!getUsingSkinTone()) {
            data = preferences.getString(LAST_SOUND_FOLDER, DEFAULT_LAST_SOUND_FOLDER);
        }

        SLog.d(TAG, "getLastSelectedName, " + data);
        return data;
    }

    public LiveData<List<SoundBoundItem>> loadLocalRepository() {
        if (localItems == null) {
            localItems = new MutableLiveData<>();
            IMEThread.postTask(IMEThread.ID.IO, new Runnable() {
                @Override
                public void run() {
                    List<SoundBoundItem> itemList = itemListFromLocalPB(SOUND_CONF_FILE_NAME);
                    localItems.postValue(itemList);
                }
            });
        }
        return localItems;
    }

    private boolean isEmptyData(MutableLiveData<List<SoundBoundItem>> items) {
        return null == items || null == items.getValue() || items.getValue().isEmpty();
    }

    private void ensureLocalItems() {
        if (localItems == null) {
            localItems = new MutableLiveData<>();
        }
    }

    private void ensureFullItems() {
        if (fullItems == null) {
            fullItems = new MutableLiveData<>();
        }
    }

    @WorkerThread
    private List<SoundBoundItem> loadLocalItemList() {
        final List<SoundBoundItem> localItemList;
        if (isEmptyData(localItems)) {
            localItemList = itemListFromLocalPB(SOUND_CONF_FILE_NAME);
            localItems.postValue(localItemList);
        } else {
            localItemList = localItems.getValue();
        }

        return localItemList;
    }

    private void reloadFullRepositoryOnBackground() {
        IMEThread.postTask(IMEThread.ID.IO, new Runnable() {
            @Override
            public void run() {
                List<SoundBoundItem> fullList = new ArrayList<>();
                fullList.addAll(loadLocalItemList());
                fullList.addAll(itemListFromRemotePB());
                fullItems.postValue(fullList);
                remoteItemsChanged = false;
            }
        });
    }

    // need to load if
    // a. has never load before
    // b. only local item in the full item list
    private boolean needToLoad() {
        if (isEmptyData(localItems) || isEmptyData(fullItems)) {
            return true;
        } else {
            return remoteItemsChanged;
        }
    }

    public LiveData<List<SoundBoundItem>> loadFullRepository() {
        if (needToLoad()) {
            ensureLocalItems();
            ensureFullItems();
            reloadFullRepositoryOnBackground();
        }
        return fullItems;
    }

    @WorkerThread
    public List<SoundBoundItem> itemListFromLocalPB(String fileName) {
        List<SoundBoundItem> itemList;
        InputStream fis = null;
        try {
            String assetInfoPath = FileUtils.joinFilePath(SOUND_ASSERT_PATH, fileName);
            fis = assetManager.open(assetInfoPath);
            itemList = itemListFromPB(fis);
        } catch (Exception e) {
            e.printStackTrace();
            itemList = Collections.emptyList();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return itemList;
    }

    @WorkerThread
    public List<SoundBoundItem> itemListFromRemotePB() {
        List<SoundBoundItem> itemList;
        try {
            File downloadedInfoFile = getRemoteInfoFile();
            if (downloadedInfoFile.exists()) {
                FileInputStream fis = new FileInputStream(downloadedInfoFile);
                itemList = itemListFromPB(fis);
            } else {
                itemList = Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            itemList = Collections.emptyList();
        }
        return itemList;
    }

    private SoundBoundItem from(SoundPersistentRepository.SoundBasicInfo info, String selectedName) {
        String name = info.getBundleName();
        return new SoundBoundItem(name, TextUtils.equals(selectedName, name), info.getPreviewIconUrl());
    }

    @WorkerThread
    public List<SoundBoundItem> itemListFromPB(final InputStream fis) throws Exception {
        List<SoundBoundItem> itemList = new ArrayList<>();
        SoundPersistentRepository.SoundInfoRepository localInfo = SoundPersistentRepository.SoundInfoRepository.parseFrom(fis);
        List<SoundPersistentRepository.SoundBasicInfo> infoList = localInfo.getBasicInfoList();

        final String selectedName = getLastSelectedName();
        for (final SoundPersistentRepository.SoundBasicInfo info : infoList) {
            itemList.add(from(info, selectedName));
        }
        return itemList;
    }

    @WorkerThread
    public SoundBundle loadSoundBundleData(final SoundBundle item) {
        final String name = item.bundleName();
        SoundPersistentRepository.PositionInfo info = loadPositionInfoFromDisk(name);
        item.setBundleContent(info);
        return item;
    }

    @WorkerThread
    public void saveBundleContent(final String bundleName, final SoundPersistentRepository.SoundBundleContent data) {
        // SoundBundleContent里有两部分内容，pos和ogg
        try {
            // save pos info
            FileOutputStream fos = new FileOutputStream(getPositionFile(bundleName));
            SoundPersistentRepository.PositionInfo info = data.getPositionInfo();
            info.writeTo(fos);

            // save oggs
            final Map<Integer, com.google.protobuf.ByteString> oggs = data.getOggsMap();
            final String oggFolder = getOggFolderPath(bundleName);
            for (Map.Entry<Integer, com.google.protobuf.ByteString> entry : oggs.entrySet()) {
                final String oggFileName = FileUtils.joinFilePath(oggFolder, entry.getKey() + ".ogg");
                FileOutputStream oggFos = new FileOutputStream(oggFileName);
                entry.getValue().writeTo(oggFos);
            }
        } catch (Exception e) {
            // TODO
        }
    }

    private SoundPersistentRepository.SoundInfoRepository remoteInfo;

    @WorkerThread
    // TODO 是去重插入还是以服务器最新为准？
    public void updateRemoteSoundList(SoundPersistentRepository.SoundInfoRepository sr) {
        final SoundPersistentRepository.SoundInfoRepository.Builder builder;
        if (null == remoteInfo) {
            builder = SoundPersistentRepository.SoundInfoRepository.newBuilder();
        } else {
            builder = remoteInfo.toBuilder();
        }

        // sr里是从服务器下载的最新items，因为不会有几个，就在数组中遍历吧
        for (SoundPersistentRepository.SoundBasicInfo info : sr.getBasicInfoList()) {
            if (!contains(info)) {
                builder.addBasicInfo(info);
                remoteItemsChanged = true;
            }
        }

        if (remoteItemsChanged) {
            remoteInfo = builder.build();
            try {
                File downloadedInfoFile = getRemoteInfoFile();
                FileOutputStream fos = new FileOutputStream(downloadedInfoFile);
                remoteInfo.writeTo(fos);
            } catch (Exception e) {
                e.printStackTrace();
                SLog.e(TAG, "updateRemoteSoundList failed to write basic info." + remoteInfo);
            }
        } else {
            // need to do nothing as no more updated items
        }
    }

    @WorkerThread
    private boolean contains(SoundPersistentRepository.SoundBasicInfo info) {
        if (null != remoteInfo) {
            List<SoundPersistentRepository.SoundBasicInfo> local = remoteInfo.getBasicInfoList();
            for (SoundPersistentRepository.SoundBasicInfo localItem : local) {
                if (TextUtils.equals(localItem.getBundleName(), info.getBundleName()))
                    return true;
            }
        }
        return false;
    }

    // check 'name' exist in downloaded folder and load it there, otherwise, try from build-in assets
    private SoundPersistentRepository.PositionInfo loadPositionInfoFromDisk(final String name) {
        SoundPersistentRepository.PositionInfo info = null;

        InputStream fis = null;
        try {
            if (FileUtils.getChildFile(getDownloadedPath(context), name).exists()) {
                File file = getPositionFile(name);
                if (file.exists()) {
                    fis = new FileInputStream(file);
                } else {
                    fis = null;
                }
            } else {
                String assetInfoPath = FileUtils.joinFilePath(SOUND_ASSERT_PATH, name, SOUND_CONF_FILE_NAME);
                fis = assetManager.open(assetInfoPath);
            }

            if (null != fis) {
                info = SoundPersistentRepository.PositionInfo.parseFrom(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SLog.e(TAG, "loadPositionInfoFromDisk failed to get position info for " + name);
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return info;
    }

    @WorkerThread
    private File getPositionFile(final String name) throws IOException {
        File dirFile = FileUtils.ensureFolder(getDownloadedPath(context), name);
        return FileUtils.getChildFile(dirFile, SOUND_CONF_FILE_NAME);
    }

    @WorkerThread
    private String getOggFolderPath(final String name) throws IOException {
        return FileUtils.ensureFolder(getDownloadedPath(context), name).getAbsolutePath();
    }

    public static File getDownloadedPath(Context context) throws IOException {
        return FileUtils.getDownloadedAssetRoot(context, SOUND_ASSERT_PATH);
    }

    private File getRemoteInfoFile() throws IOException {
        return FileUtils.getChildFile(getDownloadedPath(context), SOUND_CONF_FILE_NAME);
    }

    public LiveData<Response<SoundPersistentRepository.SoundInfoRepository>> createSoundInfoListRequest() {
        // TODO: complete the API URI with formal url
        return RequestUtil.observalbeRequestProtobuf("http://10.134.73.228/api/soundres?proto=1", context,
                SoundPersistentRepository.SoundInfoRepository.parser());
    }
}
