package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanFeed.DataFeed;
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
 * Created by  on 2017/5/15.
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_content)
    TextView et_content;
    private Map<String, String> mMytrMap = new HashMap<>();

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("反馈");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_feedback;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.btn_submit})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.btn_submit:

                String s = et_content.getText().toString();

                if (TextUtils.isEmpty(s)) {

                    ToastUtil.show(mContext, "请输入反馈内容");

                } else {

                    postSubmit(s);

                }

                break;

        }
    }

    private void postSubmit(String s) {

        String userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("ygbmid", userid);
        mMytrMap.put("ygbfcontent", s);


        OkHttp3Utils.getInstance().doPost(Constants.ADDFEEDBACK_URL, null, mMytrMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"提交成功"}
                        Gson gson = new Gson();

                        if (!TextUtils.isEmpty(s)) {

                            DataFeed dataFeed = gson.fromJson(s, DataFeed.class);

                            if (dataFeed.getData().getSuccess().equals("1")) {

                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(dataFeed.getMsg())
//                                        .setContentText("Won't be able to recover this file!")
                                        .setConfirmText("确定")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                finish();
                                            }
                                        })
                                        .show();

                            } else {
                                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(dataFeed.getMsg())
                                        .setContentText("确定")
                                        .show();


                            }
                        }

                    }
                });
    }


}
