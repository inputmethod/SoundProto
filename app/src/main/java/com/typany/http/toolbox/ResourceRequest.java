package com.typany.http.toolbox;

import android.arch.lifecycle.MutableLiveData;

import com.typany.http.Request;
import com.typany.http.Response;
import com.typany.http.VolleyError;

/**
 * Created by liuyan on 17-11-12.
 */

public abstract class ResourceRequest<T> extends Request<T> {

    final private MutableLiveData<com.typany.network.Response<T>> mObserverble;
    private int lastProgress = 0;
    public ResourceRequest(final int method,  final String url,
                           final MutableLiveData<com.typany.network.Response<T>> observerble) {
        super(method, url, null);
        mObserverble = observerble;

        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mObserverble.setValue(com.typany.network.Response.<T>error(error.getMessage()));
            }
        };
    }

    @Override
    protected void deliverResponse(T response) {
        mObserverble.setValue(com.typany.network.Response.<T>success(response));
    }

    @Override
    protected void deliverProgress(boolean isUpload, long current, long total) {
        super.deliverProgress(isUpload, current, total);
        int curProgress = (int)(current * 100 / total);
        if (curProgress != lastProgress) {
            mObserverble.setValue(com.typany.network.Response.<T>progress(curProgress));
            lastProgress = curProgress;
        }
    }
}

