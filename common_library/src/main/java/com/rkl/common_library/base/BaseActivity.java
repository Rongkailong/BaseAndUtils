package com.rkl.common_library.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rkl.common_library.R;
import com.rkl.common_library.manager.ActivityManager;
import com.rkl.common_library.util.LogUtils;
import com.rkl.common_library.util.StatusBarUtil;
import com.zhy.autolayout.AutoLayoutActivity;


/**
 * 主要功能：activity基类
 * Created by rkl on 2018/1/11.
 * 修改历史：
 */

public abstract class BaseActivity<P extends BasePresenter> extends AutoLayoutActivity {
    //当前类名，通用TAG
    protected static String TAG;

    //当前的上下文对象
    protected Context context;

    //当前activity的Presenter
    protected P presenter;



    /** 是否支持沉浸状态栏 **/
    protected boolean isAllowStatusBar = true;
    /** 是否允许全屏 **/
    protected boolean isAllowFullScreen = false;
    /** 是否允许旋转屏幕 **/
    protected boolean isAllowScreenRoate = false;


    //标题栏
    private RelativeLayout mTitleContent;
    private TextView mTitleName;
    private TextView mRightContentName;
    private ImageView mBackIcon;
    private ImageView mRightShareIcon;
    protected LinearLayout mRootContent;
    protected View childContent;
    protected LayoutInflater inflater;

    //布局文件ID
    protected abstract int getContentViewId();

    //设置状态栏、全屏、旋转
    protected  void setStyleForStatusBarScreenRoate(){}

    //初始化Presenter
    protected abstract P createPresenter ();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        TAG = this.getClass().getSimpleName();

        //设置无标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //适配软键盘弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置标题栏
        setContentView(R.layout.common_titlebar);
        mTitleContent = findView(R.id.rl_title_content);
        mTitleName = findView(R.id.tv_title_name);
        mRightContentName = findView(R.id.tv_title_right);
        mBackIcon = findView(R.id.iv_title_back);
        mRightShareIcon = findView(R.id.iv_share);
        mRootContent =  findView(R.id.content);
        //添加子布局
        if (getContentViewId() != 0) {
            mRootContent.removeAllViews();
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            childContent= inflater.inflate(getContentViewId(), null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mRootContent.removeAllViews();
            mRootContent.addView(childContent, params);
        }
        //设置状态栏、全屏、旋转
        setStyleForStatusBarScreenRoate();
        setAllowStatusBar();
        setAllowScreenRoate();
        //获取Presenter
        presenter =createPresenter();
        registerBus();
        //把当前activity加入activityManager方便管理
        ActivityManager.getInstance().addActivity(this);
    }

    /**
     * 如果要用到事件总线时，在这里注册
     * RxBus.get().register(this);
     * 注册了，那么必须取消
     */
    protected  void registerBus(){
        if (presenter!=null){
            presenter.attachView(this);
        }
    }

    /**
     * 取消RxBus
     * RxBus.get().unregister(this);
     * Presenter解除绑定的view也可以在这里
     */
    protected  void unregisterBus(){
        if (presenter!=null){
            presenter.detachView();
        }
    }



    /**
     * 是否支持沉浸式状态栏
     */
    public void setAllowStatusBar() {
        if (isAllowFullScreen){//全屏
            if (isAllowStatusBar){
                //全屏但是有状态栏的Activity
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
                StatusBarUtil.setColor(this, getResources().getColor(R.color.transparency), StatusBarUtil.MIN_STATUS_BAR_ALPHA);
            }else {
                //全屏且无状态栏的Activity
                StatusBarUtil.setTranslucent(this, StatusBarUtil.MIN_STATUS_BAR_ALPHA);
            }
        }else {
            if (isAllowStatusBar){
                //程序中一般沉浸式状态栏Activity
                StatusBarUtil.setColor(this, getResources().getColor(R.color.app_color), StatusBarUtil.MIN_STATUS_BAR_ALPHA);
            }
        }

    }


    /**
     * 是否支持屏幕旋转
     */
    public void setAllowScreenRoate() {
        if (isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }



    /**
     * 是否显示标题栏及标题栏高度
     */
    protected void setTitleContentVisible(boolean isVisible) {
        if (isVisible) {
            mTitleContent.setVisibility(View.VISIBLE);
        } else {
            mTitleContent.setVisibility(View.GONE);
        }
    }


    /**
     * 获取标题栏对象
     */
    public RelativeLayout getTitleContentView() {
        return mTitleContent;
    }

    /**
     * 是否显示返回键及其监听
     */
    protected void setBackVisible(boolean isVisible, View.OnClickListener listener) {
        if (isVisible) {
            mBackIcon.setVisibility(View.VISIBLE);
            mBackIcon.setOnClickListener(listener);
        } else {
            mBackIcon.setVisibility(View.GONE);
        }
    }
    /**
     * 设置标题名称字号以及字体颜色
     */
    protected void setTitleName(String titleName, float titleSize, int titleColor) {
        mTitleName.setText(titleName);
        mTitleName.setTextSize(titleSize);
        mTitleName.setTextColor(getResources().getColor(titleColor));
    }
    /**
     * 设置标题名称字号以及字体颜色
     */
    protected void setTitleName(String titleName) {
        mTitleName.setText(titleName);
        mTitleName.setTextSize(R.dimen.common_title_textsize);
        mTitleName.setTextColor(getResources().getColor(R.color.white));
    }
    /**
     * 显示分享图标,以及点击事件监听
     */
    protected void setShareVisible(boolean isVisible, View.OnClickListener listener) {
        if (isVisible) {
            mRightShareIcon.setVisibility(View.VISIBLE);
            mRightShareIcon.setOnClickListener(listener);
        } else {
            mRightShareIcon.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边副标题名称字号以及字体颜色
     */
    protected void setRightContentName(String titleName, float titleSize, int titleColor) {
        mRightContentName.setText(titleName);
        mRightContentName.setTextSize(titleSize);
        mRightContentName.setTextColor(getResources().getColor(titleColor));
    }

    /**
     * 设置右边副标题显示状态
     */
    protected void setRightContentVisibility(boolean isShow) {
        mRightContentName.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题栏右侧text文本的点击事件
     *
     * @param onclick
     */
    protected void setRightTextOnclick(View.OnClickListener onclick) {
        mRightContentName.setOnClickListener(onclick);
    }



    /**findViewById的封装
     * @param id 控件id
     * @param <T>返回的类型
     * @return
     */
    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    /**防止快速点击
     * @return
     */
    private boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    /**
     * 快捷跳转到另一个Activity，可传递数据
     *
     * @param activityClass 目标
     * @param bundle        数据
     */
    public void goToActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    /**
     * 快捷跳转到另一个Activity
     *
     * @param activityClass 目标
     */
    public void goToActivity(Class activityClass) {
        if (activityClass != null) {
            goToActivity(activityClass, null);
        }
    }

    /**
     * 快捷跳转到另一个Activity，可传递数据
     * @param activityClass  目标
     * @param bundle  数据
     */
    public void goToActivityForResult(Class activityClass, Bundle bundle, int resquestCode){
        Intent intent=new Intent(this,activityClass);
        if(bundle!=null){
            intent.putExtra("data",bundle);
        }
        startActivityForResult(intent, resquestCode);
    }
    /**
     * 快捷跳转到另一个Activity
     * @param activityClass  目标
     */
    public void goToActivityForResult(Class activityClass, int resquestCode){
        goToActivityForResult(activityClass, null, resquestCode);
    }

    /**
     * 获取Bundle实例
     */
    public Bundle getData() {
        return this.getIntent().getBundleExtra("data");
    }


    /**
     * 显示软键盘
     */
    public void showInputMethod(){
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),0);
            }
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * 设置 点击edittext以外的区域收起键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && v != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterBus();
        }catch (Exception e){
            LogUtils.e("程序","取消注册失败："+e.getStackTrace().toString());
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        ActivityManager.getInstance().removeActivity(this);
    }
}
