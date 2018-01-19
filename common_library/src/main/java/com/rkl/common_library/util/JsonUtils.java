package com.rkl.common_library.util;

/**
 * Created by shy on 2016/12/16.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 把Hshmap转换成json，拼装后以html格式输出
 *
 */
public class JsonUtils {
    /**把数据源HashMap转换成json
     * @param map
     */
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Entry e = (Entry) it.next();
            string += "'" + e.getKey() + "':";
            string += "'" + e.getValue() + "',";
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    public final static Map strJson2Map(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        return json2Map(jsonObject);
    }

    public final static Map json2Map(JSONObject jsonObject) throws JSONException {
        Map resultMap = new HashMap();
        Iterator iterator = jsonObject.keys();

        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                value = json2Map((JSONObject) value);
                resultMap.put(key, value);
            } else if (value instanceof JSONArray) {
                value = json2List((JSONArray) value);
                resultMap.put(key, value);
            } else {
                resultMap.put(key, jsonObject.getString(key));
            }
        }
        return resultMap;
    }

    public final static List json2List(JSONArray jsonArray) throws JSONException {
        List resultList = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                value = json2Map((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = json2List((JSONArray) value);
            }
            resultList.add(value);
        }
        return resultList;
    }

    public static List<String> json2List(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        Iterator<String> iterator=jsonObject.keys();
        ArrayList<String> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(jsonObject.getString(iterator.next()));
        }
        return list;
    }
}
