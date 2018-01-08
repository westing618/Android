package com.ztd.yyb.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.fragment.DemandFragment;
import com.ztd.yyb.fragment.MessageFragment;
import com.ztd.yyb.fragment.MianFragment;
import com.ztd.yyb.fragment.MyFragment;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.view.MyQViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by  on 2017/7/31.
 */

public class MainNActivity extends FragmentActivity {
    public static MyQViewPager mViewpager;

    @BindView(R.id.iv_hr_main_my)
    ImageView ivHrMainMy;//首页
    @BindView(R.id.tv_hr_main_my)
    TextView tv_hr_main_my;//

    @BindView(R.id.iv_hr_main_application)
    ImageView ivHrMainApplication;//需求
    @BindView(R.id.tv_hr_main_application)
    TextView tv_hr_main_application;//

    @BindView(R.id.iv_hr_main_message)
    ImageView ivHrMainMessage;//消息
    @BindView(R.id.tv_hr_main_message)
    TextView tv_hr_main_message;//

    @BindView(R.id.tv_tipcnt)
    TextView tvTipcnt;// 消息数量
    @BindView(R.id.tv_tipcnt2)
    TextView tvMTipcnt;// 我的 消息数量

    @BindView(R.id.iv_hr_main_communication)// 我的
            ImageView ivHrMainCommunication;
    @BindView(R.id.tv_hr_main_communication)
    TextView tv_hr_main_communication;//

    private List<Fragment> mFragments = new ArrayList<>();
    private boolean state = true;
    private int num=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_mainm);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mViewpager = (MyQViewPager) findViewById(R.id.viewpager);

        mFragments.clear();
        mFragments.add(new MianFragment());
        mFragments.add(new DemandFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new MyFragment());

        FragmentManager fm = getSupportFragmentManager();
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        mViewpager.setAdapter(mAdapter);
        mViewpager.setNoScroll(true);//禁止左右滑动
        String message = (String) SPUtil.get("message", "");
        if (message != null) {
//-------------------- 控制 定制消息 红包提醒
            if("{}".equals(message)){
                state = true;
            }else {
                state = false;
                try {
                    Gson gson = new Gson();
                    JsonRootBean jsonRootBean = gson.fromJson(message, JsonRootBean.class);
                    String isAccount = jsonRootBean.getIsAccount();

                    if ("1".equals(isAccount)) {
                        tvMTipcnt.setVisibility(View.VISIBLE);
                    } else {
                        tvMTipcnt.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }

            }

        }
//-------------------- 控制 系统消息 提示
        if (state) {
            String mymessage = (String) SPUtil.get("messageFragment", "");
            if ("1".equals(mymessage)) {
                num++;
                tvTipcnt.setVisibility(View.VISIBLE);
                tvTipcnt.setText(""+num);
            } else if("3".equals(mymessage)){
                num++;
                tvTipcnt.setVisibility(View.VISIBLE);
                tvTipcnt.setText(""+num);
            }else {
                tvTipcnt.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Subscribe
    public void onEventMainThread(DemanEvet degeEvent) {
        String msg = degeEvent.getMsg();
        String id = degeEvent.getId();
        if("Deman".equals(msg)){
            if("1".equals(id)){
                setColandImag(1);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAddtBus(String message) {
        if (message != null) {
//-------------------- 控制 定制消息 红包提醒
            if("{}".equals(message)){
                state = true;
            }else {
                state = false;
                try {
                    Gson gson = new Gson();
                    JsonRootBean jsonRootBean = gson.fromJson(message, JsonRootBean.class);
                    String isAccount = jsonRootBean.getIsAccount();
                    if ("1".equals(isAccount)) {
                        tvMTipcnt.setVisibility(View.VISIBLE);
                    } else {
                        tvMTipcnt.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }

            }

        }
//-------------------- 控制 系统消息 提示  1 系统消息    3 聊天的消息
        if (state) {
            String mymessage = (String) SPUtil.get("messageFragment", "");
            if ("1".equals(mymessage)) {
                num++;
                tvTipcnt.setVisibility(View.VISIBLE);
                tvTipcnt.setText(""+num);
            } else if("3".equals(mymessage)){
                num++;
                tvTipcnt.setVisibility(View.VISIBLE);
                tvTipcnt.setText(""+num);
            }else {
                tvTipcnt.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick({R.id.ll_hr_main_my, R.id.ll_hr_main_communication,
            R.id.ll_hr_main_message, R.id.ll_hr_main_application})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_hr_main_my://首页
                mViewpager.setCurrentItem(0);
                setColandImag(0);
                break;
            case R.id.ll_hr_main_communication://需求
                mViewpager.setCurrentItem(1);
                setColandImag(1);
                break;
            case R.id.ll_hr_main_message://消息
                num=0;
//                SPUtil.put("messageFragment", "2");
                tvTipcnt.setVisibility(View.INVISIBLE);
                mViewpager.setCurrentItem(2);
                setColandImag(2);
                break;
            case R.id.ll_hr_main_application://我的
//                numo=0;
//                SPUtil.put("message", "");
                tvMTipcnt.setVisibility(View.INVISIBLE);
                mViewpager.setCurrentItem(3);
                setColandImag(3);
                break;
        }
    }

    private void setColandImag(int i) {

        if (i == 0) {

            ivHrMainMy.setImageResource(R.mipmap.tab_btn_index_sel);
            tv_hr_main_my.setTextColor(getResources().getColor(R.color.color_reb));

            ivHrMainApplication.setImageResource(R.mipmap.tab_btn_demand_nor);
            tv_hr_main_application.setTextColor(getResources().getColor(R.color.tab_text));


            ivHrMainMessage.setImageResource(R.mipmap.tab_btn_msg_nor);
            tv_hr_main_message.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainCommunication.setImageResource(R.mipmap.tab_btn_index_my);
            tv_hr_main_communication.setTextColor(getResources().getColor(R.color.tab_text));

        } else if (i == 1) {

            ivHrMainMy.setImageResource(R.mipmap.tab_btn_index_nor);
            tv_hr_main_my.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainApplication.setImageResource(R.mipmap.tab_btn_demand_sel);
            tv_hr_main_application.setTextColor(getResources().getColor(R.color.color_reb));

            ivHrMainMessage.setImageResource(R.mipmap.tab_btn_msg_nor);
            tv_hr_main_message.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainCommunication.setImageResource(R.mipmap.tab_btn_index_my);
            tv_hr_main_communication.setTextColor(getResources().getColor(R.color.tab_text));

        } else if (i == 2) {

            ivHrMainMy.setImageResource(R.mipmap.tab_btn_index_nor);
            tv_hr_main_my.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainApplication.setImageResource(R.mipmap.tab_btn_demand_nor);
            tv_hr_main_application.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainMessage.setImageResource(R.mipmap.tab_btn_msg_sel);
            tv_hr_main_message.setTextColor(getResources().getColor(R.color.color_reb));

            ivHrMainCommunication.setImageResource(R.mipmap.tab_btn_index_my);
            tv_hr_main_communication.setTextColor(getResources().getColor(R.color.tab_text));

        } else if (i == 3) {

            ivHrMainMy.setImageResource(R.mipmap.tab_btn_index_nor);
            tv_hr_main_my.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainApplication.setImageResource(R.mipmap.tab_btn_demand_nor);
            tv_hr_main_application.setTextColor(getResources().getColor(R.color.tab_text));


            ivHrMainMessage.setImageResource(R.mipmap.tab_btn_msg_nor);
            tv_hr_main_message.setTextColor(getResources().getColor(R.color.tab_text));

            ivHrMainCommunication.setImageResource(R.mipmap.tab_btn_my_sel);
            tv_hr_main_communication.setTextColor(getResources().getColor(R.color.color_reb));
        }

    }

    public class JsonRootBean {
        private String isAccount;

        public String getIsAccount() {
            return isAccount;
        }

        public void setIsAccount(String isAccount) {
            this.isAccount = isAccount;
        }

    }
}
