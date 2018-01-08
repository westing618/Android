package com.ztd.yyb.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.activity.my.MyOrderDetailsActivity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 抢单成功
 * Created by  on 2017/3/20.
 */

public class GrabOrderSuccessActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    String type;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("抢单成功");
        type = (String) SPUtil.get("puttype","");

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_grabordersuccess;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.tv_chakan,R.id.tv_backhome})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;

            case R.id.tv_chakan:
                if(type.equals("1")){//师傅详情

                    Intent intent=new Intent(mContext,MyOrderDetailsActivity.class);

                    intent.putExtra("MM","7");

                    startActivity(intent);

                }else if(type.equals("2")){//家教详情

                    Intent intent2=new Intent(mContext,MyOrderDetailsActivity.class);

                    intent2.putExtra("MM","8");

                    startActivity(intent2);
                }
                break;

            case R.id.tv_backhome:
                startActivity(new Intent(GrabOrderSuccessActivity.this, MainActivity.class));
                break;

        }
    }


}
