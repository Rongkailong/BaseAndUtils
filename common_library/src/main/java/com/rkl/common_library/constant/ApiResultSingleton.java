package com.rkl.common_library.constant;

import android.util.ArrayMap;

/**
 * 主要功能:该类用于存放错误码和对应的错误信息
 * Created by rkl on 2018/1/12
 * 修订历史:
 */
public class ApiResultSingleton {
    private static volatile ApiResultSingleton instance = null;
    private static ArrayMap<Integer,String> resultData = null;
    private ApiResultSingleton(){
    }

    public static ApiResultSingleton getInstance() {
        if (instance == null) {
            synchronized (ApiResultSingleton.class) {
                if (instance == null) {
                    instance = new ApiResultSingleton();
//                    initData();
                }
            }
        }

        return instance;
    }
    public static String getErrorMsg(int code){
        if(resultData.containsKey(code)){
            return resultData.get(code);
        }else{
            return null;
        }
    }
    public static boolean isHaveCode(int code){
        return resultData.containsKey(code);
    }

}
