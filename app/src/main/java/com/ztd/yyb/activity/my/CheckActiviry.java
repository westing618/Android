package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by  on 2017/4/20.
 */

public class CheckActiviry extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("审核");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_check;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back,R.id.check_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.check_ok:

                startActivity(new Intent(this, MyCertification.class));
                    finish();

                break;
        }
    }


}
