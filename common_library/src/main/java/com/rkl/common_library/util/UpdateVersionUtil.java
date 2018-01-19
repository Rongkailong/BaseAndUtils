package com.rkl.common_library.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * 主要功能:用于下载的工具类（使用Google内置的DownLoadManager）
 * Created by wz on 2016/11/11
 * 修订历史:
 */
public class UpdateVersionUtil {

    private Context mContext;
    private static long downloadId;
    private String apkName = "每刻兼职.apk";
    private float fileSize=50;//默认文件大小为50（单位MB）
    public UpdateVersionUtil(Context context) {
        this.mContext = context;
    }
    public UpdateVersionUtil(Context context, String apkName){
        this.mContext=context;
        this.apkName=apkName;
    }

    /**
     * 调用系统后台下载， 并在下载完成后自动安装
     *
     * @param downloadUrl
     *            完整下载地址
     */
    public void downloadBackground(String downloadUrl) {
        DownloadManager downloadManager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        String path = Environment.getExternalStorageDirectory()
                + Constant.DOWNLOAD_PATH + apkName;

        File file = new File(path);

        if (file.exists()) {
            file.delete();
        }
        //当外部储存容量大于50MB时候存在外部
        if(SDCardUtils.getFreeSpace(ConstUtils.MemoryUnit.MB)>fileSize){
            Logger.d("外部储存MB："+SDCardUtils.getFreeSpace(ConstUtils.MemoryUnit.MB));
            // 外部存储
            request.setDestinationInExternalPublicDir(Constant.DOWNLOAD_PATH,
                    apkName);
        }else if(SDCardUtils.getInternalMemoryFreeSpace(ConstUtils.MemoryUnit.MB)>fileSize){
            Logger.d("外部储存MB："+SDCardUtils.getInternalMemoryFreeSpace(ConstUtils.MemoryUnit.MB));
            // 内部存储
            request.setDestinationInExternalFilesDir(mContext,
                    Constant.DOWNLOAD_PATH, apkName);
        }else{
            ToastUtils.showShortToast(mContext,"您的手机内存不足，下载失败。");
            return;
        }

        downloadId = downloadManager.enqueue(request);

        mContext.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
            if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0) == downloadId) {
                String path = Environment.getExternalStorageDirectory()
                        + Constant.DOWNLOAD_PATH + apkName;
                installApk(path,context);
            }

        }
    };

    /**
     * 安装APK文件
     */
    public void installApk(String path, Context context) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            Uri contentUri = FileProvider.getUriForFile(context, "com.ourslook.jianke.provider", apkfile);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }else {
            i.setAction("android.intent.action.VIEW");
            i.addCategory("android.intent.category.DEFAULT");
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(i);
    }
}
