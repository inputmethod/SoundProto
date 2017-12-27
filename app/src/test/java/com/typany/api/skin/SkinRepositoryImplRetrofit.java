package com.typany.api.skin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yangfeng on 2017/12/5.
 */

// http://10.134.73.228/api/soundres?proto=1
class SkinRepositoryImplRetrofit {
//    private static final String BASE_URI = "http://10.134.73.228/api/";
    private static final String BASE_URI = "http://10.152.102.239:8080/api/";

    public List<Skin> getAllSkins() throws Exception {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URI).addConverterFactory(GsonConverterFactory.create())
                .build();
        SkinResourceRetrofit skinRepo = retrofit.create(SkinResourceRetrofit.class);
        Call<SkinWrapper> skinsWrapperCall = skinRepo.getAllSkin();
        return skinsWrapperCall.execute().body().getSounds();
    }
}
