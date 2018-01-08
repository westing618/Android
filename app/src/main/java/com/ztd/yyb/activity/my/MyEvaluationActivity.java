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
import com.ztd.yyb.adapter.MyEvaluAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanEvalu.DataMyEvalu;
import com.ztd.yyb.bean.beanEvalu.EvaluatearrayBean;
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
 * 我的评价
 * Created by  on 2017/3/22.
 */

public class MyEvaluationActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_owner)
    TextView tv_owner;
    @BindView(R.id.tv_parents)
    TextView tv_parents;

    @BindView(R.id.myeval_recycler_view)
    RecyclerView mRecyclerView;

    private MyEvaluAdapter mMyAdapter;
    private List<EvaluatearrayBean> mList=new ArrayList<>();
    private Map<String, String> mMyEvaluaMap = new HashMap<>();

    private  String  type="1";

    private int mPageNum = 1;//页码
    private int mPageSize = 10;//一次获取几条数据
    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("我的评价");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new MyEvaluAdapter(this,mList);
        mRecyclerView.setAdapter(mMyAdapter);
        getData();
    }

    private void getData() {//type：用工类型 1 师傅 2家教  objecttype:1我收到的 2.我发布的    ygbmid:用户ID   pageNumber:页数 pageSize：大小
        mList.clear();

        mMyEvaluaMap.clear();
        String  userid = (String) SPUtil.get(USERID, "");
        mMyEvaluaMap.put("ygbmid",""+userid);
        mMyEvaluaMap.put("type",""+type);
        mMyEvaluaMap.put("objecttype",""+"2");
        mMyEvaluaMap.put("pageNumber", String.valueOf(mPageNum));
        mMyEvaluaMap.put("pageSize", String.valueOf(mPageSize));
//        Log.e("type","type"+type);
        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.RANKHISTORY_URL, null, mMyEvaluaMap)
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
                            DataMyEvalu dataMyEvalu = gson.fromJson(s, DataMyEvalu.class);
                            if(dataMyEvalu.getData().getSuccess().equals("1")){
                                List<EvaluatearrayBean> evaluatearray = dataMyEvalu.getData().getEvaluatearray();
                                mList.addAll(evaluatearray);
                                mMyAdapter.notifyDataSetChanged();
                                if(evaluatearray.size()==0){
                                    ToastUtil.show(MyEvaluationActivity.this,"没有数据");
                                }

                            }
                        }



                    }
                });

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_myevalua;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @OnClick({R.id.lin_back,R.id.tv_owner,R.id.tv_parents})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_owner:
                type="1";
                tv_owner.setTextColor(getResources().getColor(R.color.color_red));
                tv_parents.setTextColor(getResources().getColor(R.color.color_66));
                getData();
                break;
            case R.id.tv_parents:
                type="2";
//                tv_allorders.setTextColor(getResources().getColor(R.color.color_66));
                tv_owner.setTextColor(getResources().getColor(R.color.color_66));
                tv_parents.setTextColor(getResources().getColor(R.color.color_red));
                getData();
                break;

        }
    }
}
