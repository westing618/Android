package com.ztd.yyb.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ViewPagerAdapter;
import com.ztd.yyb.base.BaseActivity;

import java.util.ArrayList;

/**
 * 引导界面
 * Created by  on 2017/4/24.
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener ,View.OnClickListener {

    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private static final int[] pics = { R.mipmap.guide_page_one, R.mipmap.guide_page_two,
            R.mipmap.guide_page_three };

    @Override
    protected void initViewsAndEvents() {
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views,this);


        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {

            @Override
            public void transformPage(View view, float position) {

                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                view.setScaleX(normalizedposition / 2 + 0.5f);
                view.setScaleY(normalizedposition / 2 + 0.5f);

            }
        });


        initData();

    }


    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            //防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载图片资源
            iv.setImageResource(pics[i]);
            views.add(iv);
        }

        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);

//        // 初始化底部小点
//        initPoint();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_guide;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }
}
