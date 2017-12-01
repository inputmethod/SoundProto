package com.typany.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * Created by dingbei on 11/8/2017.
 * // https://developer.android.com/topic/libraries/architecture/guide.html
 */
// ResultType: Type for the Resource data
// RequestType: Type for the API response
public abstract class NetworkBoundResource<ValueType, RequestType> {
    public NetworkBoundResource(Object...params) {
        reload(params);
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract boolean shouldFetch(ValueType data);

    // Called to get the cached data from the database
    @MainThread
    protected abstract LiveData<ValueType> loadFromDisk(Object...params);

    // Called to create the API call.
    @Nullable
    @MainThread
    protected abstract LiveData<Response<RequestType>> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected void onFetchFailed() {
    }

    // fixme: the method is expected to run within main thread while SoundBundleItem constructor
    // is in worker thread from SoundStorage class, then crash with result.setValue(loading), so
    // change setValue to postValue as a workaround
//    @MainThread
    protected void reload(Object...params) {
        if (result.getValue() == null)
        {
            // 一开始是loading状态
            StatefulResource<ValueType> loading = StatefulResource.loading(null);
            result.postValue(loading);
        }

        final LiveData<ValueType> diskSource = loadFromDisk(params);

        result.addSource(diskSource, new Observer<ValueType>() {
            @Override
            public void onChanged(ValueType newdata) {
                result.removeSource(diskSource);
                // 读取结束后，看看是否过期
                if (shouldFetch(newdata)) {
                    fetchFromNetwork(diskSource);
                } else {
                    // TODO 验证一下为什么还能进回调
                    result.addSource(diskSource, new Observer<ValueType>() {
                        @Override
                        public void onChanged(@Nullable ValueType newdata) {
                            result.setValue(StatefulResource.success(newdata));
                        }
                    });
                }
            }
        });
    }


    protected final MediatorLiveData<StatefulResource<ValueType>> result = new MediatorLiveData<>();

    private void fetchFromNetwork(final LiveData<ValueType> dbSource) {
        final LiveData<Response<RequestType>> apiResponse = createCall();

        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource, new Observer<ValueType>() {
                    @Override
                    public void onChanged(@Nullable ValueType newdata) {
                        result.setValue(StatefulResource.loading(newdata));
                    }
                });

        result.addSource(apiResponse, new Observer<Response<RequestType>>() {
                    @Override
                    public void onChanged(final Response<RequestType> response) {
                        if (response.done()) {
                            result.removeSource(apiResponse);
                            result.removeSource(dbSource);
                            //noinspection ConstantConditions
                            if (response.isSuccessful()) {
                                saveResultAndReInit(response);
                            } else {
                                onFetchFailed();
                                result.addSource(dbSource, new Observer<ValueType>() {
                                    @Override
                                    public void onChanged(@Nullable ValueType newdata) {
                                        result.setValue(StatefulResource.error(response.errorMessage, newdata));
                                    }
                                });
                            }
                        } else {
                            // 有些场景需要知道下载进度，所以...
                            StatefulResource<ValueType> t = StatefulResource.loading(response.progress);
                            result.setValue(t);
                        }
                    }
                });
    }

    private void saveResultAndReInit(final Response<RequestType> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response.body);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(loadFromDisk(), new Observer<ValueType>() {
                            @Override
                            public void onChanged(@Nullable ValueType newdata) {
                                result.setValue(StatefulResource.success(newdata));
                            }
                        });
            }
        }.execute();
    }

    public final MutableLiveData<StatefulResource<ValueType>> getAsLiveData() {
        return result;
    }
}