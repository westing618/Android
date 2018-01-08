package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanWallet.WalletData;
import com.ztd.yyb.evenbean.BankCardEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.R.id.tv_top_right;
import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 我的钱包
 * Created by  on 2017/3/13.
 */

public class MyWalletActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(tv_top_right)
    TextView mTvTopRight;
    String ygisbindcard;
    String ygbmbank;
    String ygbmcard;
    private Map<String, String> mMyWlallMap = new HashMap<>();

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("我的钱包");
        mTvTopRight.setVisibility(View.VISIBLE);
        mTvTopRight.setText("交易记录");

        EventBus.getDefault().register(this);

        getData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(BankCardEvent message) {

        String msg = message.getMsg();// 绑定完银行卡后 通知我的钱包 刷新数据
        if ("Bank".equals(msg)) {
            getData();
        }

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

//                              walletData.getData().getYgbuseamount()

                                tv_money.setText("" + ygbramount);

                                ygisbindcard = walletData.getData().getYgisbindcard();

                                ygbmbank = walletData.getData().getYgbmbank();//银行卡名称
                                ygbmcard = walletData.getData().getYgbmcard();//银行卡号

                            }
                        }


                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_mywallet;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.tv_top_right, R.id.btn_payment, R.id.btn_tixpaymen})
    public void onClick(View view) {

        switch (view.getId()) {// ygisbindcard:是否绑定银行卡  0:否 1:是

            case R.id.btn_payment:

                if (ygisbindcard != null) {

                    if (ygisbindcard.equals("0")) {
                        Intent intent = new Intent(mContext, BankCardActivity.class);
                        intent.putExtra("FLAG", "" + ygisbindcard);
                        intent.putExtra("ygbmbank", "" + ygbmbank);
                        intent.putExtra("ygbmcard", "" + ygbmcard);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(mContext, WithdrawalActivity.class));//提现
                    }
                }

                break;

            case R.id.btn_tixpaymen:


                Intent intent2 = new Intent(mContext, BankCardActivity.class);
                intent2.putExtra("FLAG", "" + ygisbindcard);
                intent2.putExtra("ygbmbank", "" + ygbmbank);
                intent2.putExtra("ygbmcard", "" + ygbmcard);
                startActivity(intent2);

                break;

            case R.id.lin_back:
                finish();
                break;

            case R.id.tv_top_right:

                startActivity(new Intent(mContext, TransactRecordsActivity.class));

                break;
        }

    }


}
