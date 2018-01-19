package com.tunjin.rkl.baseandutils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.rkl.common_library.base.BaseActivity;
import com.rkl.common_library.base.BasePresenter;

public class MainActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




}
