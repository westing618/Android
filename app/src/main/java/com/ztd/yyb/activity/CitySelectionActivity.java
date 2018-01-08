package com.ztd.yyb.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.CitySelectionAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.CitylistBean;
import com.ztd.yyb.evenbean.MessageEvent;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;

/**
 * 城市选择
 * Created by  on 2017/3/20.
 */

public class CitySelectionActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.city_recycler_view)
    RecyclerView city_recycler;

    private List<CitylistBean> mCityList=new ArrayList<>();

    CitySelectionAdapter citySelectionAdapter;
    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("城市选择");

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");
        Gson gson=new Gson();
        if(!TextUtils.isEmpty(myappconfig)){
            AppConfigBean appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            List<CitylistBean> citylist = appConfigBean.getData().getCitylist();
            mCityList.addAll(citylist);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        city_recycler.setLayoutManager(linearLayoutManager);
        citySelectionAdapter=new CitySelectionAdapter(CitySelectionActivity.this, mCityList,
                new CitySelectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String s = mCityList.get(position).getLcity();
                String lcitymc = mCityList.get(position).getLcitymc();

                MessageEvent messageEvent=new MessageEvent();
                messageEvent.setMsg(lcitymc);
                messageEvent.setId(s);

                EventBus.getDefault().post(messageEvent);

                        finish();
            }
        });
        city_recycler.setAdapter(citySelectionAdapter);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_citycelection;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick(R.id.lin_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
        }
    }

}
