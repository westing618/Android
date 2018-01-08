package com.ztd.yyb.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.InvoiceAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanInvoice.Datainvoice;
import com.ztd.yyb.bean.beanInvoice.ReceiptinfoBean;
import com.ztd.yyb.evenbean.InvoiceEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

import static com.ztd.yyb.R.id.lin_top_right;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static rx.Observable.timer;

/**
 * 发票
 * Created by  on 2017/3/28.
 */

public class InvoiceActivity extends BaseActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate{

    private List<ReceiptinfoBean> mList=new ArrayList<>();

    @BindView(R.id.invoce_recyclerview)
    RecyclerView mRecyclerView;

    InvoiceAdapter mMyAdapter;

    private Map<String, String> mMytrMap = new HashMap<>();

    private int mPageNum = 1;//页码
    private int mPageSize = 10;//一次获取几条数据

    double sumamount;

    private BGARefreshLayout mRefreshLayout;

    @Override
    protected void initViewsAndEvents() {

        EventBus.getDefault().register(this);

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_invoce);
        mRefreshLayout.setDelegate(this);

        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new InvoiceAdapter(this,mList);
        mRecyclerView.setAdapter(mMyAdapter);

        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(InvoiceEvent message) {
        String msg = message.getMsg();// MakeInvoice 开票成功 接受 通知刷新
        if(msg.equals("MakeInvoice")){

            getData();
        }
    }

    private void getData() {

        mMytrMap.clear();

        String  userid = (String) SPUtil.get(USERID, "");
        mMytrMap.put("userid",""+userid);
        mMytrMap.put("pageNumber", String.valueOf(mPageNum));
        mMytrMap.put("pageSize", String.valueOf(mPageSize));

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.RECEIOLIST_URL, null, mMytrMap)
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

                            Datainvoice datainvoice = gson.fromJson(s, Datainvoice.class);

                            if(datainvoice.getData().getSuccess().equals("1")){

                                sumamount = datainvoice.getData().getSumamount();//可开票金额

                                List<ReceiptinfoBean> receiptinfo = datainvoice.getData().getReceiptinfo();


                                if(receiptinfo.size()==0){

                                    ToastUtil.show(InvoiceActivity.this,"没有数据");

//                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
//                                            .setTitleText("没有数据")
////                                            .setContentText("链接服务器失败")
//                                            .show();

                                }else {

                                    if(mPageNum==1){
                                        mList.clear();
                                    }

                                    mList.addAll(receiptinfo);
                                    mMyAdapter.notifyDataSetChanged();

                                }

                            }

                        }
                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_lnvoice;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back, R.id.lin_top_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case lin_top_right:

//                if(sumamount < 1){
//                    ToastUtil.show(mContext,"没有可开票金额");
//                } else {

                    Intent  inen= new Intent(this, MakeInvoiceActivity.class);
//                    inen.putExtra("sumamount",""+sumamount);
                    startActivity(inen);

//                }


                break;


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
