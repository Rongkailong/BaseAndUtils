package com.rkl.common_library.constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.rkl.common_library.base.BaseApplication;
import com.rkl.common_library.constant.APIServiceConfig;
import com.rkl.common_library.util.EncryptUtils;
import com.rkl.common_library.util.LogUtils;
import com.rkl.common_library.util.NetworkUtils;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rkl on 2018/1/12.
 * 拦截器
 */
public class OAuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetworkUtils.isConnected(BaseApplication.getmAppContext())) {

            Request originalRequest = chain.request();

            //添加新的参数
            HttpUrl.Builder requestBuilder = originalRequest.url().newBuilder()
                    .scheme(originalRequest.url().scheme())
                    .host(originalRequest.url().host());
            //插入键值对参数到 url query 中
//                .addQueryParameter("key","value");
            String url = requestBuilder.build().toString();
            //添加appid和sign
            if (!url.contains("&utype=")) {
//                String time = String.valueOf(System.currentTimeMillis() / 60000);
                String time = String.valueOf(System.currentTimeMillis() / 1000 / 60/60);
                String sign = APIServiceConfig.API_SERVICE_ID + APIServiceConfig.API_SERVICE_KEY + time;
                String encryptMD5ToString = EncryptUtils.encryptMD5ToString(sign).toLowerCase();
//                requestBuilder.addQueryParameter("utype", APIServiceConfig.API_UTYPE_ID + "");
//                requestBuilder.addQueryParameter("sign", "testsign");
                requestBuilder.addQueryParameter("sign", encryptMD5ToString);
                requestBuilder.addQueryParameter("appid", APIServiceConfig.API_SERVICE_ID + "");
            }
            //新的请求
            Request newRequest = originalRequest.newBuilder()
                    .method(originalRequest.method(), originalRequest.body())
                    .url(requestBuilder.build())
                    .build();

            LogUtils.e("TAG", requestBuilder.build().toString());

            Response response = chain.proceed(newRequest);

            String cacheControl = response.header("Cache-Control");

            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                return response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + 10)
                        .build();
            } else {
                return response;
            }
        } else {
            LogUtils.e("TAG", "withoutNet");

            Request request = chain.request();

            if (!isOnline()) {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }

            return chain.proceed(request);
        }
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getmAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
