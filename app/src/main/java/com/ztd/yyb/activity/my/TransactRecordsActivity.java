package com.ztd.yyb.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.TransactAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanTran.MoneyarrayBean;
import com.ztd.yyb.bean.beanTran.MoneyarrayBeanX;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;
import static rx.Observable.timer;

/**
 * 我的钱包 -- 交易记录
 * Created by  on 2017/3/13.
 */

public class TransactRecordsActivity extends BaseActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate {
    TransactAdapter transactAdapter;
    List<MoneyarrayBeanX> Leavedata=new ArrayList<>();
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_totaloutput)
    TextView tv_totaloutput;
    @BindView(R.id.tv_totalinput)
    TextView tv_totalinput;

    @BindView(R.id.mtransac_recycler_view)
    RecyclerView mtransac_recycler_view;

    private Map<String, String> mMytrMap = new HashMap<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据

    private BGARefreshLayout mRefreshLayout;

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("交易记录");

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_mtransac);
        mRefreshLayout.setDelegate(this);

        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));

        transactAdapter =new TransactAdapter(this,Leavedata);
        mtransac_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        mtransac_recycler_view.setAdapter(transactAdapter);

        getData();

    }
    private void getData() {

        mMytrMap.clear();

        String  userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("userid",""+userid);
        mMytrMap.put("pageNumber", String.valueOf(mPageNum));
        mMytrMap.put("pageSize", String.valueOf(mPageSize));

        Log.e("mPageNum","="+mPageNum);

        OkHttp3Utils.getInstance().doPost(Constants.MY_PAYHOSTORY_URL, null, mMytrMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(String s) {

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson=new Gson();

                            MoneyarrayBean tranData = gson.fromJson(s, MoneyarrayBean.class);

                            if(tranData.getData().getSuccess().equals("1")){

                                String totalinput = tranData.getData().getTotalinput();  // totalinput：总收入
                                String totaloutput = tranData.getData().getTotaloutput();//totaloutput：总支出

                                tv_totaloutput.setText("￥"+totaloutput);
                                tv_totalinput.setText("￥"+totalinput);

                                List<MoneyarrayBeanX> moneyarray = tranData.getData().getMoneyarray();

                                if(moneyarray.size()==0){
                                    ToastUtil.show(mContext,"没有相关数据");
                                } else {

                                    if( mPageNum == 1){
                                        Leavedata.clear();
                                    }

                                    Leavedata.addAll(moneyarray);
                                    transactAdapter.notifyDataSetChanged();

                                }

                            }
                        }


                    }
                });

    }
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_transactrecords;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }
    @OnClick(R.id.lin_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {

                        mPageNum = 1;

                        getData();

                        mRefreshLayout.endRefreshing();

                        return null;
                    }
                }).subscribe();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        mPageNum++;

        timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {

                        getData();
                        mRefreshLayout.endLoadingMore();

                        return null;
                    }
                }).subscribe();

        return true;
    }
}
