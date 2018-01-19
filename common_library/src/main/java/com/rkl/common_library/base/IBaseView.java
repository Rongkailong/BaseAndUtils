package com.rkl.common_library.base;

import android.content.Context;

/**
 * Created by rkl on 2018/1/12.
 * 需常用的view方法
 */

public interface IBaseView {
    Context getContext();
    void failed(String msg);
}
