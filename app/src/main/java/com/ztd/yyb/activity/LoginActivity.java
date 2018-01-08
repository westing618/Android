package com.ztd.yyb.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.base.BaseApplication;
import com.ztd.yyb.bean.UpData;
import com.ztd.yyb.bean.beanUser.UersBean;
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

import static cn.jpush.android.api.JPushInterface.getRegistrationID;
import static com.ztd.yyb.R.id.image_agreed;

/**
 * Created by westing on 2017/3/7.
 */

public class LoginActivity extends BaseActivity {

    public static String USERID = "USERID";// 用户ID
    public static String USERPHONE = "USERPHONE";// 用户手机号
    public static String IS_LOGIN = "isLogin";// 判断是否登录
    private final int TIME_COUNT_DOWN = 101;
    private final int TIME_COUNT_DOWN_COMPLETE = 102;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_login)
    Button mBtn_login;
    @BindView(R.id.btn_yanz)
    Button btn_yanz;
    @BindView(R.id.ll_myagreed)
    LinearLayout ll_mYagreed;
    @BindView(R.id.edt_phone_regin)
    EditText mEt_Phone_Regin;
    @BindView(R.id.edt_yanz)
    EditText mEt_Yanz;

    @BindView(image_agreed)
    ImageView im_agreed;

    String phone="";
    String num="";
    String deviceid="";
    private boolean flag = true;
    private Map<String, String> mLoginMap = new HashMap<>();
    private Map<String, String> mMessageMap = new HashMap<>();

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_COUNT_DOWN:
                    String str = (String) msg.obj;
                    btn_yanz.setText(str);
                    break;
                case TIME_COUNT_DOWN_COMPLETE:
                    btn_yanz.setClickable(true);
                    btn_yanz.setBackground(getResources().getDrawable(R.drawable.bg_item_demand));
                    btn_yanz.setText("获取验证码");
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("登录");


        getReadState();


    }

    //拨打电话和管理通话
    private void getReadState() {

        if (PermissionsUtil.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {

            TelephonyManager tm = (TelephonyManager)
                    getSystemService(Context.TELEPHONY_SERVICE);// deviceid :设备id   352571071639409 三星手机
            deviceid = tm.getDeviceId();

//            Log.e("=====", "deviceid" + deviceid);

        } else {

            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {


                    TelephonyManager tm = (TelephonyManager)
                            getSystemService(Context.TELEPHONY_SERVICE);// deviceid :设备id   352571071639409 三星手机
                    deviceid = tm.getDeviceId();
//                    Log.e("=====", "deviceid=" + deviceid);
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
//                    Log.e("=====", "用户拒绝了申请");
                }
            }, new String[]{Manifest.permission.READ_PHONE_STATE});
        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_login;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    @OnClick({R.id.lin_back, R.id.btn_login, R.id.btn_yanz, R.id.ll_myagreed,R.id.image_agreed})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.image_agreed:

                if (flag) {
                    im_agreed.setBackground(getResources().getDrawable(R.mipmap.login_btn_check_nor));
                    flag = false;
                } else {
                    im_agreed.setBackground(getResources().getDrawable(R.mipmap.login_btn_check_sel));
                    flag = true;
                }

                break;

            case R.id.btn_login:


                if (!BaseApplication.checkNet()) {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("没有网络哦亲...")
                            .show();
                    return;
                }

                phone = mEt_Phone_Regin.getText().toString().toString();

                num = mEt_Yanz.getText().toString().toString();

                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show(this, "手机号不能为空");
                    return;
                }
//                if (CheckFormatUtils.isMobileNO(phone) == false) {// 判断手机号码格式
//                    ToastUtil.show(this, "手机格式错误");
//                    return;
//                }

                if (phone.length()!=11) {
                    ToastUtil.show(this, "请输入11位手机号");
                    return;
                }

                if (TextUtils.isEmpty(num)) {
                    ToastUtil.show(this, "验证码不能为空");
                    return;
                }

                if (num.length() != 6) {
                    ToastUtil.show(this, "请输入6位验证码");
                    return;
                }


                if (!flag) {
                    ToastUtil.show(mContext, "请勾选使用协议");
                    return;
                }

                getLogin();


                break;
            case R.id.btn_yanz:


                if (!BaseApplication.checkNet()) {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("没有网络哦亲...")
                            .show();
                    return;
                }


                phone = mEt_Phone_Regin.getText().toString().toString();

                num = mEt_Yanz.getText().toString().toString();


                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.show(this, "手机号不能为空");
                    return;
                }
                if (phone.length()!=11) {
                    ToastUtil.show(this, "请输入11位手机号");
                    return;
                }

//                if (CheckFormatUtils.isMobileNO(phone) == false) {// 判断手机号码格式
//                    ToastUtil.show(this, "手机格式错误");
//                    return;
//                }


                getMessage();

                timeCountDown();

                break;

            case R.id.ll_myagreed:


//                if(flag){
                    Intent intent = new Intent(this,BannerActivity.class);
                    startActivity(intent);
//                }

                break;
        }
    }

    //获取验证码
    private void getMessage() {


        mMessageMap.clear();

        mMessageMap.put("usertel", phone);
        mMessageMap.put("deviceid", deviceid);


//        Log.e("=====","usertel"+phone);
//        Log.e("=====","deviceid"+deviceid);

        OkHttp3Utils.getInstance().doPost(Constants.LOGIN_MESSAGE_URL, null, mMessageMap)
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
                    public void onNext(String s) {//{"data":{"success":"1"},"code":"0","msg":"验证码发送成功"}

                        Log.e("验证码发送","="+s);

                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            UpData upData = gson.fromJson(s, UpData.class);
                            if (upData.getData().getSuccess().equals("1")) {
                                ToastUtil.show(mContext, upData.getMsg());
                            }else {
                                ToastUtil.show(mContext, upData.getMsg());
                            }

                        }


                    }
                });


    }


    //登录
    private void getLogin() {

        if (!BaseApplication.checkNet()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("没有网络哦亲...")
                    .show();
            return;
        }

        String BRAND = android.os.Build.BRAND;

        String MODEL = android.os.Build.MODEL;

        String ygbmmodel=BRAND+MODEL;

        String RELEASE = android.os.Build.VERSION.RELEASE;

        String ygbmsysversion=RELEASE;

        String registrationID = getRegistrationID(this);

        mLoginMap.clear();
        mLoginMap.put("usertel", phone);
        mLoginMap.put("ygbmsysversion", ygbmsysversion);//系统版本
        mLoginMap.put("ygbmmodel", ygbmmodel);//设备型号
        mLoginMap.put("yzm", num);//测试888888
        mLoginMap.put("ygbmregid", registrationID);// 极光推送 registrationId

//        Log.e("registrationID","="+registrationID);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.LOGIN_URL, null, mLoginMap)
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

                        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("登录失败请重试")
                                .setContentText("链接服务器失败")
                                .show();

                    }

                    @Override
                    public void onNext(String s) {

                        Log.e("onNext","=="+s);

                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UersBean uersBean = gson.fromJson(s, UersBean.class);

                            if (uersBean.getData().getSuccess().equals("1")) {

                                String userid = uersBean.getData().getUserid();

                                String ygbmtel = uersBean.getData().getUserinfo().getYgbmtel();

                                SPUtil.put(USERPHONE, ygbmtel);

                                SPUtil.put(USERID, userid);

                                SPUtil.put("IS_LOGIN", "1");

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

//                                Log.e("phone","=="+phone);

                                EMClient.getInstance().login("YGB" + phone, "Sec8ce1ffaa93106ba2", new EMCallBack() {//回调
                                    @Override
                                    public void onSuccess() {

                                        EMClient.getInstance().groupManager().loadAllGroups();
                                        EMClient.getInstance().chatManager().loadAllConversations();

                                        Log.e("main", "登录聊天服务器成功！");

//                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {
                                    }

                                    @Override
                                    public void onError(int code, String message) {
                                        Log.e("main", "登录聊天服务器失败！" + message + "code" + code);
                                    }
                                });

                            } else {
//                                ToastUtil.show(LoginActivity.this, uersBean.getMsg());

                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(uersBean.getMsg())
                                        .show();
                            }

                        } else {


                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("登录失败请重试")
                                    .setContentText("服务器数据异常")
                                    .show();

                        }


                    }
                });


    }


    private void timeCountDown() {

        btn_yanz.setClickable(false);

        btn_yanz.setBackground(getResources().getDrawable(R.drawable.bg_login_press));

        new Thread() {
            public void run() {
                int timeCount = 60;
                while (timeCount != 0) {
                    Message msg = new Message();
                    String str = "重新发送" + "(" + timeCount + ")";
                    msg.what = TIME_COUNT_DOWN;
                    msg.obj = str;
                    handler.sendMessage(msg);
                    timeCount--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = new Message();
                msg.what = TIME_COUNT_DOWN_COMPLETE;
                handler.sendMessage(msg);
            }

        }.start();

    }

//    private void requestCemera() {
//        if (PermissionsUtil.hasPermission(this, Manifest.permission.CAMERA)) {
//            //有访问摄像头的权限
//            Log.e("=====","有访问摄像头的权限");
//        } else {
//            PermissionsUtil.requestPermission(this, new PermissionListener() {
//                @Override
//                public void permissionGranted(@NonNull String[] permissions) {
//                    //用户授予了访问摄像头的权限
//                    Log.e("=====","用户授予了访问摄像头的权限");
//                }
//
//                @Override
//                public void permissionDenied(@NonNull String[] permissions) {
//                    //用户拒绝了访问摄像头的申请
//                    Log.e("=====","用户拒绝了访问摄像头的申请");
//                }
//            }, new String[]{Manifest.permission.CAMERA});
//        }
//    }


    //        if (PermissionsUtil.hasPermission(this, Manifest.permission.RECORD_AUDIO)) {//<!--获取手机录音机使用权限，听写、识别、语义理解需要用到此权限 -->
//            Log.e("=====","有访权限");
//
//        } else {
//            PermissionsUtil.requestPermission(this, new PermissionListener() {
//                @Override
//                public void permissionGranted(@NonNull String[] permissions) {
//
//                    Log.e("=====","用户授予了权限");
//
//                }
//
//                @Override
//                public void permissionDenied(@NonNull String[] permissions) {
//                    //用户拒绝了访问摄像头的申请
//                    Log.e("=====","用户拒绝了申请");
//                }
//            }, new String[]{Manifest.permission.RECORD_AUDIO});
//        }

}
