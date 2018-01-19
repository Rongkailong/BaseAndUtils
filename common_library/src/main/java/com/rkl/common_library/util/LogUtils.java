package com.rkl.common_library.util;


import com.orhanobut.logger.Logger;

/**
 * Created by shy on 2017/3/2.
 */
public class LogUtils {
    private static final Boolean LOGEnable = true;

    public static void d(String tag, String msg) {
        if (LOGEnable)
            Logger.t(tag).d(msg);
    }

    public static void e(String tag, String msg) {
        if (LOGEnable)
            Logger.t(tag).e(msg);
    }

    public static void w(String tag, String msg) {
        if (LOGEnable)
            Logger.t(tag).w(msg);
    }

    public static void v(String tag, String msg) {
        if (LOGEnable)
            Logger.t(tag).v(msg);
    }

    public static void i(String tag, String msg){
        if(LOGEnable)
            Logger.t(tag).i(msg);
    }

    public static void json(String tag, String msg) {
        if (LOGEnable)
            Logger.t(tag).json(msg);
    }

}
