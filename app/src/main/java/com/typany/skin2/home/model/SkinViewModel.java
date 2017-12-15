package com.typany.skin2.home.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.typany.network.NetworkBoundResource;
import com.typany.network.Response;
import com.typany.network.StatefulResource;
import com.typany.skin.SkinPersistentRepository.SkinCategoryRepository;
import com.typany.skin.SkinPersistentRepository.SkinCollectionRepository;
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
            SkinStorage.get().saveSkinInfoResult(request);
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
    private static class SkinCategoryGroupResource extends NetworkBoundResource<List<SkinViewEntity>, SkinCollectionRepository> {
        private String groupName;
        public SkinCategoryGroupResource(String groupName) {
            super(groupName);
        }

        @Override
        @WorkerThread
        protected void saveCallResult(SkinCollectionRepository request) {
            SkinStorage.get().saveCategoryGroupResult(groupName, request);
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
            if (params == null || params.length == 0) {
                throw new RuntimeException();
            }

            groupName = (String) params[0];

            if (LOCAL_MOCK) {
                return SkinStorage.get().getMockCategoryGroupPage(groupName);
            }

            return SkinStorage.get().getCategoryGroupPage(groupName);
        }

        @Nullable
        @Override
        protected LiveData<Response<SkinCollectionRepository>> createCall() {
            return SkinStorage.get().createCategoryGroupRequest(groupName);
        }
    }

    private static class SkinCategoryResource extends NetworkBoundResource<List<SkinViewEntity>, SkinCategoryRepository> {
        private String categoryName;
        public SkinCategoryResource(String categoryName) {
            super(categoryName);
        }

        @Override
        @WorkerThread
        protected void saveCallResult(SkinCategoryRepository request) {
            SkinStorage.get().saveCategoryResult(categoryName, request);
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
            if (params == null || params.length == 0) {
                throw new RuntimeException();
            }

            categoryName = (String) params[0];

            if (LOCAL_MOCK) {
                return SkinStorage.get().getMockCategoryPage(categoryName);
            }

            return SkinStorage.get().getCategoryPage(categoryName);
        }

        @Nullable
        @Override
        protected LiveData<Response<SkinCategoryRepository>> createCall() {
            return SkinStorage.get().createCategoryRequest(categoryName);
        }
    }

    public LiveData<StatefulResource<List<SkinViewEntity>>> getHomePage() {
        return new SkinHomeResource().getAsLiveData();
    }

    public LiveData getCategoryGroupPage(String groupName) {
        return new SkinCategoryGroupResource(groupName).getAsLiveData();
    }

    public LiveData getCategoryPage(String categoryName) {
        return new SkinCategoryResource(categoryName).getAsLiveData();
    }
}
