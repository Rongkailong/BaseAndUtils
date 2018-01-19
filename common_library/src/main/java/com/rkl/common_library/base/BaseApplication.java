package com.rkl.common_library.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.Logger;
import com.rkl.common_library.constant.ConfigConstant;
import com.rkl.common_library.net.NetworkStateService;
import com.rkl.common_library.util.Utils;


/**
 * 主要功能：BaseApplication
 * Created by rkl on 2018/1/11.
 * 修改历史：
 */

public class BaseApplication extends Application {
    public static String cacheDir;
    public static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        Logger.init(ConfigConstant.TAG);//初始化日志
        Utils.init(this);//初始化工具
        startService(new Intent(this, NetworkStateService.class));
        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);//分包

    }
    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static Context getmAppContext() {
        return mAppContext;
    }

    public static String getCachedir(){
        return cacheDir;
    }

}
