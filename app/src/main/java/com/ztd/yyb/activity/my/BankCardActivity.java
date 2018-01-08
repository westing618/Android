package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡
 * Created by  on 2017/4/6.
 */

public class BankCardActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_bankname)
    TextView tv_bankname;

    @BindView(R.id.tv_banknum)
    TextView tv_banknum;

    @BindView(R.id.re_yes)
    RelativeLayout re_yes;

    @BindView(R.id.re_no)
    RelativeLayout re_no;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("银行卡");

        Intent intent = getIntent();

        String flag = intent.getStringExtra("FLAG");// ygisbindcard:是否绑定银行卡  0:否 1:是

        if(flag.equals("1")){

            re_yes.setVisibility(View.VISIBLE);
            re_no.setVisibility(View.GONE);

        }else if (flag.equals("0")){

            re_no.setVisibility(View.VISIBLE);
            re_yes.setVisibility(View.GONE);
        }

        String ygbmbank = intent.getStringExtra("ygbmbank");

        String ygbmcard = intent.getStringExtra("ygbmcard");

        tv_bankname.setText(""+ygbmbank);

        if(!TextUtils.isEmpty(ygbmcard)){

            String substr=ygbmcard.substring(ygbmcard.length()-4,ygbmcard.length());
            tv_banknum.setText("****  ****  ****  "+substr);

        }



    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_bankcard;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.tv_bancark,R.id.re_yes})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.re_yes:

                startActivity(new Intent(mContext, WithdrawalActivity.class));//提现


                break;

            case R.id.tv_bancark:

                startActivity(new Intent(mContext, BDBankCardActivity.class));

                finish();

                break;
        }

    }

}
