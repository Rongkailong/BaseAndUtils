package com.rkl.common_library.constant;

/**
 * Created by ArmGlobe on 2016/9/23.
 */
public class APIServiceConfig {

    //https 证书
    public static final String API_SERVICE_HTTPS_CERTIFICATE ="";

    public static final String ReleaseApi = "";  //APP 的正式环境
    public static final String TestApi = "";  // APP 的测试环境
    //appid
    public static final int API_SERVICE_ID = 6;

    //sign——key
    public static final String API_SERVICE_KEY = "";//deasjk12e12!@#dss

    public static final String API_DES_KEY = "123456789";



    public static String getAPI(){
        if (ConfigConstant.isRelease){
            return ReleaseApi;
        }
        return TestApi;
    }

}
