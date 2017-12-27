package com.typany.api.skin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by yangfeng on 2017/12/5.
 */
public interface SkinResourceRetrofit {
    @Headers({ "Accept: application/json" })
    @GET("getallfeaturetheme")
    Call<SkinWrapper> getAllSkin();
}
