package com.rkl.common_library.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by rkl on 2018/1/12.
 */
public class BasePresenter<V> {

    private static String TAG;

    private CompositeSubscription mCompositeSubscription;//管理订阅者

    /**
     * 将订阅者统一管理
     *
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    /**
     * 销毁订阅者
     * @param s
     */
    protected void removeSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            if(!s.isUnsubscribed()){
                s.unsubscribe();
            }
            this.mCompositeSubscription.remove(s);
        }
    }
    /**
     * 在view销毁时解除所有和当前Presenter绑定的订阅者
     */
    public void unsubcrible() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    private V view;

    public void attachView(V view) {
        this.view = view;
        TAG = getClass().getSimpleName();
    }

    public void detachView() {
        this.unsubcrible();
        this.view = null;
    }

    /**
     * 是否已经绑定view
     *
     * @return
     */
    public boolean isViewAttached() {
        return this.view != null;
    }

    /**
     * 获取当前已经绑定的view
     *
     * @return
     */
    public V getView() {
        return view;
    }

    /**
     * 检查是否已经绑定view，如果没有会抛出MvpViewNotAttachedException
     */
    public void chekViewAttached() {
        if (!isViewAttached())
            throw new BasePresenter.MvpViewNotAttachedException();
    }

/**
 * 自定义的未绑定view的异常
 */
public static class MvpViewNotAttachedException extends RuntimeException {
    public MvpViewNotAttachedException() {
        super("Please call Presenter.attachView(MvpView) before" +
                " requesting data to the Presenter");
        }

    }

}
