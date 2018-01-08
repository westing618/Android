package com.hyphenate.easeui.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.even.GenearchinfoBean;
import com.hyphenate.easeui.even.OrderinfoBean;
import com.hyphenate.easeui.even.UserData;
import com.hyphenate.easeui.even.WorkerinfoBean;
import com.hyphenate.easeui.utils.SPUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


/**
 * Created by  on 2017/5/19.
 */

public class UserChatActivity extends AppCompatActivity {

    TextView tv_yname;
    TextView tv_userphone;
    TextView tv_usersex;
    TextView tv_userjob;
    TextView tv_userjobtime;
    TextView tv_userjobstat;
    TextView tv_userjonum;
    TextView tv_username;
    TextView tv_useraddress;

    private Dialog mSelectHeadteacherDialog; //对话框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_userchat);

        String username = getIntent().getStringExtra("username");

        String userid = (String) SPUtil.get("USERID", "", this);

//        Log.e("userid","="+userid);

        inUI();

        initSelectHeadteacherDialog();


        tv_userphone.setText(username);

        findViewById(R.id.ll_yname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectHeadteacherDialog.show();
            }
        });
        findViewById(R.id.lin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getInfoData(userid);


    }

    WorkerinfoBean workerinfo;
    GenearchinfoBean genearchinfo;

    private void getInfoData(String userid) {

        HttpUtils http = new HttpUtils();

        String url = "http://api.yogobei.com/memberAppController.do?getInfo&userid=" + userid;

        http.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();

//                Log.e("onSuccess", "=" + arg0.result);

                UserData userData = gson.fromJson(arg0.result, UserData.class);

                if (userData.getData().getSuccess().equals("1")) {

                     workerinfo = userData.getData().getWorkerinfo();

                    String ygbmcsex = workerinfo.getYgbmcsex();
                    String ygbmcjob = workerinfo.getYgbmcjob();
                    String ygbmcjobage = workerinfo.getYgbmcjobage();
                    String ygbmcname = workerinfo.getYgbmcname();
                    String ygbmcaddress = workerinfo.getYgbmcaddress();
                        if(ygbmcsex!=null){
                            if(ygbmcsex.equals("1")){
                                tv_usersex.setText("性别: "+"男");
                            }else {
                                tv_usersex.setText("性别: "+"女");
                            }
                        }

                    if(ygbmcjob==null){
                        tv_userjob.setText("职业: ");
                    }else {
                        tv_userjob.setText("职业: "+ygbmcjob);
                    }
                    if(ygbmcjobage==null){
                        tv_userjobtime.setText("工龄: ");
                    }else {
                        tv_userjobtime.setText("工龄: "+ygbmcjobage);
                    }
                    if(ygbmcname==null){
                        tv_username.setText("姓名: ");
                    }else {
                        tv_username.setText("姓名: "+ygbmcname);
                    }
                    if(ygbmcaddress==null){
                        tv_useraddress.setText("地址: ");
                    }else {
                        tv_useraddress.setText("地址: "+ygbmcaddress);
                    }

                     genearchinfo = userData.getData().getGenearchinfo();

                    OrderinfoBean orderinfo = userData.getData().getOrderinfo();

                    String star = orderinfo.getStar();

                    String order = orderinfo.getOrder();

                    tv_userjobstat.setText("平均星级评(分): "+star);

                    tv_userjonum.setText("成交量(单): "+order);

                }

            }
        });


    }

    private void inUI() {

        tv_yname = (TextView) findViewById(R.id.tv_yname);
        tv_userphone = (TextView) findViewById(R.id.tv_userphone);

        tv_usersex = (TextView) findViewById(R.id.tv_usersex);
        tv_userjob = (TextView) findViewById(R.id.tv_userjob);
        tv_userjobtime = (TextView) findViewById(R.id.tv_userjobtime);
        tv_username = (TextView) findViewById(R.id.tv_username);

        tv_useraddress = (TextView) findViewById(R.id.tv_useraddress);

        tv_userjobstat = (TextView) findViewById(R.id.tv_userjobstat);
        tv_userjonum = (TextView) findViewById(R.id.tv_userjonum);

    }

    private void initSelectHeadteacherDialog() {

        mSelectHeadteacherDialog = new Dialog(this, R.style.dialog_bottom_full);
        mSelectHeadteacherDialog.setCanceledOnTouchOutside(true);
        mSelectHeadteacherDialog.setCancelable(true);

        Window window = mSelectHeadteacherDialog.getWindow();
        window.setGravity(Gravity.TOP);

        View view = View.inflate(this, R.layout.lay_myo, null);

        view.findViewById(R.id.tv_yname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismissSelectHeadteacherDialog();
                tv_yname.setText("师傅");

                String ygbmcsex = workerinfo.getYgbmcsex();
                String ygbmcjob = workerinfo.getYgbmcjob();
                String ygbmcjobage = workerinfo.getYgbmcjobage();
                String ygbmcname = workerinfo.getYgbmcname();
                String ygbmcaddress = workerinfo.getYgbmcaddress();

//                tv_usersex.setText("性别: "+ygbmcsex);

                if(ygbmcsex!=null){
                    if(ygbmcsex.equals("1")){
                        tv_usersex.setText("性别: "+"男");
                    }else {
                        tv_usersex.setText("性别: "+"女");
                    }
                }

                if(ygbmcjob==null){
                    tv_userjob.setText("职业: ");
                }else {
                    tv_userjob.setText("职业: "+ygbmcjob);
                }
                if(ygbmcjobage==null){
                    tv_userjobtime.setText("工龄: ");
                }else {
                    tv_userjobtime.setText("工龄: "+ygbmcjobage);
                }
                if(ygbmcname==null){
                    tv_username.setText("姓名: ");
                }else {
                    tv_username.setText("姓名: "+ygbmcname);
                }
                if(ygbmcaddress==null){
                    tv_useraddress.setText("地址: ");
                }else {
                    tv_useraddress.setText("地址: "+ygbmcaddress);
                }


            }
        });
        view.findViewById(R.id.tv_xname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismissSelectHeadteacherDialog();
                tv_yname.setText("家教");

                String ygbmcsex = genearchinfo.getYgbmcsex();
                String ygbmcjob = genearchinfo.getYgbmcjob();
                String ygbmcjobage = genearchinfo.getYgbmcjobage();
                String ygbmcname = genearchinfo.getYgbmcname();
                String ygbmcaddress = genearchinfo.getYgbmcaddress();

                if(ygbmcsex != null){
                    if(ygbmcsex.equals("1")){
                        tv_usersex.setText("性别: "+"男");
                    }else {
                        tv_usersex.setText("性别: "+"女");
                    }
                }

                if(ygbmcjob ==null){
                    tv_userjob.setText("职业: ");
                }else {
                    tv_userjob.setText("职业: "+ygbmcjob);
                }
                if(ygbmcjobage ==null){
                    tv_userjobtime.setText("工龄: ");
                }else {
                    tv_userjobtime.setText("工龄: "+ygbmcjobage);
                }
                if(ygbmcname==null){
                    tv_username.setText("姓名: ");
                }else {
                    tv_username.setText("姓名: "+ygbmcname);
                }
                if(ygbmcaddress==null){
                    tv_useraddress.setText("地址: ");
                }else {
                    tv_useraddress.setText("地址: "+ygbmcaddress);
                }
            }
        });


        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

    }

    /**
     * 隐藏对话框
     */
    private void dismissSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.dismiss();
        }
    }
}
