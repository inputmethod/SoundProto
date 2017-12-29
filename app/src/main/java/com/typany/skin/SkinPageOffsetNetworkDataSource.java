package com.typany.skin;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.typany.api.skin.SkinItem;
import com.typany.api.skin.SkinListResponse;
import com.typany.api.skin.SkinRepositoryImpl;

import java.util.Collections;

/**
 * This file is part of Everboxing modules. <br/><br/>
 * <p>
 * The MIT License (MIT) <br/><br/>
 * <p>
 * Copyright (c) 2017 Malyshev Yegor aka brainail at org.brainail.everboxing@gmail.com <br/><br/>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy <br/>
 * of this software and associated documentation files (the "Software"), to deal <br/>
 * in the Software without restriction, including without limitation the rights <br/>
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell <br/>
 * copies of the Software, and to permit persons to whom the Software is <br/>
 * furnished to do so, subject to the following conditions: <br/><br/>
 * <p>
 * The above copyright notice and this permission notice shall be included in <br/>
 * all copies or substantial portions of the Software. <br/><br/>
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR <br/>
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, <br/>
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE <br/>
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER <br/>
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, <br/>
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN <br/>
 * THE SOFTWARE.
 */
public abstract class SkinPageOffsetNetworkDataSource extends PageKeyedDataSource<Integer, SkinItem> {
    private static final String TAG = SkinPageOffsetNetworkDataSource.class.getSimpleName();

    private final SkinRepositoryImpl repository;

    protected SkinPageOffsetNetworkDataSource(SkinRepositoryImpl repository) {
        super();
        this.repository = repository;
    }

    private SkinListResponse loadPage(int index, int size) {
        SkinListResponse response;
        try {
            response = repository.getAllSkins(index, size).execute().body();
        } catch (final Exception exception) { // oops ;(
            response = new SkinListResponse(Collections.<SkinItem>emptyList());
        }
        return response;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, SkinItem> callback) {
        Log.e(TAG, "loadInitial, params = " + params.requestedLoadSize);
        SkinListResponse response = loadPage(0, params.requestedLoadSize);
        if (response.getCurrent_page() < response.getTotal_page()) {
            callback.onResult(response.themes, -1, response.getCurrent_page());
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, SkinItem> callback) {
        // ignored, since we only ever append to our initial load
        Log.e(TAG, "loadBefore, params = " + params.key + ", " + params.requestedLoadSize);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, SkinItem> callback) {
        Log.e(TAG, "loadBefore, params = " + params.key + ", " + params.requestedLoadSize);
        SkinListResponse response = loadPage(params.key, params.requestedLoadSize);
        if (response.getCurrent_page() < response.getTotal_page()) {
            callback.onResult(response.themes,response.getCurrent_page());
        }
    }
}