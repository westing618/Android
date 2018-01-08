package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.EvaluAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.UpData;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.MeanBean;
import com.ztd.yyb.bean.beanHome.StarlistBean;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.evenbean.EvalEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 我的发布 提交 评价
 * Created by  on 2017/3/13.
 */

public class EvaluationActivity extends BaseActivity implements EvaluAdapter.OnEvaluItemClickListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.evaratingBar)
    RatingBar evaratingBar;
    EvaluAdapter evaluAdapter;
    List<ChangBean> leavedata = new ArrayList<>();
    List<StarlistBean> starlist;
    String ygboid;
    String ygbestar;
    String ygbeevaluate;
    String ygbeevaluatemc;
    RecyclerView recyclerView;
    private Map<String, String> mEvaluaMap = new HashMap<>();

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("评价");

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();
        if (!TextUtils.isEmpty(myappconfig)) {
            AppConfigBean appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            starlist = appConfigBean.getData().getStarlist();
        }

        ygboid = getIntent().getStringExtra("ygboid");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_evalua);

//        evaluAdapter.setOnEvaluClickListener(this);
//       recyclerView.setLayoutManager(new GridLayoutManager(this, 3));//xml里面设置，

//        recyclerView.setHasFixedSize(true);


        evaratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                leavedata.clear();

                String s = String.valueOf(rating);

                char c = s.charAt(0);
                String n = "" + c;
                ygbestar = n;

                for (int i = 0; i < starlist.size(); i++) {

                    String ygbsnums = starlist.get(i).getYgbsnums();

                    if (n.equals(ygbsnums)) {

                        List<MeanBean> mean = starlist.get(i).getMean();

                        for (int j = 0; j < mean.size(); j++) {

                            String ygbsmname = mean.get(j).getYgbsmname();
                            int ygbsmid = mean.get(j).getYgbsmid();

                            ChangBean changben = new ChangBean();

                            changben.setName(ygbsmname);
                            changben.setId("" + ygbsmid);
                            leavedata.add(changben);

                            evaluAdapter = new EvaluAdapter(mContext, leavedata, EvaluationActivity.this);
                            recyclerView.setAdapter(evaluAdapter);

                            evaluAdapter.notifyDataSetChanged();

                        }

                    }

                }


            }
        });

    }

    private void postData() {

        mEvaluaMap.clear();

        if (TextUtils.isEmpty(ygbeevaluatemc)) {
            ToastUtil.show(mContext, "请选择评价内容");
            return;
        }


        String userid = (String) SPUtil.get(USERID, "");

        mEvaluaMap.put("ygboid", "" + ygboid);//订单ID
        mEvaluaMap.put("ygbmid", "" + userid);//
        mEvaluaMap.put("ygbestar", "" + ygbestar);//评星 1-5
        mEvaluaMap.put("ygbeevaluate", "" + ygbeevaluate);//评价代码值
        mEvaluaMap.put("ygbeevaluatemc", "" + ygbeevaluatemc);//评价名称

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.ADDEVALUATE_URL, null, mEvaluaMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"添加成功"}
                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UpData upData = gson.fromJson(s, UpData.class);

                            if (upData.getData().getSuccess().equals("1")) {

//                                new SweetAlertDialog(mContext)
//                                        .setTitleText(upData.getMsg())
//                                        .show();
//
////                                finish();

                                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(upData.getMsg())
//                                        .setContentText("Won't be able to recover this file!")
//                                        .setConfirmText("Yes,delete it!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sDialog.dismissWithAnimation();

                                                EvalEvent message=new EvalEvent();
                                                message.setMsg("EvalEvent");
                                                EventBus.getDefault().post(message);

                                                finish();

                                            }
                                        })
                                        .show();


                            } else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();

                            }
                        }

                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_evaluation;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.btn_put})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.btn_put:
                postData();
                break;

        }
    }

    @Override
    public void onEvaluItemClick(int position) {

        String name = leavedata.get(position).getName();
        String id = leavedata.get(position).getId();

        ygbeevaluate = id;

        ygbeevaluatemc = name;

    }

}
