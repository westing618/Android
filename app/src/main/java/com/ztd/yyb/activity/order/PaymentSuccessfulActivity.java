package com.ztd.yyb.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.activity.my.MyOrderDetailsActivity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 付款 成功
 * Created by  on 2017/3/20.
 */

public class PaymentSuccessfulActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

//    private Map<String, String> mPutOrederMap = new HashMap<>();//查看发布订单详情

    String ygboid;
    String type;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("付款成功");

        ygboid = (String) SPUtil.get("putorderid","");

        type = (String) SPUtil.get("puttype","");


        DemanEvet messageEvent = new DemanEvet();
        messageEvent.setMsg("paysucc");
        EventBus.getDefault().post(messageEvent);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_paymentsuccessful;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back,R.id.tv_check,R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:

                finish();

                break;

            case R.id.tv_check://1 师傅 2家教

                if(type.equals("1")){//师傅详情

                    Intent intent=new Intent(mContext,MyOrderDetailsActivity.class);

                    intent.putExtra("MM","4");

                    startActivity(intent);

                }else if(type.equals("2")){//家教详情

                    Intent intent2=new Intent(mContext,MyOrderDetailsActivity.class);

                    intent2.putExtra("MM","3");

                    startActivity(intent2);
                }

                break;

            case R.id.tv_back:

                startActivity(new Intent(PaymentSuccessfulActivity.this, MainActivity.class));

                break;
        }
    }





}
