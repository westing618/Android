package com.ztd.yyb.activity.order;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanDeman.DataDemanYg;
import com.ztd.yyb.bean.beanDeman.OrderYginfoBean;
import com.ztd.yyb.bean.beanDemanJ.DataDemanJj;
import com.ztd.yyb.bean.beanDemanJ.OrderJjinfoBean;
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
 * Created by  on 2017/3/20.
 */

public class SearchActivity extends BaseActivity {

    private Map<String, String> mSearchMap = new HashMap<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 20;//一次获取几条数据

    @BindView(R.id.etv_seach)
    EditText etv_seach;

    private List<OrderYginfoBean> mYgList = new ArrayList<>();
    private List<OrderJjinfoBean> mTList = new ArrayList<>();

    SearchAdapter searchAdapter;
    private Boolean genre;

    String type;//用工类型 1 师傅 2家教

    @Override
    protected void initViewsAndEvents() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        searchAdapter=new SearchAdapter(this, mTList, mYgList);

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(searchAdapter);

        genre = (Boolean) SPUtil.get(HomeActivity.TYPE, true);//true 用工  flase 学堂


        if (genre) {//true 用工  flase 学堂

            type = "1";
            etv_seach.setHint("输入工种或地址关键词搜索");

        } else {
            type = "2";
            etv_seach.setHint("输入科目或地址关键词搜索");

        }

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_search;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @OnClick({R.id.lin_back,R.id.tv_seach})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;

            case R.id.tv_seach:


                String trim = etv_seach.getText().toString().trim();

                if(TextUtils.isEmpty(trim)){

                    if (genre) {

                        ToastUtil.show(mContext,"请输入工种或地址关键字搜索");

                    }else {

                        ToastUtil.show(mContext,"输入科目或地址关键词搜索");

                    }



                    return;
                }


                seach(trim);

                break;

        }
    }

    private void seach(String trim) {

        String   userid = (String) SPUtil.get(USERID, "");

        mSearchMap.clear();

        mSearchMap.put("userid", userid);
        mSearchMap.put("type", type);
        mSearchMap.put("keywords", trim);

        mSearchMap.put("pageNumber", String.valueOf(mPageNum));
        mSearchMap.put("pageSize", String.valueOf(mPageSize));

        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.DEMANDLIST_URL, null, mSearchMap)
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
                        Log.e("onNext","="+s);

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            mYgList.clear();
                            mTList.clear();

                            if (type.equals("1")) {

                                getYongGonData(s);

                            } else if (type.equals("2")) {

                                getTeacherData(s);
                            }
                        } else {
                            ToastUtil.show(mContext, "服务器没有数据");
                        }


                    }
                });

    }


    private void getYongGonData(String s) {

        Gson gson = new Gson();

        DataDemanYg dataDemanYg = gson.fromJson(s, DataDemanYg.class);

        if (dataDemanYg.getData().getSuccess()==1) {

            List<OrderYginfoBean> orderYginfo = dataDemanYg.getData().getOrderYginfo();

            if (orderYginfo.size() == 0) {

//                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("没有相关订单")
//                        .show();

                ToastUtil.show(mContext,"没有相关订单");

            }else {

                mYgList.addAll(orderYginfo);
                searchAdapter.notifyDataSetChanged();

            }

        }

    }
    private void getTeacherData(String s) {

        Gson gson = new Gson();

        DataDemanJj dataDemanJj = gson.fromJson(s, DataDemanJj.class);
        if (dataDemanJj.getData().getSuccess().equals("1")) {

            List<OrderJjinfoBean> orderJjinfo = dataDemanJj.getData().getOrderJjinfo();

            if (orderJjinfo.size() == 0) {

//                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("没有相关订单")
//                        .show();
                ToastUtil.show(mContext,"没有相关订单");

            }else {

                mTList.addAll(orderJjinfo);
                searchAdapter.notifyDataSetChanged();

            }
        }

    }

}
