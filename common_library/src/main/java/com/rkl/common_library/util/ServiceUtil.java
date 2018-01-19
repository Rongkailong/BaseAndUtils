package com.rkl.common_library.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * 主要功能:
 * Created by wz on 2016/10/31
 * 修订历史:
 */
public class ServiceUtil {
    private static final String TAG =ServiceUtil.class.getSimpleName();
    //开启服务：
    public static void startService(Context context, Class clazz, String action) {
        boolean serviceRunning = isServiceRunning(context, clazz);
        if (serviceRunning){
            forceStopService(context,clazz);//先停止再开启：重启服务,防止服务失效。
        }
//        Intent service = new Intent(context, clazz);
        Intent service = new Intent();
        service.setAction(action);
        service.setPackage(context.getPackageName());//这里你需要设置你应用的包名,5.0新要求
        context.startService(service);//多次调用startService并不会启动多个service 而是会多次调用onStart
    }

    /**
     * 停止service
     * @param context
     * @param clazz
     */
    public static void stopService(Context context, Class clazz) {
        boolean serviceRunning =isServiceRunning(context, clazz);
        if (serviceRunning){
            forceStopService(context, clazz);
        }
    }

    private static void forceStopService(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setPackage(context.getPackageName());//这里你需要设置你应用的包名,5.0新要求
        context.stopService(intent);
        //LogUtils.debug(TAG,clazz.getSimpleName()+" is stopped");
    }

    /**
     * service中检测本应用是否为前台界面
     * @param context
     * @return
     */
    public static boolean checkAppRunning(Context context) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
        String topPackageName = cn.getPackageName();

        String myPackageName = context.getPackageName();
        if (topPackageName != null && topPackageName.equals(myPackageName)) {
            //在这里停止含有定时执行的服务,在停止之前需要先取消该定时器
            isRunning = true;
            //LogUtils.debug(TAG,"当前栈顶应用:" + topPackageName + ",myPackageName=" + myPackageName);
        }else{
            isRunning = false;
        }
        return isRunning;
    }
    /**判断指定服务是否处于运行状态**/
    public static boolean isServiceRunning(Context mContext, Class clazz) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClass().equals(clazz) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }
}
