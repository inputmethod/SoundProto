package com.typany.sound.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.typany.debug.SLog;
import com.typany.http.toolbox.RequestUtil;
import com.typany.ime.IMEApplicationContext;
import com.typany.network.NetworkBoundResource;
import com.typany.network.Response;
import com.typany.network.StatefulResource;
import com.typany.sound.SoundPersistentRepository;
import com.typany.sound.service.SoundBoundItem;
import com.typany.sound.service.SoundStorage;

import java.util.List;


/**
 * Created by wujian on 2017/11/8.
 */

public class SoundViewModel extends ViewModel {
    private static final String TAG = SoundViewModel.class.getSimpleName();

    SoundBoundItem selectedItem;

    public SoundViewModel() {
    }

    public void clickSoundItem(final SoundBoundItem item) {
        if (selectedItem != item)
            item.onClick();
    }

    public SoundBoundItem getSelectedItem() {
        return selectedItem;
    }

    public LiveData<StatefulResource<List<SoundBoundItem>>> loadLocalRepository() {
        return new NetworkBoundResource<List<SoundBoundItem>, Integer>() {
            @Override
            protected void saveCallResult(Integer sr) {}

            @Override
            protected boolean shouldFetch(List<SoundBoundItem> data){
                return false;
            }

            @Override
            protected LiveData<List<SoundBoundItem>> loadFromDisk(Object...params) {
                return SoundStorage.get().loadLocalRepository();
            }

            @Override
            protected LiveData<Response<Integer>> createCall() {
                return null;
            }

            @Override
            protected void onFetchFailed() {
            }
        }.getAsLiveData();
    }

    public LiveData<StatefulResource<List<SoundBoundItem>>> loadFullRepository() {
        return new NetworkBoundResource<List<SoundBoundItem>, SoundPersistentRepository.SoundInfoRepository>() {
            @Override
            protected void saveCallResult(SoundPersistentRepository.SoundInfoRepository sr) {
                SoundStorage.get().updateRemoteSoundList(sr);
            }

            @Override
            protected boolean shouldFetch(List<SoundBoundItem> data){
                return true;
            }

            @Override
            protected LiveData<List<SoundBoundItem>> loadFromDisk(Object...params) {
                return SoundStorage.get().loadFullRepository();
            }

            @Override
            protected LiveData<Response<SoundPersistentRepository.SoundInfoRepository>> createCall() {
                // TODO: complete the API URI with formal url
                return RequestUtil.observalbeRequestProtobuf("http://10.134.73.228/api/soundres?proto=1",
                        IMEApplicationContext.context,
                        SoundPersistentRepository.SoundInfoRepository.parser());
            }

            @Override
            protected void onFetchFailed() {
                SLog.e(TAG, "onFetchFailed, failed to fetch basic info of online sound bundles.");
            }
        }.getAsLiveData();
    }

    public void updateSelectedItem(final SoundBoundItem item) {
        if (selectedItem != item) {
            selectedItem.unselect();
            selectedItem = item;
        }
    }
}
