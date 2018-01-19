package com.rkl.common_library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by hzh on 2015/11/15.
 */
public class SharedPrefrencesUtil {
    public static void saveData(Context context, String fileName, String key, Object data){
        if(data==null){
            Log.e("添加sp错误","添加了空的数据");
            return;
        }
//        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (data instanceof Integer){
            editor.putInt(key, (Integer)data);
        }else if (data instanceof Boolean){
            editor.putBoolean(key, (Boolean)data);
        }else if (data instanceof String){
            editor.putString(key, (String)data);
        }else if (data instanceof Float){
            editor.putFloat(key, (Float)data);
        }else if (data instanceof Long){
            editor.putLong(key, (Long)data);
        }else{
            Log.e("添加sp错误","未支持该类型，请在该文件添加"+SharedPrefrencesUtil.class.getSimpleName());
        }

        editor.commit();
    }

    /**
     * 从文件中读取数据
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static  <T extends Object> T getData(Context context, String fileName, String key, Object defValue){

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (fileName, Context.MODE_PRIVATE);
        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)){
            return (T)(Integer)sharedPreferences.getInt(key, (Integer)defValue);
        }else if ("Boolean".equals(type)){
            return (T)(Boolean)sharedPreferences.getBoolean(key, (Boolean)defValue);
        }else if ("String".equals(type)){
            return (T)sharedPreferences.getString(key, (String)defValue);
        }else if ("Float".equals(type)){
            return (T)(Float)sharedPreferences.getFloat(key, (Float)defValue);
        }else if ("Long".equals(type)){
            return (T)(Long)sharedPreferences.getLong(key, (Long)defValue);
        }

        throw new RuntimeException("get SharedPrefrences error!!");
    }

}
