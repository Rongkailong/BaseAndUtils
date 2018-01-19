package com.rkl.common_library.constant;

import android.content.Context;

import com.rkl.common_library.base.BaseApplication;

import java.util.logging.Logger;


/**
 * Created by Maple on 2017/3/6.
 */

public class ConfigConstant {

    /*运行环境配置*/
    public static final String TAG="";//logger的tag
    public static boolean isRelease=false;//是否是正式版




    /*Orm数据库配置文件*/
    //数据库缓存的地址和数据库名字
    public static final String DB_NAME_PATH = BaseApplication.getmAppContext().getDir("db", Context.MODE_PRIVATE).getAbsolutePath();
    public static final String DB_NAME = "CommonOrm.db";

}
