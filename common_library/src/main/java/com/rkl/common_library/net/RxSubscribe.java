package com.rkl.common_library.net;
import android.net.ParseException;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.rkl.common_library.R;
import com.rkl.common_library.base.BaseApplication;
import com.rkl.common_library.constant.ServerException;
import com.rkl.common_library.util.ToastUtils;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import rx.Subscriber;

/**
 * Created by ArmGlobe on 2016/9/22.
 * 对一些结果进行一些统一的处理
 */
public abstract class RxSubscribe<T> extends Subscriber<T> {
    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //获取根源 异常
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        Logger.e("网络状态码："+NetworkStateService.networkStatus+"错误信息："+e.getMessage()+"    "+e.getCause());
        if(!NetworkStateService.isNetworkAvailable(BaseApplication.getmAppContext())){
            ToastUtils.showShortToast(BaseApplication.getmAppContext(),BaseApplication.getmAppContext().getResources().getString(R.string.net_error));
        }else if (e instanceof ServerException) {//服务器返回的错误
            _onError((ServerException) e);
        } else if (e instanceof JsonParseException
                || e instanceof ParseException) {//解析异常
            ToastUtils.showShortToast(BaseApplication.getmAppContext(),BaseApplication.getmAppContext().getResources().getString(R.string.data_parse_error));
        } else if (e instanceof UnknownHostException) {
            ToastUtils.showShortToast(BaseApplication.getmAppContext(), BaseApplication.getmAppContext().getResources().getString(R.string.server_unknow_error));
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShortToast(BaseApplication.getmAppContext(), BaseApplication.getmAppContext().getResources().getString(R.string.time_out_error));
        } else {
            ToastUtils.showShortToast(BaseApplication.getmAppContext(), BaseApplication.getmAppContext().getResources().getString(R.string.request_error)+"\n错误信息："+e.getMessage());
            e.printStackTrace();
        }

    }

    protected abstract void _onNext(T t);
    protected abstract void _onError(ServerException e);
}
