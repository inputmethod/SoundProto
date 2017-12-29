package com.typany.api.base;

import android.support.annotation.NonNull;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yangfeng on 2017/12/28.
 */

public abstract class Repository {
    //    private static final String BASE_URI_DEFAULT = "http://10.134.73.228/api/";
    private static final String BASE_URI_DEFAULT = "http://www.typany.com/api/";
    protected final Retrofit retrofit;
    public Repository() {
        this(BASE_URI_DEFAULT);
    }

    public Repository(@NonNull String baseUrl) {
        this(baseUrl, GsonConverterFactory.create());
    }

    public Repository(@NonNull String baseUrl, @NonNull Converter.Factory converterFactory) {
        retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(converterFactory)
                .build();
    }
}
