package com.rkl.common_library.util;

import android.widget.EditText;

/**
 * Created by shy on 2017/3/2.
 */
public class EdtUtil {

      public static boolean isEdtEmpty(EditText editText) {
        if (editText == null) {
            return true;
        }
        String text = editText.getText().toString().trim();
        return EmptyUtils.isEmpty(text);
    }

    /**
     * 检查这些EditText是否有空的
     * @param editTexts
     * @return
     */
    public static boolean isHaveEdtEmpty(EditText...editTexts) {
        for (EditText editText:editTexts) {
            if (editText == null) {
                return true;
            }
            String text = editText.getText().toString().trim();
            if(EmptyUtils.isEmpty(text)){
                return true;
            }
        }
        return false;
    }

    public static String getEdtText(EditText editText) {
        if (editText == null) {
            return "";
        }
        String text = editText.getText().toString().trim();
        return text;
    }
}
