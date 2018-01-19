package com.rkl.common_library.net;


import com.rkl.common_library.base.BaseApplication;
import com.rkl.common_library.constant.OAuthInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by ArmGlobe on 2016/9/9.
 */

enum OKHttpFactory {

    INSTANCE;

    private final OkHttpClient okHttpClient;

    private static final int TIMEOUT_READ = 10;
    private static final int TIMEOUT_WRITE = 10;
    private static final int TIMEOUT_CONNECTION = 10;

    OKHttpFactory() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //缓存
        File cacheFile = new File(BaseApplication.cacheDir, "/NetCache");
        Cache cache = new Cache(cacheFile, 50 * 1024 * 1024);

        OAuthInterceptor authInterceptor = new OAuthInterceptor();
//        https证书不为空的情况下设置证书
        if (HttpsFactroy.getSSLSocketFactory() != null) {
            builder.sslSocketFactory(HttpsFactroy.getSSLSocketFactory());
            builder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
        builder.cache(cache).addNetworkInterceptor(authInterceptor);

        builder.retryOnConnectionFailure(true)//失败重连
                //time out
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS);

        okHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}