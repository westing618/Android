package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.evenbean.BankCardEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 提现详情
 * Created by  on 2017/5/3.cd
 */

public class WithdrawalDetail extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_withone)
    TextView tv_withone;

    @BindView(R.id.tv_withtwo)
    TextView tv_withtwo;

    @BindView(R.id.tv_withthress)
    TextView tv_withthress;

    private Map<String, String> mdrawalMap = new HashMap<>();

    String ygbmcard;
    String ygbchargeamount;
    String ygbramount;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("提现详情");

         ygbchargeamount =   getIntent().getStringExtra("ygbchargeamount");//手续费
         ygbramount      =   getIntent().getStringExtra("ygbramount");//提现金额
        String ygbbankinfo     =   getIntent().getStringExtra("ygbbankinfo");//王大哥尾号910  ygbmcard
         ygbmcard        =   getIntent().getStringExtra("ygbmcard");//  银行卡

        tv_withtwo.setText("￥"+ygbramount);
        tv_withthress.setText("￥"+ygbchargeamount);
        tv_withone.setText(""+ygbbankinfo);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_withdrawaldetail;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.btn_wipay})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.btn_wipay:


                if( !TextUtils.isEmpty(ygbmcard) && !TextUtils.isEmpty(ygbramount) && !TextUtils.isEmpty(ygbchargeamount)){
                    getEventData();
                } else {
                    ToastUtil.show(mContext,"提交数据为空");
                }





                break;

        }
    }

    private void getEventData() {

        String userid = (String) SPUtil.get(USERID, "");

        mdrawalMap.clear();
        mdrawalMap.put("userid", userid);
        mdrawalMap.put("bankno", ""+ygbmcard);//银行卡号
        mdrawalMap.put("amount", ""+ygbramount);//金额
        mdrawalMap.put("ygbchargeamount", ""+ygbchargeamount);//手续费

        OkHttp3Utils.getInstance().doPost(Constants.GETBANKMONEYFINISH_URL, null, mdrawalMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"待审核"}

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            DataBanK dataBanK = gson.fromJson(s, DataBanK.class);

                            if(dataBanK.getData().getSuccess().equals("1")){

                                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(dataBanK.getMsg())
//                                        .setContentText("Won't be able to recover this file!")
//                                        .setConfirmText("Yes,delete it!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();


                                                BankCardEvent messageEvent=new BankCardEvent();
                                                messageEvent.setMsg("Bank");
                                                EventBus.getDefault().post(messageEvent);

                                                finish();
                                            }
                                        })
                                        .show();
                            }else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(dataBanK.getMsg())
                                        .show();

                            }


                        }


                    }
                });

    }


    class DataBanK{


        /**
         * result : true
         * data : {"success":"1"}
         * msg : 待审核
         */

        private String result;
        private DataBean data;
        private String msg;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }



}
