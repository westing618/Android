package com.ztd.yyb.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.order.PaymentActivity;
import com.ztd.yyb.activity.order.PaymentSuccessfulActivity;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.SPUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WENXIN_APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResp(BaseResp resp) {

        //	errCode  0	成功	  展示成功页面  -2	用户取消
        // -1错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (resp.errCode == 0) {

                String jiajia = (String) SPUtil.get("JIAJIA", "");

                if(jiajia.equals("0")){//加价的支付
                    new SweetAlertDialog(WXPayEntryActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("加价成功")
//                                    .setContentText("Won't be able to recover this file!")
//                                    .setConfirmText("Yes,delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    finish();
                                }
                            })
                            .show();

                }else {

                    startActivity(new Intent(WXPayEntryActivity.this, PaymentSuccessfulActivity.class));

                    finish();
                }


            } else if (resp.errCode == -2) {

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("用户已取消支付")
//                        .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();

                                finish();
                            }
                        })
                        .show();



            }else if (resp.errCode == -1) {

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("支付失败")
//                        .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();

            }


        }


    }




}