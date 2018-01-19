package com.rkl.common_library.net;

import com.google.gson.Gson;
import com.rkl.common_library.constant.APIServiceConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ArmGlobe on 2016/9/9.
 */
public enum RetrofitClient {
    INSTANCE;
    private Retrofit retrofit;
    RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .client(OKHttpFactory.INSTANCE.getOkHttpClient())
                .baseUrl(APIServiceConfig.getAPI())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){
        return  retrofit;
    }
}
