package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ChooseCouponAdapter;
import com.ztd.yyb.adapter.MyCouponAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanCoup.CouponarrayBean;
import com.ztd.yyb.bean.beanCoup.DataCoup;
import com.ztd.yyb.evenbean.CouEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.R.id.tv_noChack;
import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 选择 优惠券
 * Created by  on 2017/5/8.
 */

public class ChooseCouponActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;


    @BindView(R.id.tv_noChack)
    TextView tv_noChack;

    @BindView(R.id.cou_recycler_view)
    RecyclerView mRecyclerView;

    private ChooseCouponAdapter mMyAdapter;
    private List<CouponarrayBean> mList=new ArrayList<>();

    private Map<String, String> mMytrMap = new HashMap<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据

    String state="1"; //状态 1:可用 2:已过期 3：已使用

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("选择优惠券");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChooseCouponActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mMyAdapter = new ChooseCouponAdapter(ChooseCouponActivity.this,mList);
        mRecyclerView.setAdapter(mMyAdapter);

        getData();

    }
    private void getData() {

        mList.clear();

        mMytrMap.clear();

        String  userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("userid",""+userid);
        mMytrMap.put("state",""+state);
        mMytrMap.put("pageNumber", String.valueOf(mPageNum));
        mMytrMap.put("pageSize", String.valueOf(mPageSize));

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.COUPONLIST_URL, null, mMytrMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dismissLoadingDialog();
                    }
                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                    }
                    @Override
                    public void onNext(String s) {
                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson=new Gson();

                            DataCoup dataCoup = gson.fromJson(s, DataCoup.class);

                            if(dataCoup.getData().getSuccess().equals("1")){

                                List<CouponarrayBean> couponarray = dataCoup.getData().getCouponarray();

                                if(couponarray.size()==0){

                                    ToastUtil.show(ChooseCouponActivity.this,"没有数据");

                                }else {

                                    tv_noChack.setVisibility(View.VISIBLE);
                                    mList.addAll(couponarray);
                                    mMyAdapter.notifyDataSetChanged();

                                }
                            }

                        }
                    }
                });

    }
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_chooscoupon;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back, R.id.tv_noChack})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.tv_noChack:

                CouEvent couEvent = new CouEvent();
//                couEvent.setId("00000000000000");
//                couEvent.setMsg(ygbctitle);
//                couEvent.setProce(""+ygbcamount);
                couEvent.setFlag(false);//不使用优惠券
                EventBus.getDefault().post(couEvent);

                finish();


                break;


        }
    }
}
