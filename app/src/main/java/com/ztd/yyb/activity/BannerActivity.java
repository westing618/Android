package com.ztd.yyb.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 展示广告
 * Created by  on 2017/3/20.
 */

public class BannerActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.web_banner)
    WebView WebView;

    @Override
    protected void initViewsAndEvents() {


        WebView.getSettings().setJavaScriptEnabled(true);//支持javascript

        String url = getIntent().getStringExtra(HomeActivity.PAR_KEY_BANNER);

//        Log.e("url","="+url);

        if (url == null) {
//            mTvTitle.setText("用户协议");
            WebView.loadUrl("http://api.yogobei.com/agreement.jsp");//用户协议地址
            WebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    mTvTitle.setText(view.getTitle());
                }
            });

//            Log.e("111111","111111");

        } else if(url.equals("")){
//            mTvTitle.setText("查看");
//            Log.e("22222","222222");

        }else {
//            Log.e("33333","33333");

            WebView.loadUrl(url);

            WebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    mTvTitle.setText(view.getTitle());
                }
            });

        }




    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_banner;
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
