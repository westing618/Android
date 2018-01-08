package com.ztd.yyb.activity.uppaypw;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanUpPaypw.Datauppay;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.view.mypwdinputlibrary.InputPwdView;
import com.ztd.yyb.view.mypwdinputlibrary.MyInputPwdUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.R.id.tv_top_right;
import static com.ztd.yyb.activity.LoginActivity.USERPHONE;

/**
 * 修改支付密码
 * Created by  on 2017/4/5.
 */

public class UpdataPayPww extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_perpaw)
    TextView tv_perpaw;

    @BindView(R.id.tv_perpawps)
    TextView tv_perpawps;

    @BindView(tv_top_right)
    TextView mTvTopRight;

    private MyInputPwdUtil myInputPwdUtil;

    String type;//1  2
    String pwone;//1  2
    String pwtwo;//1  2

    private Map<String, String> mMessageMap = new HashMap<>();

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("修改支付密码");
        mTvTopRight.setVisibility(View.VISIBLE);
        mTvTopRight.setText("保存");


        myInputPwdUtil = new MyInputPwdUtil(this);
        myInputPwdUtil.getMyInputDialogBuilder().setAnimStyle(R.style.dialog_anim);
        myInputPwdUtil.setListener(new InputPwdView.InputPwdListener() {
            @Override
            public void hide() {
                myInputPwdUtil.hide();
            }

            @Override
            public void forgetPwd() {
//                Toast.makeText(mContext, "忘记密码", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void finishPwd(String pwd) {

                if(type.equals("1")){
                    tv_perpaw.setText(pwd);
                    pwone=pwd;
                }else if(type.equals("2")){
                    pwtwo=pwd;
                    tv_perpawps.setText(pwd);
                }

                myInputPwdUtil.hide();

            }
        });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_updatapayy;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.tv_top_right,R.id.tv_perpaw,R.id.tv_perpawps})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;

            case R.id.tv_top_right:

            if(!TextUtils.isEmpty(pwone) && !TextUtils.isEmpty(pwtwo)){

                if(pwone.equals(pwtwo)){

                    updata();

                } else {
                    ToastUtil.show(mContext,"2次密码输入不一样，请重新输入!");
                }

            }


                break;
            case R.id.tv_perpaw:
                type="1";
                myInputPwdUtil.show();
                break;
            case R.id.tv_perpawps:
                type="2";
                myInputPwdUtil.show();
                break;
        }
    }

    private void updata() {


        String pNumber = (String) SPUtil.get(USERPHONE, "");
        mMessageMap.clear();
        mMessageMap.put("usertel",pNumber);
        mMessageMap.put("pwd",pwone);
        mMessageMap.put("type","0");//type=0 更新支付密码 ，type=1验证密码

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.UPDATEPAYPW_URL, null, mMessageMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(String s) {//{"msg":"请输入正确的手机号","success":"0"}

                        dismissLoadingDialog();

                        if(!TextUtils.isEmpty(s)){

                            Gson gson=new Gson();

                            Datauppay datauppay = gson.fromJson(s, Datauppay.class);

                            if(datauppay.getData().getSuccess().equals("1")){

                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("修改成功!")
//                                .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();

                            }else {

                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("修改成功!")
//                                .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
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
                });

    }

}
