package com.rkl.common_library.util;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shy on 2016/9/23.
 */
public class EmptyUtils {

    /**
     * 判断list列表是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 判断list集合是否全部为空
     * @param lists
     * @return
     */
    public static boolean isAllEmpty(List... lists) {
        for (List list : lists) {
            if (!isEmpty(list)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断list集合是否有空元素
     * @param lists
     * @return
     */
    public static boolean isOneEmpty(List... lists) {
        for (List list : lists) {
            if (isEmpty(list)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || TextUtils.isEmpty(s);
    }

    /**
     * 判断string数组是否是都为空
     * @param strings
     * @return
     */
    public static boolean isAllEmpty(String... strings) {
        for (String s : strings) {

            if (!isEmpty(s)) {
                //如果有一个不为空则返回false.
                return false;
            }
        }
        return true;
    }
    /**
     * 判断string数组是否有空元素
     * @param strings
     * @return
     */
    public static boolean isOneEmpty(String... strings) {
        for (String s : strings) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查一组TextView是否有空数据
     * @param tvs
     * @return
     */
    public static boolean isOneEmptyInTextView(TextView... tvs){
        if(tvs==null){
            return true;
        }
        for(TextView t:tvs){
            if(isEmpty(t.getText().toString().trim())){
                return  true;
            }
        }
        return false;
    }

    /**
     * 判断Object集合中是否有空元素
     * @param objects
     * @return
     */
    public static Boolean compareWithNull(Object... objects){
        for (Object object:objects){
            if(object == null)
                return true;
        }
        return false;
    }
}
