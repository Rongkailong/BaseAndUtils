package com.rkl.common_library.permission;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.rkl.common_library.permission.permissionconfig.AndPermission;
import com.rkl.common_library.permission.permissionconfig.Permission;
import com.rkl.common_library.permission.permissionconfig.PermissionNo;
import com.rkl.common_library.permission.permissionconfig.PermissionYes;
import com.rkl.common_library.permission.permissionconfig.Request;
import com.rkl.common_library.util.LogUtils;

import java.util.List;

/**
 * 主要功能：可适配Android8.0的开源权限库
 * Created by rkl on 2018/1/19.
 * 修改历史：
 */

public class PermissionUtil {

        private static final int PERMISSION_REQUESTCODE=110;//权限请求码110
        private PermissionCallback mCallback;
        private Request permissionRequest;

        public PermissionUtil(Object context, PermissionCallback callback) {
            this.mCallback = callback;
            setContext(context);
        }

        public void setContext(Object context) {
            if (context instanceof Activity){
                permissionRequest = AndPermission.with((Activity) context);
            }else if (context instanceof Context){
                permissionRequest =AndPermission.with((Context) context);
            }else if (context instanceof Fragment ){
                permissionRequest =AndPermission.with((Fragment) context);
            }
    }

        @PermissionYes(PERMISSION_REQUESTCODE)
        public void yes(List<String> permissions) {
            this.mCallback.onSuccessful();
        }

        @PermissionNo(PERMISSION_REQUESTCODE)
        public void no(List<String> permissions) {
            LogUtils.e("permissions",permissions.toString());
            this.mCallback.onFailure();
        }



    public interface PermissionCallback {
            void onSuccessful();

            void onFailure();
        }

    /**
     * 申请拍照权限
     */
        public void requestTakePhotoes(){
            permissionRequest.requestCode(PERMISSION_REQUESTCODE)
                    .permission(Permission.CAMERA,Permission.STORAGE)
                    .callback(this)
                    .start();
        }

    /**
     *申请获取位置权限
     */
    public void requestLocation() {
        permissionRequest.requestCode(PERMISSION_REQUESTCODE)
                .permission(Permission.PHONE, Permission.LOCATION)
                .callback(this)
                .start();
    }


}
