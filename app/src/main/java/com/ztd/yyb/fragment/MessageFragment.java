package com.ztd.yyb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息（外面）
 * Created by  on 2017/4/19.
 */

public class MessageFragment extends Fragment {

    private List<Fragment> mFragments = new ArrayList<Fragment>();
    ViewPager viewPager;
    TextView mtvParents;
    TextView mtvTteacher;
    ImageView img_dpointone,img_cpointtwo;

    private boolean state = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details, null);

        viewPager = (ViewPager) view.findViewById(R.id.mess_viewpager);

        mtvParents=(TextView)view.findViewById(R.id.tv_parents);
        mtvTteacher=(TextView)view.findViewById(R.id.tv_teacher);

        img_dpointone=(ImageView)view.findViewById(R.id.img_dpointone);
        img_cpointtwo=(ImageView)view.findViewById(R.id.img_cpointtwo);


        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);}

        String message = (String) SPUtil.get("message", "");
        if (message != null) {
//-------------------- 控制 定制消息 红包提醒
            if("{}".equals(message)){
                state = true;
            }else {
                state = false;
            }
        }
//-------------------- 控制 系统消息 提示
        if (state) {
            String mymessage = (String) SPUtil.get("messageFragment", "");
            if ("1".equals(mymessage)) {
                img_dpointone.setVisibility(View.VISIBLE);
            } else if("3".equals(mymessage)){
                img_cpointtwo.setVisibility(View.VISIBLE);
            }else {
                img_dpointone.setVisibility(View.INVISIBLE);
                img_cpointtwo.setVisibility(View.INVISIBLE);
            }
        }



        view.findViewById(R.id.ll_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtil.put("messageFragment", "2");
                img_dpointone.setVisibility(View.INVISIBLE);
                viewPager.setCurrentItem(0);
                mtvParents.setTextColor(getResources().getColor(R.color.themecolor));
                mtvTteacher.setTextColor(getResources().getColor(R.color.color_66));
            }
        });
        view.findViewById(R.id.ll_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                SPUtil.put("messageFragment", "2");
                img_cpointtwo.setVisibility(View.INVISIBLE);
                mtvTteacher.setTextColor(getResources().getColor(R.color.themecolor));
                mtvParents.setTextColor(getResources().getColor(R.color.color_66));
            }
        });
        view.findViewById(R.id.lin_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });




        mFragments.add(new SystemFragment());
        mFragments.add(new ConversationListFragment());

        FragmentManager fm = getChildFragmentManager();
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(fm) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };

        viewPager.setAdapter(mAdapter);

        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAddtBus(String message) {

        if (message != null) {
//-------------------- 控制 定制消息 红包提醒
            if("{}".equals(message)){
                state = true;
            }else {
                state = false;
            }
        }
//-------------------- 控制 系统消息 提示
        if (state) {
            String mymessage = (String) SPUtil.get("messageFragment", "");
            if ("1".equals(mymessage)) {
                img_dpointone.setVisibility(View.VISIBLE);
            } else if("3".equals(mymessage)){
                img_cpointtwo.setVisibility(View.VISIBLE);
            }else {
                img_cpointtwo.setVisibility(View.INVISIBLE);
                img_dpointone.setVisibility(View.INVISIBLE);
            }
        }

    }

}
