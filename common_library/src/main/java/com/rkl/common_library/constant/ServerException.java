package com.rkl.common_library.constant;
/**
 * Created by ArmGlobe on 2016/9/22.
 * 自定义的网络请求异常
 */
public class ServerException extends Exception {

    public ServerException(String msg){
        super(msg);
    }

}