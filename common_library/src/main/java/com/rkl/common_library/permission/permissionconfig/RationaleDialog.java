/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rkl.common_library.permission.permissionconfig;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

/**请自定义
 * <p>Default Rationale Dialog.</p>
 * Created by Yan Zhenjie on 2016/12/28.
 * Android运行时权限有一个特点，在拒绝过一次权限后，再此申请该权限，在申请框会多一个不再提示的复选框，当用户勾选了不再提示并拒绝了权限后，下次再申请该权限将直接回调申请失败。
 * 因此Rationale功能是在用户拒绝一次权限后，再次申请时检测到已经申请过一次该权限了，允许开发者弹窗说明申请权限的目的，获取用户的同意后再申请权限，避免用户勾选不再提示，导致不能再次申请权限。
 *
 *
 */
public class RationaleDialog {

    private AlertDialog.Builder mBuilder;
    private Rationale mRationale;

    RationaleDialog(@NonNull Context context, @NonNull Rationale rationale) {
        this.mRationale = rationale;
        mBuilder=new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("权限申请")
                .setMessage("拒绝后可能影响正常使用哦！")
                .setPositiveButton("确认申请",mClickListener)
                .setNegativeButton("依然拒绝",mClickListener);
    }

    @NonNull
    public RationaleDialog setTitle(@NonNull String title) {
        mBuilder.setTitle(title);
        return this;
    }

    @NonNull
    public RationaleDialog setTitle(@StringRes int title) {
        mBuilder.setTitle(title);
        return this;
    }

    @NonNull
    public RationaleDialog setMessage(@NonNull String message) {
        mBuilder.setMessage(message);
        return this;
    }

    @NonNull
    public RationaleDialog setMessage(@StringRes int message) {
        mBuilder.setMessage(message);
        return this;
    }

    @NonNull
    public RationaleDialog setNegativeButton(@NonNull String text, @Nullable DialogInterface.OnClickListener negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return this;
    }

    @NonNull
    public RationaleDialog setNegativeButton(@StringRes int text, @Nullable DialogInterface.OnClickListener negativeListener) {
        mBuilder.setNegativeButton(text, negativeListener);
        return this;
    }

    @NonNull
    public RationaleDialog setPositiveButton(@NonNull String text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return this;
    }

    @NonNull
    public RationaleDialog setPositiveButton(@StringRes int text) {
        mBuilder.setPositiveButton(text, mClickListener);
        return this;
    }

    public void show() {
        mBuilder.show();
    }

    /**
     * The dialog's btn click listener.
     */
    private DialogInterface.OnClickListener mClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    mRationale.cancel();
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    mRationale.resume();
                    break;
            }
        }
    };

}
