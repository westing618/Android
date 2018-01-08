package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanBdBank.BankData;
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
 * 绑定 银行卡
 * Created by  on 2017/4/6.
 */

public class BDBankCardActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_bank)
    EditText et_bank;

    @BindView(R.id.et_bankname)
    EditText et_bankname;

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.tv_kanum)
    EditText tv_kanum;

    @BindView(R.id.tv_kaphone)
    EditText tv_kaphone;

    private Map<String, String> mBdandMap = new HashMap<>();

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("绑定银行卡");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_bdbankcard;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.btn_bank})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.btn_bank:

                postData();

//                Intent intent=new Intent(this, PerfectInformationActicity.class);
//                intent.putExtra("FLAG","4");
//                startActivity(intent);

                break;
        }

    }

    private void postData() {

        String bank = et_bank.getText().toString().trim();//银行名称
        String bankname = et_bankname.getText().toString().trim();//银行所在地

        String username = et_username.getText().toString().trim();//申请人名称
        String kanum = tv_kanum.getText().toString().trim();//银行卡号
        String kaphone = tv_kaphone.getText().toString().trim();//绑定银行时的申请电话

        if (TextUtils.isEmpty(bank)) {
            ToastUtil.show(mContext,"请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(bankname)) {
            ToastUtil.show(mContext,"请输入银行所在地");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            ToastUtil.show(mContext,"请输入申请人名称");
            return;
        }
        if (TextUtils.isEmpty(kanum)) {
            ToastUtil.show(mContext,"请输入银行卡号");
            return;
        }

        boolean cardnum = checkBankCard(kanum);

        if (!cardnum) {
            ToastUtil.show(mContext,"请输入正确的银行卡号");
            return;
        }


        if (TextUtils.isEmpty(kaphone)) {
            ToastUtil.show(mContext,"请输入绑定银行时的申请电话");
            return;
        }


        String userid = (String) SPUtil.get(USERID, "");

        mBdandMap.clear();

        mBdandMap.put("userid",userid);//用户id

        mBdandMap.put("bankno",kanum);//银行卡号

        mBdandMap.put("ygbmbank",bank);//银行名称

        mBdandMap.put("ygbmbankarea",bankname);//银行所在地

        mBdandMap.put("ygbmbankname",username);//申请人名称

        mBdandMap.put("ygbmbanktel",kaphone);//绑定银行时的申请电话

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.BINDBANDCARD_URL, null, mBdandMap)
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
                    public void onNext(String s) {
                        dismissLoadingDialog();


                        if(!TextUtils.isEmpty(s)){
                            Gson gson=new Gson();
                            BankData bankData = gson.fromJson(s, BankData.class);
                            if(bankData.getData().getSuccess().equals("1")){

                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("银行卡绑定成功!")
//                                .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
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

                            }
                        }
                    }
                });

    }
    /**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */

    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
//如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

}
