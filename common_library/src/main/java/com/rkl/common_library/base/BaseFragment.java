package com.rkl.common_library.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.rkl.common_library.R;
import com.rkl.common_library.util.LogUtils;
import com.rkl.common_library.util.TimeUtils;
import com.rkl.common_library.util.ToastUtils;

import java.util.Date;
/**
 * 主要功能：Fragment基类
 * Created by rkl on 2018/1/11.
 * 修改历史：
 */

public abstract class BaseFragment extends Fragment {
    public static String TAG;
    protected BaseActivity mActivity;
    private View mViewContent;//缓存视图内容
    private ProgressDialog pDialog;
    private View rootView;
    private FrameLayout rootlayout;

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    private boolean hasCreateView;

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private boolean isFragmentVisible;

    protected abstract void initView(View view, Bundle savedInstanceState);
    //获取fragment布局文件ID
    protected abstract int getLayoutId();

    //获取宿主Activity
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }


    /**
     * 如果要用到事件总线时，在这里注册
     * RxBus.get().register(this);
     * 注册了，那么必须取消
     */
    protected  void registerBus(){};

    /**
     * 取消RxBus
     * RxBus.get().unregister(this);
     * Presenter解除绑定的view也可以在这里
     */
    protected  void unregisterBus(){};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
        TAG = this.getClass().getSimpleName();
        registerBus();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=LayoutInflater.from(getHoldingActivity()).inflate(R.layout.common_fragment_root, container, false);
        rootlayout= (FrameLayout) rootView.findViewById(R.id.fl_root_view);
        if(mViewContent==null){
            rootlayout.removeAllViews();
            mViewContent = inflater.inflate(getLayoutId(), container, false);
            rootlayout.addView(mViewContent);
        }
        initView(mViewContent, savedInstanceState);
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mViewContent == null) {
            return;
        }
        hasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
    }
    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {
    }
    @Override
    public void onDestroyView() {
        try {
            unregisterBus();
        }catch (Exception e){
            LogUtils.e("程序","取消注册失败："+e.getStackTrace().toString());
        }
        super.onDestroyView();

    }

    /**
     * 便于子类显示吐司
     * @param str
     */
    public void showToast(String str){
        ToastUtils.showShortToast(getHoldingActivity(),str);
    }
    public void showProgress(String content){
        pDialog= new ProgressDialog(getHoldingActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage(content);
        pDialog.show();
    }
    public void dismissProgress(){
        if(pDialog!=null){
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }


    /**
     * 快捷跳转到另一个Activity，可传递数据
     * @param activityClass  目标
     * @param bundle  数据
     */
    public void goToActivity(Class activityClass,Bundle bundle){
        Intent intent=new Intent(getHoldingActivity(),activityClass);
        if(bundle!=null){
            intent.putExtra("data",bundle);
        }
        startActivity(intent);
    }
    /**
     * 快捷跳转到另一个Activity
     * @param activityClass  目标
     */
    public void goToActivity(Class activityClass){
        goToActivity(activityClass,null);
    }

    /**
     * 快捷跳转到另一个Activity，可传递数据
     * @param activityClass  目标
     * @param bundle  数据
     */
    public void goToActivityForResult(Class activityClass, Bundle bundle, int resquestCode){
        Intent intent=new Intent(getHoldingActivity(),activityClass);
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
     * 防抖动操作
     * @return
     */
    public boolean fastClick(){
        return TimeUtils.continuousClick((new Date()).getTime());
    }
    public Context getContext() {
        return getHoldingActivity();
    }

    public View getRootView() {
        return mViewContent;
    }


}
