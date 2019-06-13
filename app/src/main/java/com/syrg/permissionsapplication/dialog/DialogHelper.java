package com.syrg.permissionsapplication.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.syrg.permissionsapplication.R;

/**
 * Created by liujie on 2019/6/11.
 */

public class DialogHelper {
    public static Dialog getSureCancelDialog(Context mContext, String title, String concent, String sure, String cancle,
                                             boolean isCancle, final DialogInterface.OnClickListener onClickListener) {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);
        rxDialogSureCancel.getTitleView().setText(title);
        rxDialogSureCancel.getContentView().setText(concent);
        rxDialogSureCancel.getSureView().setText(sure);
        rxDialogSureCancel.getCancelView().setText(cancle);
        rxDialogSureCancel.setCanceledOnTouchOutside(isCancle);
        rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null)
                    onClickListener.onClick(rxDialogSureCancel,DialogInterface.BUTTON_POSITIVE);
                rxDialogSureCancel.cancel();
            }
        });
        rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null)
                    onClickListener.onClick(rxDialogSureCancel,DialogInterface.BUTTON_NEGATIVE);
                rxDialogSureCancel.cancel();

            }
        });
        return rxDialogSureCancel;
    }

    public static Dialog getLoadingDialog(Context mContext, boolean isCancle) {
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
        rxDialogLoading.setCancelable(isCancle);
        return rxDialogLoading;
    }


    public static Dialog getEditSureCancelDialog(final Context mContext, String title, String sure, String cancle,
                                             boolean isCancle, final OnEditDialogClickListener onClickListener) {
        final RxDialogEditSureCancel rxDialogEditSureCancel = new RxDialogEditSureCancel(mContext);
        rxDialogEditSureCancel.getTitleView().setText(title);
        rxDialogEditSureCancel.getSureView().setText(sure);
        rxDialogEditSureCancel.getCancelView().setText(cancle);
        rxDialogEditSureCancel.setCanceledOnTouchOutside(isCancle);
//                rxDialogEditSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
        rxDialogEditSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editText = rxDialogEditSureCancel.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(editText)){
                    Toast.makeText(mContext,"请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onClickListener!=null){
                    onClickListener.onClick(rxDialogEditSureCancel,DialogInterface.BUTTON_POSITIVE,editText);
                }
                rxDialogEditSureCancel.cancel();


            }
        });
        rxDialogEditSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener!=null){
                    onClickListener.onClick(rxDialogEditSureCancel,DialogInterface.BUTTON_NEGATIVE,null);
                }
                rxDialogEditSureCancel.cancel();
            }
        });
        return rxDialogEditSureCancel;
    }


    public  interface OnEditDialogClickListener {

        void onClick(DialogInterface dialog, int which,String editContent);
    }

}
