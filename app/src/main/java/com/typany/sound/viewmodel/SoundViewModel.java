package com.typany.sound.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.typany.debug.SLog;
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

    public void clearLastClickedItem() {
        clickedItem = null;
    }

    // local sound resource, which load local item only via loadFromDisk, and disable all other
    // remote requesting methods or make them empty.
    private static final class LocalSoundResource extends NetworkBoundResource<List<SoundBoundItem>, Integer> {
        @Override
        protected void saveCallResult(Integer sr) {
        }

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
    }

    // both local sound item and downloaded sound item via loadFromDisk, and enable all other
    // remote requesting methods to load info list of remote sound items.
    private static final class FullSoundResource extends NetworkBoundResource<List<SoundBoundItem>, SoundPersistentRepository.SoundInfoRepository> {
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
            return SoundStorage.get().createSoundInfoListRequest();
        }

        @Override
        protected void onFetchFailed() {
            SLog.e(TAG, "onFetchFailed, failed to fetch basic info of online sound bundles.");
        }
    }

    // current selected item,
    private SoundBoundItem selectedItem;
    private SoundBoundItem clickedItem;

    public SoundViewModel() {
    }

    // TODO: 已经选中的再次点击，不需要再走item.onClick流程, 但需要通知observer
    // 进行后续播放预览音，显示控制面板等可能的动作
    public void clickSoundItem(final SoundBoundItem item) {
        clickedItem = item;
        if (selectedItem == item) {
            item.postClickResult();
        } else {
            item.onClick();
        }
    }

    public SoundBoundItem getSelectedItem() {
        return selectedItem;
    }

    public LiveData<StatefulResource<List<SoundBoundItem>>> loadLocalRepository() {
        return new LocalSoundResource() .getAsLiveData();
    }

    public LiveData<StatefulResource<List<SoundBoundItem>>> loadFullRepository() {
        return new FullSoundResource().getAsLiveData();
    }

    public boolean updateSelectedItem(final SoundBoundItem item) {
        if (selectedItem != item) {
            if (null != selectedItem) {
                selectedItem.unselect();
            }

            selectedItem = item;
        }

        return clickedItem == item;
    }
}
