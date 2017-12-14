package com.typany.skin2.home.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.typany.network.NetworkBoundResource;
import com.typany.network.Response;
import com.typany.network.StatefulResource;
import com.typany.skin.SkinPersistentRepository.SkinInfoRepository;
import com.typany.skin2.storage.SkinStorage;

import java.util.List;

/**
 * Created by dingbei on 11/19/2017.
 */

// the model for view in skin home page
public class SkinViewModel extends ViewModel {
    // todo: remove this hard code when api call is ready.
    private static final boolean LOCAL_MOCK = true;

    public SkinViewModel() {
    }

    private static class SkinHomeResource extends NetworkBoundResource<List<SkinViewEntity>, SkinInfoRepository> {
        public SkinHomeResource() {
        }

        @Override
        @WorkerThread
        protected void saveCallResult(SkinInfoRepository request) {
            SkinStorage.get().saveNewRequestResult(request);
        }

        @Override
        protected boolean shouldFetch(List<SkinViewEntity> data) {
            if (LOCAL_MOCK) {
                return false;
            }

            return data.isEmpty();
        }

        @Override
        protected LiveData<List<SkinViewEntity>> loadFromDisk(Object... params) {
//            if (params == null || params.length == 0) {
//                throw new RuntimeException();
//            }

            if (LOCAL_MOCK) {
                return SkinStorage.get().getMockPageList();
            }

            return SkinStorage.get().getHomePageList();
        }

        @Nullable
        @Override
        protected LiveData<Response<SkinInfoRepository>> createCall() {
            return SkinStorage.get().createSkinInfoRequest();
        }
    }

    public LiveData<StatefulResource<List<SkinViewEntity>>> getHomePage() {
        return new SkinHomeResource().getAsLiveData();
    }
}
