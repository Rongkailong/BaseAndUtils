package com.rkl.common_library.manager;
import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 主要功能：Activity管理器
 * Created by rkl on 2018/1/11.
 * 修改历史：
 */

public class ActivityManager {

    private static ActivityManager mActivityManager;

    private Stack<Activity> mActivityStack;

    private ActivityManager(){
        mActivityStack = new Stack<Activity>();
    }

    /**
     * 保证activityManager的唯一性
     */
    public static ActivityManager getInstance(){
        if (mActivityManager==null){
            synchronized (ActivityManager.class){
                if (mActivityManager==null){
                    return new ActivityManager();
                }
            }
        }
        return mActivityManager;
    }

    /**
     * 把activity加入栈中
     */
    public void addActivity(Activity activity){
        mActivityStack.push(activity);
    }

    /**
     * 移除指定的activity
     */
    public void removeActivity(Activity activity){
        mActivityStack.remove(activity);
    }

    /**
     * 移除当前activity
     */
    public void removeActivity(){
        mActivityStack.pop();
    }

    /**
     * @param index
     * 根据下标获取对应的activity
     */
    public Activity getActivity(int index){
        return mActivityStack.get(index);
    }

    /**
     * @return 当前activity的数量
     */
    public int getCount(){
        return mActivityStack.size();
    }

    /**
     * 关闭所有的activity
     */
    public void finishAll(){
        for(Activity activity : mActivityStack){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        clearAll();
    }

    private void clearAll(){
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        //杀死进程,用来保存统计数据
//        MobclickagentHelper.getInstance().onKillProcess(context);//友盟请放开注释
        try {
            finishAll();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}

