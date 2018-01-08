package com.ztd.yyb.activity.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ztd.yyb.Alipay.AipayBean;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.my.ChooseCouponActivity;
import com.ztd.yyb.activity.uppaypw.UpdataPayPw;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.base.PayResult;
import com.ztd.yyb.bean.beanPare.WeiXPay;
import com.ztd.yyb.bean.beanUpPaypw.Datauppay;
import com.ztd.yyb.bean.beanWallet.WalletData;
import com.ztd.yyb.bean.beanYe.DataYe;
import com.ztd.yyb.evenbean.CouEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.view.mypwdinputlibrary.InputPwdView;
import com.ztd.yyb.view.mypwdinputlibrary.MyInputPwdUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;
import static com.ztd.yyb.activity.LoginActivity.USERPHONE;

/**
 * 付款
 * Created by  on 2017/3/17.
 */

public class PaymentActivity extends BaseActivity {

    /**
     * 微信支付错误检测 提示语
     */
    private static final String WX_PAY_ERRMSG_1 = "您没有安装微信...";        //wxApi  !mIWXAPI.isWXAppInstalled()
    private static final String WX_PAY_ERRMSG_2 = "当前版本不支持支付功能...";//wxApi   !mIWXAPI.isWXAppSupportAPI()
    private static final String WX_PAY_ERRMSG_3 = "微信支付失败...";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    public IWXAPI wxApi;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_ownecoupon)
    TextView tv_ownecoupon;

    @BindView(R.id.pay_money)
    TextView pay_money;
    @BindView(R.id.img_ali)
    ImageView img_ali;
    @BindView(R.id.img_wx)
    ImageView img_wx;
    @BindView(R.id.img_ye)
    ImageView img_ye;


    String orderid;
    String userid;
    String type = "1";     //  0 加价 1需求
    String ygbdgamount;
    String ygbdgamount2;
    String ygbdaddprice = "0";
    String category;
    String jiajia;

    String ygbcId = "";//优惠券ID

    String ygbmkl;//ygbmkl = 0 未设置  ygbmkl = 1 已设置支付密码

    boolean Coupon = false;//默认用户没用优惠券

    private Map<String, String> mpayMap = new HashMap<>();//微信
    private Map<String, String> mAliMap = new HashMap<>();//支付宝
    private Map<String, String> mCanMap = new HashMap<>();//余额
    private Map<String, String> mMyWlallMap = new HashMap<>();//获取 用户是否有设置支付密码
    private Map<String, String> mMessageMap = new HashMap<>();  //验证 ，密码
    private int flag = 1;
    private MyInputPwdUtil myInputPwdUtil;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {


                        if (jiajia.equals("0")) {

                            new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                        } else {

                            Intent intent = new Intent(PaymentActivity.this, PaymentSuccessfulActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
//                        new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("支付失败")
////                        .setContentText("Something went wrong!")
//                                .show();
                    }
                    break;
                }

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void initViewsAndEvents() {


        EventBus.getDefault().register(this);

        userid = (String) SPUtil.get(USERID, "");

        jiajia = (String) SPUtil.get("JIAJIA", "");

        mTvTitle.setText("付款");

        Intent intent = getIntent();

        orderid = intent.getStringExtra("orderid");

        ygbdgamount = intent.getStringExtra("ygbdgamount");
        ygbdgamount2=intent.getStringExtra("ygbdgamount");

        ygbdaddprice = intent.getStringExtra("ygbdaddprice");

        category = intent.getStringExtra("type");//0师傅  1家教

        pay_money.setText("￥" + ygbdgamount);


        wxApi = WXAPIFactory.createWXAPI(this, Constants.WENXIN_APP_ID, false);//微信支付
        wxApi.registerApp(Constants.WENXIN_APP_ID);

        getData();

        inView();

    }

    private void inView() {

        myInputPwdUtil = new MyInputPwdUtil(this);
        myInputPwdUtil.getMyInputDialogBuilder().setAnimStyle(R.style.dialog_anim);
        myInputPwdUtil.setListener(new InputPwdView.InputPwdListener() {
            @Override
            public void hide() {
                myInputPwdUtil.hide();
            }

            @Override
            public void forgetPwd() {
            }

            @Override
            public void finishPwd(String pwd) {

                updata(pwd);

                myInputPwdUtil.hide();

            }
        });

    }

    private void updata(String pwone) {


        String pNumber = (String) SPUtil.get(USERPHONE, "");

        mMessageMap.clear();
        mMessageMap.put("usertel", pNumber);
        mMessageMap.put("pwd", pwone);
        mMessageMap.put("type", "1");//type=0 更新支付密码 ，type=1验证密码

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
                    public void onNext(String s) {//{"data":{"success":"1"},"code":"0"}

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            Datauppay datauppay = gson.fromJson(s, Datauppay.class);

                            if (datauppay.getData().getSuccess().equals("1")) {

                                payContr();     //余额

                            } else {

                                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("提示")
                                        .setContentText(datauppay.getMsg())

                                        .setConfirmText("去修改")

                                        .setCancelText("重试")

                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                Intent intent = new Intent(mContext, UpdataPayPw.class);
                                                intent.putExtra("FLAG", "1");
                                                startActivity(intent);
                                                finish();
                                            }
                                        })

                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                                myInputPwdUtil.show();
                                            }
                                        })
                                        .show();

                            }
                        }

                    }
                });

    }

    private void getData() {

        mMyWlallMap.clear();
        mMyWlallMap.put("userid", userid);

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

                            WalletData walletData = gson.fromJson(s, WalletData.class);

                            if (walletData.getData().getSuccess().equals("1")) {

                                ygbmkl = walletData.getData().getYgbmkl();

                            }
                        }


                    }
                });

    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_payment;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    @Subscribe
    public void onEventMainThread(CouEvent degeEvent) {

        String msg = degeEvent.getMsg();// 接受优惠券 返回的ID
        String id = degeEvent.getId();
        String proce = degeEvent.getProce();

        boolean flag = degeEvent.isFlag();

        if (flag) {//

            Coupon = true;

            if (!id.equals(ygbcId)) {

                tv_ownecoupon.setText("已选择优惠券" + msg);

                if (proce != null) {

                    double aaproce = Double.parseDouble(proce);         //优惠券价格

                    double bbygbdgamount = Double.parseDouble(ygbdgamount);//总金额

                    if (bbygbdgamount > aaproce) {//总金额大于优惠券时才可以用

                        pay_money.setText("￥" + (bbygbdgamount - aaproce));

                        ygbdgamount = "" + (bbygbdgamount - aaproce);

                    } else {

//                        ToastUtil.show(mContext,"总金额小于优惠券");

                        pay_money.setText("￥" + "0.01");

                        ygbdgamount = "0.01";

                    }


                }

                ygbcId = id;

            }

        } else {//未使用优惠券

            tv_ownecoupon.setText("未使用优惠券");

            Coupon = false;

            ygbdgamount=ygbdgamount2;

            pay_money.setText("￥" + ygbdgamount2);

            ygbcId="";

        }


    }

    @OnClick({R.id.lin_back, R.id.btn_payment, R.id.img_ali, R.id.img_wx, R.id.img_ye, R.id.ll_ownecoupon})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_ownecoupon:
                startActivity(new Intent(mContext, ChooseCouponActivity.class));
                break;

            case R.id.img_ali://支付宝

                flag = 1;
                img_ali.setImageResource(R.mipmap.list_btn_check_sel);
                img_wx.setImageResource(R.mipmap.list_btn_checked_nor);
                img_ye.setImageResource(R.mipmap.list_btn_checked_nor);

                break;
            case R.id.img_wx://微信

                flag = 2;
                img_wx.setImageResource(R.mipmap.list_btn_check_sel);
                img_ali.setImageResource(R.mipmap.list_btn_checked_nor);
                img_ye.setImageResource(R.mipmap.list_btn_checked_nor);

                break;
            case R.id.img_ye://余额

                flag = 3;
                img_ye.setImageResource(R.mipmap.list_btn_check_sel);
                img_ali.setImageResource(R.mipmap.list_btn_checked_nor);
                img_wx.setImageResource(R.mipmap.list_btn_checked_nor);

                break;

            case R.id.btn_payment:

                if (flag == 1) {

                    //支付宝
                    payAli();

                } else if (flag == 2) {
                    //微信

                    getmyData();

                } else if (flag == 3) {


                    if (ygbmkl.equals("0")) {//ygbmkl = 0 未设置  ygbmkl = 1 已设置支付密码


                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("还未设置支付密码")
                                .setContentText("去设置!")
                                .setConfirmText("确定!")//确定
                                .setCancelText("取消!")//取消
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();

                                        Intent intent = new Intent(mContext, UpdataPayPw.class);
                                        intent.putExtra("FLAG", "2");
                                        startActivity(intent);

                                        finish();

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();
//                        return;

                    } else {

                        myInputPwdUtil.show();

                    }

                }

                break;
        }
    }

    //TODO  余额
    private void payContr() {

//        String jiajia = (String) SPUtil.get("JIAJIA", "");

        mCanMap.clear();

        mCanMap.put("orderId", "" + orderid);
        mCanMap.put("userId", "" + userid);
        mCanMap.put("category", category);//类型 0：师傅 1：家教


        if (Coupon) {
            mCanMap.put("ygbcId", ygbcId);//优惠券id  -1 没有
        } else {
            mCanMap.put("ygbcId", "-1");//优惠券id  -1 没有
        }


        if (jiajia.equals("0")) {//加价
            mCanMap.put("type", "0");//0 加价付款 1发布需求付款
            mCanMap.put("realAmount", ygbdaddprice);//
        } else {
            mCanMap.put("type", "1");//0 加价付款 1发布需求付款
            mCanMap.put("realAmount", "0");//0 未加价
        }

        mCanMap.put("price", "" + ygbdgamount);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.PAYCONTR_URL, null, mCanMap)
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
                    public void onNext(String s) { //    {"msg":"付款成功","orderNo":645,"error":"false"}

                        Log.e("onNext","="+s);

                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();

                            try {

                                DataYe dataYe = gson.fromJson(s, DataYe.class);

                                if (dataYe.getError().equals("false")) {

//                                    String jiajia = (String) SPUtil.get("JIAJIA", "");

                                    if (jiajia.equals("0")) {

                                        new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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

                                    } else {

                                        Intent intent = new Intent(PaymentActivity.this, PaymentSuccessfulActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }


                                } else {
                                    new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText(dataYe.getMsg())
                                            .show();
                                }

                            } catch (Exception e) {

                                ToastUtil.show(mContext, "余额支付数据解析异常请联系后台管理员");

                            }


                        }
                    }
                });


    }

    //TODO  支付宝
    private void payAli() {

//        String jiajia = (String) SPUtil.get("JIAJIA", "");


        mAliMap.clear();

        mAliMap.put("orderId", "" + orderid);//订单id
        mAliMap.put("userId", "" + userid);//用户id
        mAliMap.put("category", category);//类型 0：师傅 1：家教


        if (Coupon) {
            mAliMap.put("ygbcId", ygbcId);//优惠券id  -1 没有
        } else {
            mAliMap.put("ygbcId", "-1");//优惠券id  -1 没有
        }


        if (jiajia.equals("0")) {//加价
            mAliMap.put("type", "0");//0 加价付款 1发布需求付款
            mAliMap.put("realAmount", ygbdaddprice);//
        } else {
            mAliMap.put("type", "1");//0 加价付款 1发布需求付款
            mAliMap.put("realAmount", "0");//0 未加价
        }

        mAliMap.put("price", "" + ygbdgamount);// 需求订单价格

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.GETALIPAY_URL, null, mAliMap)
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

                        if (!TextUtils.isEmpty(s)) {

                            try {

                                Gson gson = new Gson();
                                AipayBean aipayBean = gson.fromJson(s, AipayBean.class);
                                if (aipayBean.getData().getSuccess() == 1) {
                                    String data = aipayBean.getData().getData();
                                    if (!TextUtils.isEmpty(data)) {
                                        genPayAliReq(data);
                                    } else {
                                        ToastUtil.show(mContext, "支付宝订单信息为空");
                                    }
                                } else {

                                    new SweetAlertDialog(mContext)
                                            .setTitleText("提示")
                                            .setContentText(aipayBean.getMsg())
                                            .show();

                                }

                            } catch (Exception e) {
                                ToastUtil.show(mContext, "后台获取支付宝订单信息异常");
                            }


                        }

                    }
                });

    }


    /**
     * 从后台获取微信的订单信息
     */
//TODO  微信
    private void getmyData() {

//        String jiajia = (String) SPUtil.get("JIAJIA", "");

        mpayMap.clear();

        mpayMap.put("userId", "" + userid);//用户id
        mpayMap.put("orderId", "" + orderid);//订单id
        mpayMap.put("category", category);//类型 0：师傅 1：家教

        if (Coupon) {
            mpayMap.put("ygbcId", ygbcId);//优惠券id  -1 没有
        } else {
            mpayMap.put("ygbcId", "-1");//优惠券id  -1 没有
        }


        if (jiajia.equals("0")) {//加价
            mpayMap.put("type", "0");//0 加价付款 1发布需求付款
            mpayMap.put("realAmount", ygbdaddprice);//
        } else {
            mpayMap.put("type", "1");//0 加价付款 1发布需求付款
            mpayMap.put("realAmount", "0");//0 未加价
        }

        mpayMap.put("price", "" + ygbdgamount);// 需求订单价格

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.WXPAY_URL, null, mpayMap)
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
                    public void onNext(String s) {//{"msg":"error","body":"数据请求异常"}{"msg":"error","body":"prepay_id为空！"}

//                        Log.e("PAYMENT","="+s);

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            WeiXPay weiXPay = gson.fromJson(s, WeiXPay.class);

                            payWx(weiXPay);      //微信支付

                        }

                    }
                });

    }


    /**
     * 3.发送微信参数调起支付功能
     */
    private void payWx(WeiXPay weiXPay) {

        if (!wxApi.isWXAppInstalled()) {
            ToastUtil.show(mContext, WX_PAY_ERRMSG_1);
            return;
        } else if (!wxApi.isWXAppSupportAPI()) {
            ToastUtil.show(mContext, WX_PAY_ERRMSG_2);
            return;
        }


        if (weiXPay == null) {
            return;
        }

        PayReq request = new PayReq();

        request.appId = weiXPay.getAppid();

        request.partnerId = weiXPay.getPartnerid();

        request.prepayId = weiXPay.getPrepayid();

        request.packageValue = "Sign=WXPay";

        request.nonceStr = weiXPay.getNoncestr();

        request.timeStamp = weiXPay.getTimestamp();

        request.sign = weiXPay.getSign();

        wxApi.sendReq(request);

    }


    /**
     * 3.调用支付宝支付功能  ()
     */
    private void genPayAliReq(final String payInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

//                String payInfo= AlipayUtils.getPayStr("用工贝订单","用工贝订单","0.01",getOutTradeNo(),Constants.NOTIFY_URL);
//                String payInfo= ""+payInfo;

                PayTask alipay = new PayTask(PaymentActivity.this);

                Map<String, String> result = alipay.payV2(payInfo, true);

                Log.e("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;

                mHandler.sendMessage(msg);

            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }


}
