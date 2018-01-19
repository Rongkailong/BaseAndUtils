package com.rkl.common_library.util.helper;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 主要功能:
 * Created by wz on 2017/5/5
 * 修订历史:
 */
public class JsonReadHelper {
    /**
     * 读取assets中json文件的JSON字符串；
     */
    public static String getJSONString(Context context, String jsonName) {
        String jsonString = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(jsonName), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);//使用BufferReader读取输入流中的数据；
            String line;
            StringBuilder stringBuilder = new StringBuilder();//所有读取的json放到StringBuilder中，这里也可以使用StringBuffer,效果一样；
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonString = stringBuilder.toString();
            bufferedReader.close();//按相反的顺序关闭流；
            inputStreamReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
    public static String getAddressJSONString(Context context){
        return getJSONString(context,"findProvinceCityDistrictList.json");
    }


}
