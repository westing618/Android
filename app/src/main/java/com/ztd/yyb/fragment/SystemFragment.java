package com.ztd.yyb.fragment;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.MessageAdapter;
import com.ztd.yyb.base.BaseLazyFragment;
import com.ztd.yyb.bean.beanMess.DataMess;
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
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;
import static rx.Observable.timer;

/**
 * 系统消息
 * Created by  on 2017/4/19.
 */

public class SystemFragment extends BaseLazyFragment
{

    private Map<String, String> mSystemLimitMap = new HashMap<>();

    List<DataMess.DataEntity.NoticearrayEntity> leavedata = new ArrayList<>();

    @BindView(R.id.mess_recycler)
    RecyclerView mRecyclerView;


    @BindView(R.id.refresh)
    TwinklingRefreshLayout mRefreshLayout;
    MessageAdapter messageadapter;

    @Override
    protected void initViewsAndEvents() {

        messageadapter = new MessageAdapter(getActivity(), leavedata);
        mRecyclerView.setAdapter(messageadapter);

        getDataSys();


        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        getDataSys();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });


    }

    private void getDataSys() {

        mSystemLimitMap.clear();

        String userid = (String) SPUtil.get(USERID, "");
        mSystemLimitMap.put("userid", userid);

        OkHttp3Utils.getInstance().doPost(Constants.GETSYSTEMNOTICE_URL, null, mSystemLimitMap)
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
                            leavedata.clear();

                            Gson gson = new Gson();

                            DataMess dataMess = gson.fromJson(s, DataMess.class);

                            if (dataMess.getData().getSuccess().equals("1")) {
                                List<DataMess.DataEntity.NoticearrayEntity> noticearray
                                        = dataMess.getData().getNoticearray();
                                if(noticearray.size()==0){
//                                    ToastUtil.show(mContext,"没有相关订单");
                                }else {
                                    leavedata.addAll(noticearray);
                                    messageadapter.notifyDataSetChanged();
                                }

                            }

                        }

                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_message;

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//        timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//                .map(new Func1<Long, Object>() {
//                    @Override
//                    public Object call(Long aLong) {
//
//                        getDataSys();
//
//                        mRefreshLayout.endRefreshing();
//
//                        return null;
//                    }
//                }).subscribe();
//    }

//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        return false;
//    }
}
