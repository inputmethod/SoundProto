package com.typany.sound.service;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.WorkerThread;

import com.typany.http.toolbox.RequestUtil;
import com.typany.network.NetworkBoundResource;
import com.typany.network.Response;
import com.typany.network.StatefulResource;
import com.typany.sound.SoundPersistentRepository;

/**
 * Created by wujian on 2017/11/9.
 */

// SoundBoundItem是在load SoundBoundItemList的时候被创建的
// 所以构造函数会被执行到WorkerThread
public class SoundBoundItem extends NetworkBoundResource<SoundBundle, SoundPersistentRepository.SoundBundleContent> {
    @WorkerThread
    public SoundBoundItem(final String bundleName, final boolean select, final String previewUrl) {
        super(bundleName, select, previewUrl);
    }

    @Override
    @WorkerThread
    protected LiveData<SoundBundle> loadFromDisk(Object...params) {
        MutableLiveData<SoundBundle> mutableItem = null;

        StatefulResource<SoundBundle> item = getAsLiveData().getValue();
        SoundBundle soundBundle = null == item ? null : item.data;

        if (soundBundle == null) {
            // 说明是第一次进来，需要加载显示item用的信息，loadInfo
            mutableItem = new MutableLiveData<>();
            if (params.length != 3) {
                throw new IllegalArgumentException("SoundBoundItem should load with 3 parameters");
            }
            String bundleName = (String) params[0];
            boolean select = (boolean) params[1];
            String previewUrl = (String) params[2];
            soundBundle = new SoundBundle(bundleName, select, previewUrl);
            StatefulResource<SoundBundle> initData = StatefulResource.loading(soundBundle);
            getAsLiveData().postValue(initData);
        }

        switch (soundBundle.getStatus()) {
            case INFO_LOADED:
                mutableItem = new MutableLiveData<>();
                mutableItem.postValue(SoundStorage.get().loadSoundBundleData(soundBundle));
                break;
            case DATA_LOAED:
                // TODO
                break;
        }
        return mutableItem;
    }

    @Override
    protected LiveData<Response<SoundPersistentRepository.SoundBundleContent>> createCall() {
        SoundBundle soundBundle = getAsLiveData().getValue().data;
        String url = "http://10.134.73.228/api/soundproto?id=" + soundBundle.bundleName();
        return RequestUtil.observalbeRequestProtobuf(url, null, SoundPersistentRepository.SoundBundleContent.parser());
    }

    @Override
    protected void saveCallResult(SoundPersistentRepository.SoundBundleContent data){
        SoundBundle soundBundle = getAsLiveData().getValue().data;

        soundBundle.setBundleContent(data.getPositionInfo());
        soundBundle.setSelect(true);
        SoundStorage.get().saveBundleContent(soundBundle.bundleName(), data);
    }

    @Override
    protected boolean shouldFetch(SoundBundle data){
        return data.shouldFetch();
    }

    @Override
    protected void onFetchFailed() {
        // TODO log
    }

    public void onClick() {
        SoundBundle soundBundle = getAsLiveData().getValue().data;

        switch (soundBundle.getStatus()) {
            case INFO_LOADED:
                reload();
                break;
            case DATA_LOAED:
                setSelectAndUpdate(soundBundle, true);
                break;
        }
    }

    public void unselect() {
        SoundBundle soundBundle = getAsLiveData().getValue().data;
        setSelectAndUpdate(soundBundle, false);
    }

    public void setSelectAndUpdate(final SoundBundle item, boolean select) {
        if (null == item) {
            return;
        }

        item.setSelect(select);
        result.setValue(StatefulResource.success(item));
    }
}
