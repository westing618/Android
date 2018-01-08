package com.ztd.yyb.activity.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.ReleaseDemandAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanRlea.DataBean;
import com.ztd.yyb.bean.beanRlea.ReleaData;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.evenbean.DemanEvet;
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

/**
 * 用工发布 的工种搜索
 * Created by HH on 2017/7/30.
 */

public class ReleaseDemandActivity extends BaseActivity implements  ReleaseDemandAdapter.OnItemClickListener{

    @BindView(R.id.etv_seach)
    EditText mEdtvSeach;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<com.ztd.yyb.bean.beanRlea.GzdjlistBean> mList = new ArrayList<>();
//    private List<com.ztd.yyb.bean.beanRlea.GzdjlistBean> mListold = new ArrayList<>();

//    List<GzdjlistBean> gzdjlist;
//    AppConfigBean appConfigBean;

    ReleaseDemandAdapter releaseDemandAdapter;

    private Map<String, String> mSetAttendanceMap = new HashMap<>();

    String flag;//1 发布 2 需求

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("选择工种");
         releaseDemandAdapter=new ReleaseDemandAdapter(this,mList);
        releaseDemandAdapter.setmOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(releaseDemandAdapter);

        mEdtvSeach.addTextChangedListener(watcher);

         flag = getIntent().getStringExtra("flag");

        getDataList();

    }

    List<com.ztd.yyb.bean.beanRlea.GzdjlistBean> gzdjlist;

    private void getDataList() {
        String lcityid = (String) SPUtil.get("lcityid", "");
        mSetAttendanceMap.put("city",""+lcityid);//按照城市获取工种类型
//        LogUtil.e("123456", ""+lcityid);
        OkHttp3Utils.getInstance().doGet(Constants.GETLABOURCOST, null, mSetAttendanceMap)
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
//                        LogUtil.e("GETLABOURCOST", "="+s);
                        Gson gson=new Gson();
                        ReleaData releaData = gson.fromJson(s, ReleaData.class);
                        if("true".equals(releaData.getResult())){
                            mList.clear();
                            DataBean data = releaData.getData();
                            gzdjlist = data.getGzdjlist();
                            if(gzdjlist.size()==0){
                                ToastUtil.show(mContext,"该城市没有数据");
                             }else {
                                mList.addAll(gzdjlist);
                            }
                            releaseDemandAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtil.show(mContext,"没有数据");
                        }
                    }
                });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_release;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void onItemClick(View view, int position) {

        int ygblcid = mList.get(position).getYgblcid();
        String name = mList.get(position).getYgblcname();
        double ygblcprice = mList.get(position).getYgblcprice();



        if("1".equals(flag)){
            ChangBean chang=new ChangBean();
            chang.setId(""+ygblcid);
            chang.setName(name);
            chang.setPrice(""+ygblcprice);
            EventBus.getDefault().post(chang);
        }else  if("2".equals(flag)){
            DemanEvet degeEvent=new DemanEvet();
            degeEvent.setMsg("4");
            degeEvent.setId(""+ygblcid);
            degeEvent.setName(name);
            degeEvent.setPrice(""+ygblcprice);
            EventBus.getDefault().post(degeEvent);
        }

        finish();

    }

    @OnClick({R.id.lin_back, R.id.tv_seach})//

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_seach:
                String trim = mEdtvSeach.getText().toString().trim();
                if(TextUtils.isEmpty(trim)){
                    ToastUtil.show(mContext,"请输入搜索工种");
                }else {

                    mList.clear();

                    for (int i = 0; i < gzdjlist.size(); i++) {

                        String ygblcname = gzdjlist.get(i).getYgblcname();

                        if(ygblcname.contains(trim)){

                            com.ztd.yyb.bean.beanRlea.GzdjlistBean chanbean = new com.ztd.yyb.bean.beanRlea.GzdjlistBean();

                            String classname = gzdjlist.get(i).getYgblcname();
                            int ygblcid = gzdjlist.get(i).getYgblcid();
                            double ygblcprice = gzdjlist.get(i).getYgblcprice();

                            chanbean.setYgblcprice(ygblcprice);
                            chanbean.setYgblcname(classname);
                            chanbean.setYgblcid(ygblcid);
                            mList.add(chanbean);

                        }

                    }
                    releaseDemandAdapter.notifyDataSetChanged();
                    if(mList.size()==0){
                        ToastUtil.show(mContext,"没有相关信息");
                    }

                }

                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            String trim = mEdtvSeach.getText().toString().trim();
            if (TextUtils.isEmpty(trim)){
//                mList.clear();
//                for (int i = 0; i < gzdjlist.size(); i++) {
//                    ChangBean chanbean = new ChangBean();
//                    String classname = gzdjlist.get(i).getYgblcname();
//                    String classcd = gzdjlist.get(i).getYgblcid();
//                    String ygblcprice = gzdjlist.get(i).getYgblcprice();
//                    chanbean.setPrice(ygblcprice);
//                    chanbean.setName(classname);
//                    chanbean.setId(classcd);
//                    mList.add(chanbean);
//                }
                mList.addAll(gzdjlist);
                releaseDemandAdapter.notifyDataSetChanged();
            }
        }
    };
}
