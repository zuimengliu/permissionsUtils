package com.syrg.permissionlibrary.permissions;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.syrg.permissionlibrary.permissions.rom.HuaweiUtils;
import com.syrg.permissionlibrary.permissions.rom.MeizuUtils;
import com.syrg.permissionlibrary.permissions.rom.MiuiUtils;
import com.syrg.permissionlibrary.permissions.rom.OppoUtils;
import com.syrg.permissionlibrary.permissions.rom.QikuUtils;
import com.syrg.permissionlibrary.permissions.rom.RomUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 功能描述: 权限请求工具类
 * <p>
 * 作者: July
 * 日期: 2018/9/3 下午3:53
 */
public class PermissionUtils {

    private static final String TAG = "PermissionUtils";

    private PermissionUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    @SuppressWarnings("WeakerAccess")
    public static void requestPermission(@NonNull final RequestPermission requestPermission, @NonNull RxPermissions rxPermissions, String... permissions) {

        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("RxPermissions.request/requestEach requires at least one input permission");
        }

        List<String> needRequest = new ArrayList<>();
        // 过滤调已经申请过的权限
        for (String permission : permissions) {
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission);
            }
        }

        // 全部权限都已经申请过，直接执行操作
        if (needRequest.isEmpty()) {
            requestPermission.onSuccess();
            return;
        }

        // 没有申请过,则开始申请
        rxPermissions.requestEach(needRequest.toArray(new String[0]))
                .buffer(permissions.length)
                .subscribe(new Observer<List<Permission>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Permission> permissions) {
                        List<String> failurePermissions = new ArrayList<>();
                        List<String> askNeverAgainPermissions = new ArrayList<>();
                        for (Permission permission : permissions) {
                            if (!permission.granted) {
                                if (permission.shouldShowRequestPermissionRationale) {
                                    failurePermissions.add(permission.name);
                                } else {
                                    askNeverAgainPermissions.add(permission.name);
                                }
                            }
                        }
                        if (failurePermissions.size() > 0 || askNeverAgainPermissions.size() > 0) {
                            Log.d(TAG, "Request permissions failure => " + failurePermissions);
                            Log.d(TAG, "Request permissions failure with ask never again => " + askNeverAgainPermissions);
                            requestPermission.onFailure(failurePermissions, askNeverAgainPermissions);
                        }

                        if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0) {
                            Log.d(TAG, "Request permissions success");
                            requestPermission.onSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Request permissions error => " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 请求摄像头权限
     */
    public static void requestLaunchCamera(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CAMERA);
    }

    /**
     * 请求通讯录的权限
     */
    public static void requestReadContacts(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_CONTACTS);
    }


    /**
     * 请求外部存储的权限
     */
    public static void requestExternalStorage(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 请求打电话权限
     */
    public static void requestCallPhone(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.CALL_PHONE);
    }

    /**
     * 请求获取手机状态的权限
     */
    public static void requestReadPhonestate(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 请求定位权限
     */
    public static void requestLocation(@NonNull RxPermissions rxPermissions, @NonNull RequestPermission requestPermission) {
        requestPermission(requestPermission, rxPermissions, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public interface RequestPermission {

        /**
         * 权限请求成功
         */
        void onSuccess();

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param failurePermissions       请求失败的权限名
         * @param askNeverAgainPermissions 请求失败的权限名  不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         */
        void onFailure(List<String> failurePermissions, List<String> askNeverAgainPermissions);
    }

    /**
     * 跳转到权限设置界面
     *
     * @param context
     */
    public static void toPermissionSetting(Context context) throws NoSuchFieldException, IllegalAccessException {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                MiuiUtils.applyMiuiPermission(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                MeizuUtils.applyPermission(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                HuaweiUtils.applyPermission(context);
            } else if (RomUtils.checkIs360Rom()) {
                QikuUtils.applyPermission(context);
            } else if (RomUtils.checkIsOppoRom()) {
                OppoUtils.applyOppoPermission(context);
            } else {
                RomUtils.getAppDetailSettingIntent(context);
            }
        } else {
            if (RomUtils.checkIsMeizuRom()) {
                MeizuUtils.applyPermission(context);
            } else {
                if (RomUtils.checkIsOppoRom() || RomUtils.checkIsVivoRom()) {
                    RomUtils.getOppoVivoSettingIntent(context);
                } else if ( RomUtils.checkIsHuaweiRom() || RomUtils.checkIsSamsunRom()) {
                    RomUtils.getAppDetailSettingIntent(context);
                }else if (RomUtils.checkIsMiuiRom()) {
                    MiuiUtils.toPermisstionSetting(context);
                } else {
                    RomUtils.commonROMPermissionApplyInternal(context);
                }
            }
        }
    }

}
