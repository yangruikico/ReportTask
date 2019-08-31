package com.yrbase.soulpermission.request.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yrbase.soulpermission.Constants;
import com.yrbase.soulpermission.PermissionTools;
import com.yrbase.soulpermission.bean.Permission;
import com.yrbase.soulpermission.bean.Special;
import com.yrbase.soulpermission.callbcak.GoAppDetailCallBack;
import com.yrbase.soulpermission.callbcak.RequestPermissionListener;
import com.yrbase.soulpermission.callbcak.SpecialPermissionListener;
import com.yrbase.soulpermission.checker.SpecialChecker;
import com.yrbase.soulpermission.debug.PermissionDebug;
import com.yrbase.soulpermission.request.IPermissionActions;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author cd5160866
 */
public class PermissionFragment extends Fragment implements IPermissionActions {

    private static final String TAG = PermissionFragment.class.getSimpleName();

    private Special specialToRequest;

    private RequestPermissionListener runtimeListener;

    private SpecialPermissionListener specialListener;

    private GoAppDetailCallBack goAppDetailCallBack;

    @TargetApi(M)
    @Override
    public void requestPermissions(String[] permissions, RequestPermissionListener listener) {
        this.runtimeListener = listener;
        requestPermissions(permissions, Constants.REQUEST_CODE_PERMISSION);
    }

    @TargetApi(M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission[] permissionResults = new Permission[permissions.length];
        //some specific rom will provide a null array
        if (null == permissionResults || null == grantResults) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < permissions.length; ++i) {
                Permission permission = new Permission(permissions[i], grantResults[i], this.shouldShowRequestPermissionRationale(permissions[i]));
                permissionResults[i] = permission;
            }
        }
        if (null != runtimeListener && PermissionTools.isActivityAvailable(getActivity())) {
            runtimeListener.onPermissionResult(permissionResults);
        }
    }

    @Override
    public void requestSpecialPermission(Special permission, SpecialPermissionListener listener) {
        this.specialListener = listener;
        this.specialToRequest = permission;
        Intent intent = PermissionTools.getSpecialPermissionIntent(getActivity(), specialToRequest);
        if (null == intent) {
            PermissionDebug.w(TAG, "create intent failed");
            return;
        }
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_PERMISSION_SPECIAL);
        } catch (Exception e) {
            e.printStackTrace();
            PermissionDebug.e(TAG, e.toString());
        }
    }

    @Override
    public void goAppDetail(@Nullable GoAppDetailCallBack callBack) {
        this.goAppDetailCallBack = callBack;
        Intent intent = PermissionTools.getAppManageIntent(getActivity());
        if (null == intent) {
            PermissionDebug.w(TAG, "create intent failed");
            return;
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_APPLICATION_SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = getActivity();
        if (!PermissionTools.isActivityAvailable(activity)) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_SPECIAL && null != specialToRequest && null != specialListener) {
            boolean result = new SpecialChecker(activity, specialToRequest).check();
            if (result) {
                specialListener.onGranted(specialToRequest);
            } else {
                specialListener.onDenied(specialToRequest);
            }
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_APPLICATION_SETTINGS && null != goAppDetailCallBack) {
            goAppDetailCallBack.onBackFromAppDetail(data);
        }
    }
}
