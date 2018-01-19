package com.rkl.common_library.net;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;
import com.rkl.common_library.util.ToastUtils;


/**
 * 主要功能:主要用于监听网络状态的服务
 * Created by rkl on 2018/1/12
 * 修订历史:
 */
public class NetworkStateService extends Service {

    // Class that answers queries about the state of network connectivity.
    // 系统网络连接相关的操作管理类.

    private ConnectivityManager connectivityManager;
    // Describes the status of a network iml.
    // 网络状态信息的实例
    private NetworkInfo info;

    /**
     * 当前处于的网络
     * 0 ：null
     * 1 ：2G/3G
     * 2 ：wifi
     */
    public static int networkStatus=-1;

    public static final String NETWORKSTATE = "com.text.android.network.state"; // An action name

    /**
     * 广播实例
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // The action of this intent or null if none is specified.
            // action是行动的意思，也许是我水平问题无法理解为什么叫行动，我一直理解为标识（现在理解为意图）
            String action = intent.getAction(); //当前接受到的广播的标识(行动/意图)

            // 当前接受到的广播的标识(意图)为网络状态的标识时做相应判断
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                // 获取网络连接管理器
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                // 获取当前网络状态信息
                if (connectivityManager != null) {
                    info = connectivityManager.getActiveNetworkInfo();
                }

                if (info != null && info.isAvailable()) {

                    //当NetworkInfo不为空且是可用的情况下，获取当前网络的Type状态
                    //根据NetworkInfo.getTypeName()判断当前网络
                    String name = info.getTypeName();
                  if(networkStatus==0){
                        ToastUtils.showShortToast(context, "网络已经恢复，请尝试刷新界面。");
                    }
                    //更改NetworkStateService的静态变量，之后只要在Activity中进行判断就好了
                    if (name.equals("WIFI")) {
                        Logger.w("网络可用，状态 Wifi");
                        networkStatus = 2;
                    } else {
                        Logger.w("网络可用，状态 3G/4G");
                        networkStatus = 1;
                    }

                } else {
                    Logger.w("网络不可用！");
                    // NetworkInfo为空或者是不可用的情况下
                    networkStatus = 0;

                    ToastUtils.showShortToast(context, "貌似网络已经断开，请检查网络状态。");

                    Intent it = new Intent();
                    it.putExtra("networkStatus", networkStatus);
                    it.setAction(NETWORKSTATE);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);//发送无网络广播给注册了当前服务广播的Activity
//                    sendBroadcast(it);
                    /**
                     * 这里推荐使用本地广播的方式发送:
                     *
                     */
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册网络状态的广播，绑定到mReceiver
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销接收
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        // 获取网络连接管理器
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取当前网络状态信息
        NetworkInfo[] info = new NetworkInfo[0];
        if (mgr != null) {
            info = mgr.getAllNetworkInfo();
        }
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }
}