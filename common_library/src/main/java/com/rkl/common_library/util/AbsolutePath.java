package com.rkl.common_library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hzh on 2017/3/22.
 * 获取本地路径下的图片路径
 * 如:shareSDK中 分享 setimagePath
 */

public class AbsolutePath{

    /**
     * @param context   上下文
     * @param resid     本地资源文件id
     * @param filename  要加载的图片名称()
     * @return 图片路径
     */
    public static String GetAbsolutePathfromshare(Context context, int resid, String filename){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
        File appDir = new File(Environment.getExternalStorageDirectory(), filename);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(appDir, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String absolutePath = file.getAbsolutePath();
        return absolutePath;

    }
}
