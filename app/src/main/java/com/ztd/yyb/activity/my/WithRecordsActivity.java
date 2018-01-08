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
import com.ztd.yyb.adapter.WithRecorAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanWith.DataWithR;
import com.ztd.yyb.bean.beanWith.EvaarrayBean;
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
 * 提现  提现记录
 * Created by  on 2017/5/12.
 */

public class WithRecordsActivity extends BaseActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Map<String, String> mMytrMap = new HashMap<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据


    @BindView(R.id.mwith_recyclerview)
    RecyclerView mwith_recyclerview;

    private BGARefreshLayout mRefreshLayout;
    WithRecorAdapter transactAdapter;

    List<EvaarrayBean> leavedata =new ArrayList<>();

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("提现记录");

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_withmtransac);
        mRefreshLayout.setDelegate(this);

        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));

        transactAdapter =new WithRecorAdapter(this,leavedata);
        mwith_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mwith_recyclerview.setAdapter(transactAdapter);

        getData();
    }

    private void getData() {

        mMytrMap.clear();

        String  userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("userid",""+userid);
        mMytrMap.put("pageNumber", String.valueOf(mPageNum));
        mMytrMap.put("pageSize", String.valueOf(mPageSize));

        Log.e("mPageNum","="+mPageNum);

        OkHttp3Utils.getInstance().doPost(Constants.MONEYHISTORY_URL, null, mMytrMap)
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

//                            leavedata.clear();

                            Gson gson=new Gson();
                            DataWithR dataWithR = gson.fromJson(s, DataWithR.class);
                            String success = dataWithR.getData().getSuccess();
                            if(success.equals("1")){

                                List<EvaarrayBean> evaarray = dataWithR.getData().getEvaarray();

                                if(evaarray.size()==0){

                                    ToastUtil.show(mContext,"没有记录");

                                }else {

                                    if(mPageNum==1){
                                        leavedata.clear();
                                    }

                                    leavedata.addAll(evaarray);
                                    transactAdapter.notifyDataSetChanged();
                                }
                            }
                        }


                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {

        return R.layout.lay_withrecords;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

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
    @OnClick(R.id.lin_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
        }
    }
}
