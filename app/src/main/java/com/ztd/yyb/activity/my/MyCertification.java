package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.uppaypw.UpdataPayPw;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanMyCerti.CerarrayBean;
import com.ztd.yyb.bean.beanMyCerti.DataCertifi;
import com.ztd.yyb.evenbean.PerfEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 我的认证
 * Created by  on 2017/3/30.
 */

public class MyCertification extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_sfstate)
    TextView tv_sfstate;

    @BindView(R.id.tv_jjstate)
    TextView tv_jjstate;

    private Map<String, String> mMyCerMap = new HashMap<>();

    private int flag = 3;//   0审核中--1认证通过--2认证未通过 --3未认证
    private int flag1 = 3;//   0审核中--1认证通过--2认证未通过 --3未认证

    String ygbmcid1;
    String ygbmcid2;

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("我的认证");
        EventBus.getDefault().register(this);
        getData();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(PerfEvent perEvent) {
        String msg = perEvent.getMsg();
        if (msg.equals("true")) {
            getData();
        }

    }

    private void getData() {



        String userid = (String) SPUtil.get(USERID, "");

        mMyCerMap.clear();
        mMyCerMap.put("userid", userid);

//        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.GETCERTIFI_URL, null, mMyCerMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dismissLoadingDialog();

                    }

                    @Override
                    public void onNext(String s) {

//                        dismissLoadingDialog();

//                        ygbmcstate：状态：0审核中    1认证通过       2认证未通过  3未认证

//                        ygbmctype   类型：1：业主   2：师傅      3：家长   4家教


                        if (!TextUtils.isEmpty(s)) {// int flag = 3;//   0审核中--1认证通过--2认证未通过 --3未认证

                            Gson gson = new Gson();

                            DataCertifi dataCertifi = gson.fromJson(s, DataCertifi.class);

                            if (dataCertifi.getData().getSuccess().equals("1")) {

                                List<CerarrayBean> cerarray = dataCertifi.getData().getCerarray();

                                for (int i = 0; i < cerarray.size(); i++) {

                                    String ygbmctype = cerarray.get(i).getYgbmctype();
                                    String ygbmcstate = cerarray.get(i).getYgbmcstate();
                                    String ygbmci = cerarray.get(i).getYgbmcid();

                                    if (ygbmctype.equals("2")) {

                                        ygbmcid1=ygbmci;

                                        if (ygbmcstate.equals("0")) {

                                            tv_sfstate.setText("审核中");
                                            flag=0;

                                        } else if (ygbmcstate.equals("1")) {

                                            flag=1;
                                            tv_sfstate.setText("认证通过");

                                        } else if (ygbmcstate.equals("2")) {

                                            flag=2;
                                            tv_sfstate.setText("认证未通过");

                                        } else if (ygbmcstate.equals("3")) {

                                            flag=3;
                                            tv_sfstate.setText("未认证");
                                        }

                                    }


                                    if (ygbmctype.equals("4")) {

                                        ygbmcid2=ygbmci;

                                        if (ygbmcstate.equals("0")) {
                                            flag1=0;
                                            tv_jjstate.setText("审核中");

                                        } else if (ygbmcstate.equals("1")) {

                                            flag1=1;
                                            tv_jjstate.setText("认证通过");

                                        } else if (ygbmcstate.equals("2")) {

                                            flag1=2;
                                            tv_jjstate.setText("认证未通过");

                                        } else if (ygbmcstate.equals("3")) {

                                            flag1=3;
                                            tv_jjstate.setText("未认证");

                                        }

                                    }


                                }

                            }

                        }

                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_mycertification;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.ll_teacher, R.id.ll_shifu})//  0审核中--1认证通过--2认证未通过         】--3未认证
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_teacher:

//                Log.e("ygbmcid","="+ygbmcid2);

                if(flag1==3){

                    Intent intent = new Intent(this, PerfectInformationActicity.class);
                    intent.putExtra("FLAG", "4");
                    intent.putExtra("ygbmcid", ygbmcid2);
                    startActivity(intent);

                }else if(flag1==1){

                    Intent intent1 = new Intent(this, CertifiDetailsActivity.class);
                    intent1.putExtra("FLAG", "4");
                    intent1.putExtra("ygbmcid", ygbmcid2);
                    startActivity(intent1);

                }else if(flag1==2){
                    Intent inten = new Intent(this, PerfectInformationActicity.class);
                    inten.putExtra("FLAG", "4");
                    inten.putExtra("ygbmcid", ygbmcid2);
                    startActivity(inten);


                }else if(flag1==0){

                    startActivity(new Intent(this, CheckActiviry.class));

                }


                break;
            case R.id.ll_shifu:

//                Log.e("ygbmcid","="+ygbmcid1);

                if(flag==3){

                    Intent intent2 = new Intent(this, PerfectInformationActicity.class);
                    intent2.putExtra("ygbmcid", ygbmcid1);
                    intent2.putExtra("FLAG", "2");
                    startActivity(intent2);

                }else if(flag==1){

                    Intent intent3 = new Intent(this, CertifiDetailsActivity.class);
                    intent3.putExtra("FLAG", "2");
                    intent3.putExtra("ygbmcid", ygbmcid1);
                    startActivity(intent3);

                }else if(flag==2){

                    Intent inte = new Intent(this, PerfectInformationActicity.class);
                    inte.putExtra("ygbmcid", ygbmcid1);
                    inte.putExtra("FLAG", "2");
                    startActivity(inte);

                }else if(flag==0){

                    startActivity(new Intent(this, CheckActiviry.class));

                }



                break;
        }
    }

}
