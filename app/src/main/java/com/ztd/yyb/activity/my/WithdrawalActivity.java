package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanBank.BankWith;
import com.ztd.yyb.bean.beanWallet.WalletData;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

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
 * 提现
 * Created by  on 2017/4/6.
 */

public class WithdrawalActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_wbankname)
    TextView tv_wbankname;

    @BindView(R.id.etv_mtotality)
    EditText etv_mtotality;//1

    @BindView(R.id.tv_totality)
    TextView tv_totality;//2

    @BindView(R.id.tv_mytotality)
    TextView tv_mytotality;//3
    String ygbmcard;
    String ygbuseamount;

    private Map<String, String> mMyWlallMap = new HashMap<>();

    private Map<String, String> mMydrawMap = new HashMap<>();

    @BindView(R.id.tv_top_right)
    TextView mTvTopRight;

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("提现");
        mTvTopRight.setVisibility(View.VISIBLE);
        mTvTopRight.setText("提现记录");
        getData();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_withdrawal;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    private void getData() {

        mMyWlallMap.clear();

        String userid = (String) SPUtil.get(USERID, "");
        mMyWlallMap.put("userid", "" + userid);

        OkHttp3Utils.getInstance().doPost(Constants.MY_MONET_URL, null, mMyWlallMap)
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
                    public void onNext(String s) {

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

//                            ygbramount： 总金额
//                            ygbuseamount:可提现金额


                            WalletData walletData = gson.fromJson(s, WalletData.class);

                            if (walletData.getData().getSuccess().equals("1")) {

                                String ygbramount = walletData.getData().getYgbramount();
                                ygbuseamount = walletData.getData().getYgbuseamount();

//
                                String ygbmbank = walletData.getData().getYgbmbank();//银行卡名称

                                ygbmcard = walletData.getData().getYgbmcard();//银行卡号

                                String substr = ygbmcard.substring(ygbmcard.length() - 4, ygbmcard.length());

                                tv_wbankname.setText("" + ygbmbank + "(" + substr + ")");
//                                tv_mtotality.setText("￥"+ygbuseamount);
                                tv_totality.setText("总余额" + ygbramount + "元");
                                tv_mytotality.setText("可提现金额" + ygbuseamount + "元");

                            }
                        }


                    }
                });

    }


    @OnClick({R.id.lin_back, R.id.btn_withdraw, R.id.tv_tixian,R.id.tv_top_right})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_top_right:

                startActivity(new Intent(mContext, WithRecordsActivity.class));

                break;

            case R.id.tv_tixian:

                etv_mtotality.setText("" + ygbuseamount);

                break;

            case R.id.btn_withdraw:

                String trim = etv_mtotality.getText().toString().trim();

                if (TextUtils.isEmpty(trim)) {

                    ToastUtil.show(mContext, "请输入可提现金额");

                    return;
                }


                try {

                    double aa = Double.parseDouble(trim);
                    double bb = Double.parseDouble(ygbuseamount);


                    if(aa < 1){

                        ToastUtil.show(mContext, "可提现金额不足");

                        return;
                    }
//
                    if (aa > bb) {
                        ToastUtil.show(mContext, "可提现金额不足");
                        return;
                    }

                    ygbuseamount=trim;

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


                postData();

                break;


        }

    }

    private void postData() {

        mMydrawMap.clear();
        String userid = (String) SPUtil.get(USERID, "");
        mMydrawMap.put("userid", userid);
        mMydrawMap.put("bankno", ygbmcard);//银行卡号
        mMydrawMap.put("amount", ygbuseamount);//金额

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.GETBANKMONEY_URL, null, mMydrawMap)
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
                    public void onNext(String s) {//

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {
                            Gson gson=new Gson();
                            BankWith bankWith = gson.fromJson(s, BankWith.class);

                            if(bankWith.getData().getSuccess().equals("1")){

                                String ygbchargeamount = bankWith.getData().getYgbchargeamount();//手续费
                                String ygbramount = bankWith.getData().getYgbramount();//提现金额
                                String ygbbankinfo = bankWith.getData().getYgbbankinfo();//王大哥尾号910
                                String ygbmcard = bankWith.getData().getYgbmcard();

                                Intent in=new Intent(WithdrawalActivity.this,WithdrawalDetail.class);

                                in.putExtra("ygbchargeamount",""+ygbchargeamount);
                                in.putExtra("ygbramount",""+ygbramount);
                                in.putExtra("ygbbankinfo",""+ygbbankinfo);
                                in.putExtra("ygbmcard",""+ygbmcard);

                                startActivity(in);

                                finish();

                            } else {
                                new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)

                                        .setTitleText("提示")

                                        .setContentText(bankWith.getMsg())

                                        .setConfirmText("确定")

                                        .setCancelText("取消")

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })

                                        .show();
                            }
                        }
                    }
                });

    }


}
