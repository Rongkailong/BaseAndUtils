package com.rkl.common_library.net;


import com.rkl.common_library.constant.ApiService;

/**
 * Created by rkl on 2018/1/12.
 */
public enum  ApiFactory {
    INSTANCE;

    private final ApiService apiService;

    ApiFactory(){
        apiService = RetrofitClient.INSTANCE.getRetrofit().create(ApiService.class);
    }

    public ApiService getApiService(){
        return  apiService;
    }

}
