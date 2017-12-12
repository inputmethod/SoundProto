package com.typany.skin2.home.model;

import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.typany.network.NetworkBoundResource;
import com.typany.network.Response;
import com.typany.network.StatefulResource;
import com.typany.skin2.home.network.SkinRequest;
import com.typany.skin2.storage.SkinStorage;

import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

// the model for view in skin home page
public class SkinViewModel {
    private final SkinStorage skinStorage;
    public SkinViewModel(SkinStorage skinStorage) {
        this.skinStorage = skinStorage;
    }

    public LiveData<StatefulResource<List<SkinViewEntity>>> getHomePageList(final int pageNo) {
        return new NetworkBoundResource<List<SkinViewEntity>, SkinRequest>() {
            @Override
            @WorkerThread
            protected void saveCallResult(SkinRequest request) {
                skinStorage.saveNewRequestResult(request);
            }

            @Override
            protected boolean shouldFetch(List<SkinViewEntity> data) {
                return data.isEmpty();
            }

            @Override
            protected LiveData<List<SkinViewEntity>> loadFromDisk(Object... params) {
                if (params == null || params.length == 0)
                    throw new RuntimeException();

                Integer pageNo = (Integer)params[0];
                return skinStorage.getHomePageList(pageNo.intValue());
            }

            @Nullable
            @Override
            protected LiveData<Response<SkinRequest>> createCall() {
                // TODO
                return null;
            }
        }.getAsLiveData();
    }
}
