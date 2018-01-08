package com.ztd.yyb.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hss01248.lib.MyDialogListener;
import com.hss01248.lib.StytledDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.uppaypw.UpdataPayPw;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by  on 2017/3/20.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("设置");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_setting;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.btn_exit, R.id.ll_persondata, R.id.ll_security, R.id.ll_myrenzh, R.id.ll_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_persondata:
                startActivity(new Intent(this, PersonalDataActivity.class));
                break;
            case R.id.ll_security:
//                startActivity(new Intent(this, UpdataPayPw.class));

                Intent intent = new Intent(mContext, UpdataPayPw.class);
                intent.putExtra("FLAG", "1");
                startActivity(intent);

                break;
            case R.id.ll_myrenzh:
                startActivity(new Intent(this, MyCertification.class));
                break;

            case R.id.ll_about://

                startActivity(new Intent(this, AboutActivity.class));

                break;

            case R.id.btn_exit:


//                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                        .setTitleText("确定退出用工贝?")
////                    .setContentText("Won't be able to recover this file!")
//                        .setConfirmText("确定!")
//                        .setCancelText("取消")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//
//
//                                SPUtil.put("IS_LOGIN","0");
//
////                                EMClient.getInstance().logout(false);
//
//                                EMClient.getInstance().logout(true, new EMCallBack() {
//
//                                    @Override
//                                    public void onSuccess() {
//                                        Log.e("main", "退出聊天服务器成功");
//                                    }
//                                    @Override
//                                    public void onProgress(int progress, String status) {
//                                    }
//
//                                    @Override
//                                    public void onError(int code, String message) {
//                                        Log.e("main", "退出聊天服务器失败"+message+"code"+code);
//                                    }
//                                });
//
//
//                                sDialog.dismissWithAnimation();
//                                startActivity(new Intent(mContext, HomeActivity.class));
//
////                                Intent exit = new Intent(Intent.ACTION_MAIN);
////                                exit.addCategory(Intent.CATEGORY_HOME);
////                                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(exit);
////                                System.exit(0);
//
//                            }
//                        })
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.cancel();
//                            }
//                        })
//                        .show();


                StytledDialog.showIosAlert(this, "温馨提示", "是否确认退出登录", "取消", "确定", "", true, true, new MyDialogListener() {
                    @Override
                    public void onFirst(DialogInterface dialog) {
//                        showToast("onFirst");


                    }

                    @Override
                    public void onSecond(DialogInterface dialog) {
//                        showToast("onSecond");


                        SPUtil.put("IS_LOGIN", "0");

//                      EMClient.getInstance().logout(false);

                        EMClient.getInstance().logout(true, new EMCallBack() {

                            @Override
                            public void onSuccess() {
                                Log.e("main", "退出聊天服务器成功");
                            }

                            @Override
                            public void onProgress(int progress, String status) {
                            }

                            @Override
                            public void onError(int code, String message) {
                                Log.e("main", "退出聊天服务器失败" + message + "code" + code);
                            }
                        });


                        startActivity(new Intent(mContext, HomeActivity.class));
                    }

                    @Override
                    public void onThird(DialogInterface dialog) {
//                        showToast("onThird");
                    }


                });


                break;

        }
    }
}
