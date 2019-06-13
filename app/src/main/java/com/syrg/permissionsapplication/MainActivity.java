package com.syrg.permissionsapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.syrg.permissionsapplication.dialog.DialogHelper;
import com.syrg.permissionsapplication.dialog.RxDialogEditSureCancel;
import com.syrg.permissionsapplication.dialog.RxDialogLoading;
import com.syrg.permissionsapplication.dialog.RxDialogSureCancel;
import com.syrg.permissionsapplication.permissions.PermissionUtils;
import com.syrg.permissionsapplication.permissions.RxPermissions;

import java.util.List;

import static android.R.attr.dial;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mContext;
    private Dialog rxDialogLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button button_DialogSureCancle = (Button) findViewById(R.id.button_DialogSureCancle);
        Button button_DialogEditTextSureCancle = (Button) findViewById(R.id.button_DialogEditTextSureCancle);
        Button button_DialogLoadingspinkit = (Button) findViewById(R.id.button_DialogLoadingspinkit);
        button_DialogSureCancle.setOnClickListener(this);
        button_DialogEditTextSureCancle.setOnClickListener(this);
        button_DialogLoadingspinkit.setOnClickListener(this);

    }

    private void requestUsePermissions() {
        PermissionUtils.requestExternalStorage(getRxPermissions(), new PermissionUtils.RequestPermission() {
            @Override
            public void onSuccess() {
                DialogHelper.getSureCancelDialog(mContext, "温馨提示", "已获取权限.", "确定", "取消", false, null ).show();
            }

            @Override
            public void onFailure(List<String> failurePermissions, List<String> askNeverAgainPermissions) {

                DialogHelper.getSureCancelDialog(mContext, "温馨提示", "没有权限, 您需要去设置中开启读取手机存储权限.", "去设置", "取消",
                        false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (DialogInterface.BUTTON_POSITIVE==which){
                                    try {
                                        PermissionUtils.toPermissionSetting(MainActivity.this);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } ).show();
            }
        });
    }

    private RxPermissions mRxPermissions;

    public synchronized RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        return mRxPermissions;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_DialogSureCancle:
                requestUsePermissions();
                break;
            case R.id.button_DialogEditTextSureCancle:
                DialogHelper.getEditSureCancelDialog(mContext, "温馨提示", "确定", "取消", false, new DialogHelper.OnEditDialogClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, String editContent) {
                        if (DialogInterface.BUTTON_POSITIVE == which){
                            Toast.makeText(mContext,editContent,Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
                break;
            case R.id.button_DialogLoadingspinkit:
                rxDialogLoading = DialogHelper.getLoadingDialog(mContext,true);
                rxDialogLoading.show();
                handler.sendEmptyMessageDelayed(88,2000);
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 88){
                if (rxDialogLoading!=null&&rxDialogLoading.isShowing()){
                    rxDialogLoading.cancel();
                }
            }
        }
    };
}
