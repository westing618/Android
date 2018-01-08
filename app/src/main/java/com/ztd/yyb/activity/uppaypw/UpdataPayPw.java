package com.ztd.yyb.activity.uppaypw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanUpPaypw.Datauppay;
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

import static com.ztd.yyb.activity.LoginActivity.USERPHONE;

/**
 * 修改支付密码
 * Created by  on 2017/4/5.
 */

public class UpdataPayPw extends BaseActivity {

    private final int TIME_COUNT_DOWN = 101;
    private final int TIME_COUNT_DOWN_COMPLETE = 102;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_uppay)
    TextView tv_uppay;

    @BindView(R.id.btn_yanz)
    Button btn_yanz;

    @BindView(R.id.edt_yanz)
    EditText edt_yanz;

    private Map<String, String> mMessageMap = new HashMap<>();
    private Map<String, String> mCheckMessageMap = new HashMap<>();

    String  yzm="";//测试验证码

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

        String flag = getIntent().getStringExtra("FLAG");

        if(flag.equals("1")){
            mTvTitle.setText("修改支付密码");
        }else {
            mTvTitle.setText("设置支付密码");
        }



        String pNumber = (String) SPUtil.get(USERPHONE, "");

        if(!TextUtils.isEmpty(pNumber) && pNumber.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            tv_uppay.setText("为了验证您的身份，需向手机"+sb.toString()+"发送一条验证码，请点击下方按钮获取验证码");
        }





    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_updatapay;
    }


    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back,R.id.btn_login, R.id.btn_yanz})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.btn_yanz:
                timeCountDown();
                getMessage();
                break;
            case R.id.btn_login:

                check();

                break;

        }
    }


    //校验验证码
    private void check() {

        yzm=edt_yanz.getText().toString().trim();

        if(TextUtils.isEmpty(yzm)){
            ToastUtil.show(mContext,"请输入验证码");
            return;
        }

        if(yzm.length()!=6){
            ToastUtil.show(mContext,"请输入6位验证码");
            return;
        }

        mCheckMessageMap.clear();

        mCheckMessageMap.put("yzm",yzm);


        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.CODECHECK_URL, null, mCheckMessageMap)
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

                            Datauppay datauppay = gson.fromJson(s, Datauppay.class);

                            if(datauppay.getData().getSuccess().equals("1")){

                                startActivity(new Intent(mContext, UpdataPayPww.class));

                                finish();

                            }else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(datauppay.getMsg())
                                        .show();
                            }
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
                    String str =   "重新发送"+"("+timeCount+")";
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
            };

        }.start();

    }

    //获取验证码
    private void getMessage() {

        String pNumber = (String) SPUtil.get(USERPHONE, "");

        mMessageMap.clear();
        mMessageMap.put("usertel",pNumber);

        OkHttp3Utils.getInstance().doPost(Constants.CODEMESSAGE_URL, null, mMessageMap)
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
                    public void onNext(String s) {//{"msg":"请输入正确的手机号","success":"0"}

                        if(!TextUtils.isEmpty(s)){

                            Gson gson=new Gson();

                            Datauppay datauppay = gson.fromJson(s, Datauppay.class);

                            if(datauppay.getData().getSuccess().equals("1")){
                                ToastUtil.show(mContext,"验证码已发送请注意查收");
                            }else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(datauppay.getMsg())
                                        .show();
                            }
                        }


                    }
                });


    }

}
