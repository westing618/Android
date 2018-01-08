package com.ztd.yyb.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

/**
 * 欢迎 界面
 * Created by  on 2017/4/24.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void initViewsAndEvents() {


        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                // 第一次
                SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                boolean isFirst = sp.getBoolean("isFirst", true);
                if (isFirst) {
                    intent.setClass(SplashActivity.this, GuideActivity.class);
                    sp.edit().putBoolean("isFirst", false).commit();
                } else {
                    intent.setClass(SplashActivity.this, HomeActivity.class);
                }
                // 第二次及之后
                startActivity(intent);
                finish();
            }
        }, 1000);


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_splash;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
}
