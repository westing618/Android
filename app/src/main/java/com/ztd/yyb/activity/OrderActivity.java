package com.ztd.yyb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.base.PayResult;
import com.ztd.yyb.bean.JSON_Pay;
import com.ztd.yyb.bean.JSON_PayAli;
import com.ztd.yyb.bean.JSON_PayList;
import com.ztd.yyb.net.NetworkHelper;
import com.ztd.yyb.net.ReverseRegisterNetworkHelper;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.MD5;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.util.UIDataListener;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提交订单
 * Created by westing on 2017/2/16.
 */

public class OrderActivity extends BaseActivity {
    @BindView(R.id.img_ali)
    ImageView mImgAli;
    @BindView(R.id.lin_ail)
    LinearLayout mLinAil;
    @BindView(R.id.img_wx)
    ImageView mImgWx;
    @BindView(R.id.lin_wx)
    LinearLayout mLinWx;
    @BindView(R.id.bnt_check)
    Button mBnt_check;

    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

    @Override
    protected void initViewsAndEvents() {
        initUI();
        req = new PayReq();
        msgApi.registerApp(Constants.APP_ID);
    }

    private LinearLayout btBack;

    private void initUI() {
        if (btBack == null) {
            btBack = (LinearLayout) findViewById(R.id.bt_back);
        }
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_order;
    }

    public void toBack(View view) {
        finish();
    }


    @Override
    protected void getBundleExtras(Bundle extras) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    private int state;
    @OnClick({R.id.lin_ail, R.id.lin_wx,R.id.bnt_check})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_ail:
                mImgAli.setImageResource(R.mipmap.list_btn_checked_sel);
                mImgWx.setImageResource(R.mipmap.list_btn_checked_nor);
                state=0;
                break;
            case R.id.lin_wx:
                mImgAli.setImageResource(R.mipmap.list_btn_checked_nor);
                mImgWx.setImageResource(R.mipmap.list_btn_checked_sel);
                state=1;
                break;
            case R.id.bnt_check:
                if(state==1){
                    payWx();
                }else{
                    payAli();
                }
                break;
        }
    }

    /**
     * 微信支付
     * 1.获取后台支付订单
     */
    private NetworkHelper<JSONObject> mPayHelper;
    private void payWx(){
        mPayHelper = new ReverseRegisterNetworkHelper(this);
//        map.clear();
//        map.put("Production_type","1");
//        map.put("GO_proid","1");
//        map.put("GA_id","1");
//        map.put("GO_receiving_method","1");
//        map.put("GO_number","1");
//        map.put("GO_price","1");
//        map.put("GO_title","户外攀岩");
//        map.put("GU_id",""+ SharedPreferencesUtil.get("GU_id",""));

        mPayHelper.sendPostMapRequest(Constants.URL_ORDERPAY, Request.Method.POST, null, new UIDataListener<JSONObject>() {
            @Override
            public void onDataChanged(JSONObject response) {
                Log.e("Pay","response:"+response.toString());
                getPayData(response.toString());

            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }

            @Override
            public void onResponse(Object response) {

            }
        });
    }

    private String Mrg,TAG_LOG="PayActivity";
    private JSON_Pay mPayList=new JSON_Pay();

    /**
     * 2.解析微信支付订单的参数
     * @param json
     */
    private void getPayData(String json){
        try {
            JSON_PayList listPay = JSON.parseObject(json, JSON_PayList.class);
            if (listPay != null) {
                if (listPay.isSuccess()) {
                    if (listPay.getRows() != null) {
                        mPayList = listPay.getRows();
                        Log.e("Pay","mPayList:"+mPayList.toString());
                        mHandler.sendEmptyMessage(UPDATE_INDEXS);
                        return;
                    } else {

                        Log.e(TAG_LOG, "解析失败：listPay.getRows() == null");
                    }

                } else {
                    Log.e(TAG_LOG, "解析失败：listPay.isSuccess()=false");
                }
            } else {
                Log.e(TAG_LOG, "解析失败：listPay == null");
            }
            Mrg = listPay.getMsg();
            mHandler.sendEmptyMessage(FAILE_1);
        } catch (JSONException e) {
            mHandler.sendEmptyMessage(FAILE_1);
        }
    }
    private final int FAILE_1=100,UPDATE_INDEXS=201,UPDATE_AliINDEXS=301,SDK_PAY_FLAG=401;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FAILE_1:
                    ToastUtil.show(OrderActivity.this,""+Mrg);
                    break;
                case UPDATE_INDEXS:
//                    payWc();
                    genPayReq();//微信支付
                    break;
                case UPDATE_AliINDEXS:
                    genPayAliReq();//支付宝支付
                    break;
                case SDK_PAY_FLAG://监听支付回调
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    /**
     * 3.发送微信参数调起支付功能
     */
    private void genPayReq() {

        req.appId = Constants.APP_ID;
        req.partnerId = mPayList.getPartnerid();//Constants.MCH_ID;
        req.prepayId = mPayList.getPrepayid();//resultunifiedorder.get("prepay_id");
        req.packageValue = mPayList.getPackages();//"prepay_id="+resultunifiedorder.get("prepay_id");
        req.nonceStr = mPayList.getNoncestr();//genNonceStr();
        req.timeStamp = mPayList.getTimestamp();//String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * 签名
     * @param params
     * @return
     */
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }

    /**
     * 支付宝支付
     * 1.获取后台支付订单
     */
    private void payAli(){
        mPayHelper = new ReverseRegisterNetworkHelper(this);
//        map.clear();
//        map.put("Production_type","1");
//        map.put("GO_proid","1");
//        map.put("GA_id","1");
//        map.put("GO_receiving_method","1");
//        map.put("GO_number","1");
//        map.put("GO_price","1");
//        map.put("GO_title","户外攀岩");
//        map.put("GU_id",""+ SharedPreferencesUtil.get("GU_id",""));

        mPayHelper.sendPostMapRequest(Constants.URL_PAYALI, Request.Method.POST, null, new UIDataListener<JSONObject>() {
            @Override
            public void onDataChanged(JSONObject response) {
                Log.e("Pay","response:"+response.toString());
                getPayAliData(response.toString());

            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }

            @Override
            public void onResponse(Object response) {

            }
        });
    }

    /**
     * 2.解析后台返回的支付订单参数
     */
    private String mPayAliList="";
    private void getPayAliData(String json){
        try {
            JSON_PayAli listPay = JSON.parseObject(json, JSON_PayAli.class);
            if (listPay != null) {
                if (listPay.isSuccess()) {
                    if (listPay.getRows() != null) {
                        mPayAliList = listPay.getRows();
                        Log.e("PayAli","mPayList:"+mPayList.toString());
                        mHandler.sendEmptyMessage(UPDATE_AliINDEXS);
                        return;
                    } else {

                        Log.e(TAG_LOG, "解析失败：listPay.getRows() == null");
                    }

                } else {
                    Log.e(TAG_LOG, "解析失败：listPay.isSuccess()=false");
                }
            } else {
                Log.e(TAG_LOG, "解析失败：listPay == null");
            }
            Mrg = listPay.getMsg();
            mHandler.sendEmptyMessage(FAILE_1);
        } catch (JSONException e) {
            mHandler.sendEmptyMessage(FAILE_1);
        }
    }

    /**
     * 3.调用支付宝支付功能
     */
    private void genPayAliReq() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderActivity.this);
                Map<String, String> result = alipay.payV2(mPayAliList,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }
}
