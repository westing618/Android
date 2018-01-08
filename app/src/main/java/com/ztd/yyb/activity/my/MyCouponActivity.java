package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.MyCouponAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanCoup.CouponarrayBean;
import com.ztd.yyb.bean.beanCoup.DataCoup;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

/**
 * 我的优惠券
 * Created by  on 2017/3/22.
 */

public class MyCouponActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_allorders)
    TextView tv_allorders;
    @BindView(R.id.tv_ongoing)
    TextView tv_ongoing;
    @BindView(R.id.tv_completed)
    TextView tv_completed;

    @BindView(R.id.cou_recycler_view)
    RecyclerView mRecyclerView;

    private MyCouponAdapter mMyAdapter;

    private List<CouponarrayBean> mList=new ArrayList<>();

    private Map<String, String> mMytrMap = new HashMap<>();

    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据

    String state="1"; //状态 1:可用 2:已过期 3：已使用
    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("我的优惠券");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyCouponActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new MyCouponAdapter(MyCouponActivity.this,mList);
        mRecyclerView.setAdapter(mMyAdapter);

        SPUtil.put("COUPON", "1");

        getData();

    }

    private void getData() {


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

                            mList.clear();

                            Gson gson=new Gson();

                            DataCoup dataCoup = gson.fromJson(s, DataCoup.class);

                                if(dataCoup.getData().getSuccess().equals("1")){

                                    List<CouponarrayBean> couponarray = dataCoup.getData().getCouponarray();

                                    if(couponarray.size()==0){
                                        ToastUtil.show(MyCouponActivity.this,"没有数据");
                                        mMyAdapter.notifyDataSetChanged();
                                    } else {
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
        return R.layout.lay_coupon;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back,R.id.tv_allorders,R.id.tv_ongoing,R.id.tv_completed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_allorders:
                state="1";

                SPUtil.put("COUPON", "1");
                getData();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_red));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_66));
                tv_completed.setTextColor(getResources().getColor(R.color.color_66));



                break;
            case R.id.tv_ongoing:
                state="2";
                SPUtil.put("COUPON", "2");
                getData();
                tv_allorders.setTextColor(getResources().getColor(R.color.color_66));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_red));
                tv_completed.setTextColor(getResources().getColor(R.color.color_66));

                break;
            case R.id.tv_completed:
                state="3";
                SPUtil.put("COUPON", "3");
                getData();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_66));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_66));
                tv_completed.setTextColor(getResources().getColor(R.color.color_red));

                break;
        }
    }
}
