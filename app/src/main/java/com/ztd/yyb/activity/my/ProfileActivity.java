package com.ztd.yyb.activity.my;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanPro.DataPro;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 应用简介
 * Created by  on 2017/5/12.
 */

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private Map<String, String> queryMap = new HashMap<>();

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_connevt)
    TextView tv_connevt;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("应用简介");

        tv_username.setText(""+getVersion());

        OkHttp3Utils.getInstance().doPost(Constants.APPINTRODUCE_URL, null, queryMap)
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
                    public void onNext(String s) {//DataPro

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();


                            DataPro dataPro = gson.fromJson(s, DataPro.class);

                            if(dataPro.getData().getSuccess().equals("1")){

                                String introduce = dataPro.getData().getIntroduce();
                                tv_connevt.setText("        "+introduce);

                            }



                        }

                    }
                });

    }


    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return this.getString(R.string.app_name) + version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.app_name);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_pro;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:

                finish();

                break;


        }
    }
}
