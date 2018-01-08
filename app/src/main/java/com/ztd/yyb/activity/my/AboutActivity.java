package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 * Created by  on 2017/5/12.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_username)
    TextView tv_username;

//    @BindView(R.id.ll_two)
//    LinearLayout ll_two;
//
//    @BindView(R.id.ll_one)
//    LinearLayout ll_one;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("关于我们");

        tv_username.setText(""+getVersion());
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "用工贝" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "用工贝";
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_about;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.ll_two,R.id.ll_one})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_one:
//                Log.e("ll_one","ll_one");
                startActivity(new Intent(mContext, ProfileActivity.class));
                break;
            case R.id.ll_two:
                startActivity(new Intent(mContext, FeedbackActivity.class));
                break;

        }
    }
}
