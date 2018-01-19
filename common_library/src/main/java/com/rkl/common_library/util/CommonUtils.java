package com.rkl.common_library.util;

import android.os.Looper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shy on 2017/3/2.
 * 简单的通用util
 */
public class CommonUtils {

    /**
     * 只encode中文
     */
    public static String encode(String sourceString) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            String s1 = matcher.group();
            sourceString = sourceString.replace(s1, urlEncode(s1));
        }

        return sourceString;
    }


    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(e.toString(), "urlEncode");
            return URLEncoder.encode(s);
        }
    }

    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return URLDecoder.decode(s);
        }
    }

    /**
     * 返回设备信息
     *
     * @param context
     * @return
     */
//    public static DeviceModel getDeviceModel(Context context) {
//        DeviceModel deviceModel = new DeviceModel();
//        deviceModel.setBrand(DeviceUtils.getManufacturer());
//        deviceModel.setModel(DeviceUtils.getModel());
//        deviceModel.setResolution(ScreenUtils.getScreenHeight(context) + "*" + ScreenUtils.getScreenWidth(context));
//        deviceModel.setSystemversion(Build.VERSION.SDK_INT + "");
//        return deviceModel;
//    }

    /**
     * 返回设备信息的json数据
     *
     * @param context
     * @return
     */
//    public static String getDeviceJsonStr(Context context) {
//        return new Gson().toJson(getDeviceModel(context));
//    }

    /**
     * 四舍五入算法
     *
     * @param oldNum 需要计算的数字
     * @param n      精确的位数
     * @return
     */
    public static float getScaleNum(double oldNum, int n) {
        BigDecimal b = new BigDecimal(oldNum);
        float newNum = b.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
        return newNum;
    }


    //===ELSE===

    //生存uuid
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //判断是否在主线程
    public boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
