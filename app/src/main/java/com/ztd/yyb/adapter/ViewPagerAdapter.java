package com.ztd.yyb.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.ztd.yyb.activity.HomeActivity;

import java.util.ArrayList;

/**
 *
 * @author YeChao
 * @功能描述：ViewPager适配器，用来绑定数据和view
 */
public class ViewPagerAdapter extends PagerAdapter{
    //界面列表
    private ArrayList<View> views;
    public Activity context;
    public ViewPagerAdapter(ArrayList<View> views,Activity context)
    {
        this.views = views;
        this.context = context;
    }

    /**
     * 获得当前界面数
     */
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        else return 0;
    }

    /**
     * 判断是否由对象生成界面
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    /**
     * 销毁position位置的界面
     */
    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    /**
     * 初始化position位置的界面
     */
    @Override
    public Object instantiateItem(View container, final int position) {
        ((ViewPager) container).addView(views.get(position), 0);

        views.get(position).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(position == 2){

                    Intent intent=new Intent(context,HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();

                }

            }
        });

        return views.get(position);
    }

}
