package com.rkl.common_library.constant;

import android.content.Context;
import android.os.Environment;

import com.rkl.common_library.base.BaseApplication;

import java.util.logging.Logger;


/**
 * Created by rkl on 2017/3/6.
 */

public class ConfigConstant {

    /*运行环境配置*/
    public static final String TAG="";//logger的tag
    public static boolean isRelease=false;//是否是正式版

    /*目录名配置*/
    public static final String ROOT_PATH = "/root/";//根目录
    public static final String IMAGE_PATH = ROOT_PATH + "image/";//图片目录
    public static final String DOWNLOAD_PATH = ROOT_PATH + "download/";//下载目录
    public static final String DOWNLOAD_VIDEO_PATH = ROOT_PATH + "video/";//视频下载目录




    /*Orm数据库配置文件*/
    //数据库缓存的地址和数据库名字
    public static final String DB_NAME_PATH = BaseApplication.getmAppContext().getDir("db", Context.MODE_PRIVATE).getAbsolutePath();
    public static final String DB_NAME = "CommonOrm.db";

}
