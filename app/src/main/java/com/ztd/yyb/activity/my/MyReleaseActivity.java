package com.ztd.yyb.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.order.PaymentActivity;
import com.ztd.yyb.adapter.MyReAdapter;
import com.ztd.yyb.adapter.ParentsAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.UpData;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.DmlistBean;
import com.ztd.yyb.bean.beanMyReleJj.DataMyReleJj;
import com.ztd.yyb.bean.beanMyReleJj.OrderJjinfoBean;
import com.ztd.yyb.bean.beanMyReleYg.DataMyReleYg;
import com.ztd.yyb.bean.beanMyReleYg.OrderYginfoBean;
import com.ztd.yyb.bean.beanSuess.ChangBean;
import com.ztd.yyb.evenbean.EvalEvent;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.R.id.tv_myreone;
import static com.ztd.yyb.R.id.tv_myrethree;
import static com.ztd.yyb.R.id.tv_myretwo;
import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static rx.Observable.timer;

/**
 * 我的发布
 * Created by  on 2017/3/13.
 */

public class MyReleaseActivity extends BaseActivity
        implements BGARefreshLayout.BGARefreshLayoutDelegate, MyReAdapter.OnItemClickListener {

    public static String REORDERTYPE = "REORDERTYPE";// 我的发布-用工类型 1 业主  2家教
    @BindView(R.id.myrelease_recycler_view)
    RecyclerView morder_recycler_view;
    @BindView(R.id.tv_reallorders)
    TextView tv_reallorders;
    @BindView(R.id.tv_reongoing)
    TextView tv_reongoing;
    @BindView(R.id.tv_recance)
    TextView tv_recance;
    @BindView(R.id.tv_coming)
    TextView tv_coming;
    @BindView(R.id.tv_recompleted)
    TextView tv_recompleted;
    @BindView(R.id.tv_recancel)
    TextView tv_recancel;

    @BindView(R.id.tv_yname)
    TextView tv_yname;

    MyReAdapter myorderAdapter;
    String userid;
    String type = "1";                // 用工类型 1 师傅 2家教
    String ygbotype = "";             // 订单0 进行中 1 已完成 2 已取消3：施工进行中 4:待业主确认 9：所有进行中 3：施工进行中 空字符串：全部
    String ygbdstate = "";            // 状态：0 未接  1已接  2取消
    String ygbdauditing = "";         // 审核状态 0  未审核 1 审核通过 2 审核未通过
    AppConfigBean appConfigBean;
    List<DatadictionaryBean> datadictionary;
    String orderid;
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据
    private Map<String, String> mMyReleaMap = new HashMap<>();
    // 说明 ygbdstate 用于未接单  ygbdauditing用于审核中
    private BGARefreshLayout mRefreshLayout;
    private List<OrderJjinfoBean> mJjinfoList = new ArrayList<>();
    private List<OrderYginfoBean> mYginfoList = new ArrayList<>();
    private Map<String, String> mcancelorderMap = new HashMap<>();//取消
    private Map<String, String> mstartorderMap = new HashMap<>();//开始
    private Map<String, String> mfinishorderMap = new HashMap<>();//完成
    private Map<String, String> mpayhorderMap = new HashMap<>();//完成

    private Dialog mSelectHeadteacherDialog; //对话框  加价
    private Dialog Dialog; //对话框  加价

    private ParentsAdapter mParentsAdapter;
    private List<ChangBean> mInfoList = new ArrayList<>();

    @Override
    protected void initViewsAndEvents() {
        initUI();

        initSelectHeadteacherDialog();
    }

    private void initUI() {

        EventBus.getDefault().register(this);

        SPUtil.put(REORDERTYPE, "1");//默认先展示 业主 订单

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_modulename_refresh);

        mRefreshLayout.setDelegate(this);

        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));

        myorderAdapter = new MyReAdapter(this, mYginfoList, mJjinfoList, this);

        myorderAdapter.setOnItemClickListener(this);

        morder_recycler_view.setHasFixedSize(true);

        morder_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        morder_recycler_view.setAdapter(myorderAdapter);

//        inSpinner();

        getData();
        injiaData();
        initDialog();

    }

    private void injiaData() {

        String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");

        Gson gson = new Gson();

        if (!TextUtils.isEmpty(myappconfig)) {

            appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
            datadictionary = appConfigBean.getData().getDatadictionary();

            mInfoList.clear();

            for (int i = 0; i < datadictionary.size(); i++) {
                if ("jjje".equals(datadictionary.get(i).getZldm())) {//加价金额
                    List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                    for (int ii = 0; ii < dmlist.size(); ii++) {
                        ChangBean chanbean = new ChangBean();
                        String classname = dmlist.get(ii).getDmmc();
                        String classcd = dmlist.get(ii).getDm();
                        chanbean.setName(classname);
                        chanbean.setId(classcd);
                        mInfoList.add(chanbean);
                    }
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getData() {

        userid = (String) SPUtil.get(USERID, "");
//        Log.e("222","111="+userid);

        mMyReleaMap.clear();

        mMyReleaMap.put("userid", userid);
        mMyReleaMap.put("type", type);
        mMyReleaMap.put("ygbotype", ygbotype);
        mMyReleaMap.put("ygbdstate", ygbdstate);
        mMyReleaMap.put("ygbdauditing", ygbdauditing);

        mMyReleaMap.put("pageNumber", String.valueOf(mPageNum));
        mMyReleaMap.put("pageSize", String.valueOf(mPageSize));

//        Log.e("mPageNum","mPageNum="+mPageNum);
//        Log.e("type","type="+type);
//        Log.e("ygbotype","ygbotype="+ygbotype);
//        Log.e("ygbdstate","ygbdstate="+ygbdstate);
//        Log.e("ygbdauditing","ygbdauditing="+ygbdauditing);

//        showLoadingDialog();
//        progressDialog.show();

        OkHttp3Utils.getInstance().doPost(Constants.GETPUBLISHLIST_URL, null, mMyReleaMap)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
//                        dismissLoadingDialog();
//                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        dismissLoadingDialog();
//                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(String s) {
//                        dismissLoadingDialog();
//                        progressDialog.dismiss();

                        if (!TextUtils.isEmpty(s)) {
                            if (type.equals("1")) {//用工
                                initYgData(s);
                            } else {//家教
                                initJjData(s);
                            }

                        }
                    }
                });

    }

    private void initJjData(String s) {

        Gson gson = new Gson();

        try {
            DataMyReleJj dataMyReleJj = gson.fromJson(s, DataMyReleJj.class);
            if (dataMyReleJj.getData().getSuccess().equals("1")) {
                List<OrderJjinfoBean> orderJjinfo = dataMyReleJj.getData().getOrderJjinfo();
                if (orderJjinfo.size() == 0) {
                    ToastUtil.show(this, "没有相关订单");
                } else {
                    if (mPageNum == 1) {
                        mJjinfoList.clear();
                    }
                    mJjinfoList.addAll(orderJjinfo);
                    myorderAdapter.notifyDataSetChanged();
                }
            } else {
                ToastUtil.show(this, "查询失败");
            }
        } catch (Exception e) {
            ToastUtil.show(this, "没有相关订单");
        }


    }

    private void initYgData(String s) {//{"result":"true","data":"","msg":"获取成功"}

        Gson gson = new Gson();
        DataMyReleYg dataMyReleYg = gson.fromJson(s, DataMyReleYg.class);

        if (dataMyReleYg.getResult().equals("true")) {
            com.ztd.yyb.bean.beanMyReleYg.DataBean data = dataMyReleYg.getData();
            if (data == null || data.equals("")) {
                ToastUtil.show(this, "没有相关订单");
            }
        }

        if (dataMyReleYg.getData().getSuccess().equals("1")) {
            List<OrderYginfoBean> orderYginfo = dataMyReleYg.getData().getOrderYginfo();
            if (orderYginfo.size() == 0) {
                ToastUtil.show(this, "没有相关订单");
            } else {
                if (mPageNum == 1) {//上拉刷新时清空数据
                    mYginfoList.clear();
                }
                mYginfoList.addAll(orderYginfo);
                myorderAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtil.show(this, "查询失败");
        }


    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_myrelease2;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(EvalEvent message) {//EvalEvent评价完刷新

        if (message.getMsg().equals("EvalEvent")) {
            mRefreshLayout.beginRefreshing();
        }

    }

    private void initDialog() {

        Dialog = new Dialog(this, R.style.dialog_bottom_full);
        Dialog.setCanceledOnTouchOutside(true);
        Dialog.setCancelable(true);

        Window window = Dialog.getWindow();
        window.setGravity(Gravity.TOP);

        View view = View.inflate(this, R.layout.lay_myore, null);

        view.findViewById(R.id.tv_yname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                tv_yname.setText("业主");
                type = "1";
                SPUtil.put(REORDERTYPE, "1");// 1业主 订单
                mRefreshLayout.beginRefreshing();

            }
        });
        view.findViewById(R.id.tv_xname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
                tv_yname.setText("家长");
                type = "2";
                SPUtil.put(REORDERTYPE, "2");// 2家教 订单
                mRefreshLayout.beginRefreshing();
            }
        });


        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

    }

    /**
     * 显示对话框
     */
    private void showSelectHeadteacherDialog() {

        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.show();
            myorderAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 隐藏对话框
     */
    private void dismissDialog() {
        if (Dialog != null) {
            Dialog.dismiss();
        }
    }

    /**
     * 初始化对话框
     */
    private void initSelectHeadteacherDialog() {

        mSelectHeadteacherDialog = new Dialog(this, R.style.dialog_bottom_full);
        mSelectHeadteacherDialog.setCanceledOnTouchOutside(true);
        mSelectHeadteacherDialog.setCancelable(true);
        Window window = mSelectHeadteacherDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_animation);
        View view = View.inflate(this, R.layout.attendance_lay_select_class, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.select_class_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//使用RecyclerView 默认动画效果
        mParentsAdapter = new ParentsAdapter(MyReleaseActivity.this, mInfoList, new ParentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismissSelectHeadteacherDialog();

                String name = mInfoList.get(position).getName();

                SPUtil.put("JIAJIA", "0");

                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("orderid", "" + orderid);
                intent.putExtra("ygbdgamount", "" + name);
                intent.putExtra("ygbdaddprice", "" + name);
                startActivity(intent);

//                finish();

            }
        });

        recyclerView.setAdapter(mParentsAdapter);
        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }

    /**
     * 隐藏对话框
     */
    private void dismissSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.dismiss();
        }
    }

    @OnClick({R.id.lin_back, R.id.tv_reallorders,
            R.id.tv_reongoing, R.id.tv_recance, R.id.tv_recompleted,
            R.id.tv_recancel, R.id.tv_coming, R.id.ll_yname})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_yname:
                Dialog.show();
                break;
            case R.id.tv_coming:

                mRefreshLayout.beginRefreshing();

                ygbotype = "9";
                ygbdstate = "";
                ygbdauditing = "";
                tv_coming.setTextColor(getResources().getColor(R.color.color_reb));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recancel.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recance.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

                break;
            case R.id.tv_reallorders:

                mRefreshLayout.beginRefreshing();

                ygbotype = "";
                ygbdstate = "";
                ygbdauditing = "";
                tv_coming.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_reb));
                tv_recancel.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recance.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

                break;
            case R.id.tv_reongoing://未接单

                mRefreshLayout.beginRefreshing();

                ygbotype = "";
                ygbdstate = "0";
                ygbdauditing = "";
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_reb));
                tv_recancel.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recance.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_coming.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

                break;
            case R.id.tv_recance://审核中

                mRefreshLayout.beginRefreshing();


                ygbotype = "";
                ygbdstate = "";
                ygbdauditing = "0";
                tv_recance.setTextColor(getResources().getColor(R.color.color_reb));
                tv_recancel.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_coming.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

                break;
            case R.id.tv_recompleted://已完成


                mRefreshLayout.beginRefreshing();


                ygbotype = "1";
                ygbdstate = "";
                ygbdauditing = "";
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_reb));
                tv_recancel.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recance.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_coming.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

                break;
            case R.id.tv_recancel://已取消

                mRefreshLayout.beginRefreshing();


                ygbotype = "2";
                ygbdstate = "";
                ygbdauditing = "";
                tv_recancel.setTextColor(getResources().getColor(R.color.color_reb));
                tv_recompleted.setTextColor(getResources().getColor(R.color.color_333333));
                tv_recance.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_reallorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_coming.setTextColor(getResources().getColor(R.color.color_333333));

                mYginfoList.clear();
                mJjinfoList.clear();

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


    @Override
    public void onItemClick(View view, int position) {

        switch (view.getId()) {

            case tv_myreone:

                if (type.equals("1")) {  // mYginfoList

                    makeYg(position);

                } else {                //  mJjinfoList

                    makeJj(position);

                }

                break;

            case tv_myretwo:


                if (type.equals("1")) {


                    String ygboid = mYginfoList.get(position).getYgboid();
                    SPUtil.put("putorderid", "" + ygboid);

                    Intent intent = new Intent(mContext, MyOrderDetailsActivity.class);
                    intent.putExtra("MM", "5");
                    startActivity(intent);

                } else {


                    String ygboid = mJjinfoList.get(position).getYgboid();
                    SPUtil.put("putorderid", "" + ygboid);

                    Intent intent2 = new Intent(mContext, MyOrderDetailsActivity.class);
                    intent2.putExtra("MM", "6");
                    startActivity(intent2);

                }

//                Log.e("tv_myretwo","tv_myretwo="+type);


                break;

            case tv_myrethree:


                if (type.equals("1")) {  // mYginfoList
                    operationYg(position);
                } else {                //  mJjinfoList
                    operationJj(position);
                }

                break;


        }

    }

    //TODO 第三个按钮
    private void operationYg(int position) {

        String ygbdauditing = mYginfoList.get(position).getYgbdauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
        String ygbdstate = mYginfoList.get(position).getYgbdstate();       //  0 未接 1已接 2取消-----------------------------2
        String ygbotype = mYginfoList.get(position).getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
        String ygboastate = mYginfoList.get(position).getYgboastate(); //0 未确认 1 已确认

        final String ygboid = mYginfoList.get(position).getYgboid();

        if (ygbotype.equals("2")) {// 查看详情

        } else {

            if (ygbdauditing.equals("0")) {// 取消订单 查看详情


            } else if (ygbdauditing.equals("1")) {


                if (ygbdstate.equals("0")) {//取消订单  查看详情  加价

                    showSelectHeadteacherDialog();

                    orderid = mYginfoList.get(position).getYgboid();

                } else {

                    if (ygbotype.equals("0") || ygbotype.equals("3")) {//查看详情  施工完成

                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("确定施工完成")
//                    .setContentText("Won't be able to recover this file!")
                                .setConfirmText("确定!")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        finishorder(ygboid);
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();


                    } else {

                        if (ygboastate.equals("0")) {//查看详情  付款

                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("同意支付")
//                    .setContentText("Won't be able to recover this file!")
                                    .setConfirmText("确定!")
                                    .setCancelText("取消")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();

                                            payhorder(ygboid);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();

                        } else {

                            if (ygbotype.equals("1")) {// 去评价 查看详情

                                Intent inten = new Intent(this, EvaluationActivity.class);
                                inten.putExtra("ygboid", ygboid);
                                startActivity(inten);

                            }
                        }
                    }
                }
            } else {//取消订单  查看详情

            }
        }

    }

    //TODO 第三个按钮
    private void operationJj(int position) {

        String ygbdauditing = mJjinfoList.get(position).getYgbdgauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
        String ygbdstate = mJjinfoList.get(position).getYgbdgstate();       //  0 未接 1已接 2取消-----------------------------2
        String ygbotype = mJjinfoList.get(position).getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
        String ygboastate = mJjinfoList.get(position).getYgboastate(); //0 未确认 1 已确认
        final String ygboid = mJjinfoList.get(position).getYgboid();

        if (ygbotype.equals("2")) {//查看详情
        } else {
            if (ygbdauditing.equals("0")) {//  取消订单 查看详情
            } else if (ygbdauditing.equals("1")) {
                if (ygbdstate.equals("0")) {//  取消订单  查看详情 加价
                } else {
                    if (ygbotype.equals("0") || ygbotype.equals("3")) {// 查看详情 结束课程
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("确定结束课程")
//                    .setContentText("Won't be able to recover this file!")
                                .setConfirmText("确定!")
                                .setCancelText("取消")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();

                                        finishorder(ygboid);
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();

                    } else {
                        if (ygboastate.equals("0")) {//查看详情  付款
                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("同意支付")
//                    .setContentText("Won't be able to recover this file!")
                                    .setConfirmText("确定!")
                                    .setCancelText("取消")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            payhorder(ygboid);
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();

                        } else {
                            if (ygbotype.equals("1")) {//查看详情  去评价

                                Intent inten = new Intent(this, EvaluationActivity.class);
                                inten.putExtra("ygboid", ygboid);
                                startActivity(inten);
                            }
                        }
                    }
                }
            } else {//取消订单  查看详情

            }
        }

    }

    //TODO 第一个按钮
    private void makeYg(int position) {

        String ygbdauditing = mYginfoList.get(position).getYgbdauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
        String ygbdstate = mYginfoList.get(position).getYgbdstate();       //  0 未接 1已接 2取消-----------------------------2
        String ygbotype = mYginfoList.get(position).getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
        String ygboastate = mYginfoList.get(position).getYgboastate(); //0 未确认 1 已确认

        final String ygboid = mYginfoList.get(position).getYgbodid();

        String ygbeid = mYginfoList.get(position).getYgbeid();

        if (ygbotype.equals("2")) {//查看详情
        } else {
            if (ygbdauditing.equals("0")) {//取消订单 查看详情
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定取消订单?")
                        .setContentText("第一次取消免费扣费，第二次开始需要扣20%的违约金!")
                        .setConfirmText("确定!")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                cancel(ygboid);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            } else if (ygbdauditing.equals("1")) {

                if (ygbdstate.equals("0")) {// 取消订单 查看详情  加价

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定取消订单?")
                            .setContentText("第一次取消免费扣费，第二次开始需要扣20%的违约金!")
                            .setConfirmText("确定!")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    cancel(ygboid);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();

                }

            }

        }


        if (ygbotype.equals("2")) {

        } else {
            if (ygbdauditing.equals("0")) {
//                tv_myreone.setText("取消订单");
            } else if (ygbdauditing.equals("1")) {

                if (ygbdstate.equals("0")) {
//                    tv_myreone.setText("取消订单");
                } else {
                    if (ygbotype.equals("0")) {
                    } else {
                        if (ygboastate.equals("0")) {
                        } else {
                            if (ygbotype.equals("1")) {

                                if (ygbeid.equals("")) {

//                                    tv_myreone.setText("开票");
//                                    tv_myretwo.setText("查看详情");
//                                    tv_myrethree.setText("去评价");

                                    Intent inen = new Intent(this, MakeInvoiceActivity.class);
                                    startActivity(inen);


                                } else {

                                }

                            }
                        }
                    }
                }
            } else {
//                        [self.stateLable setText:@"审核未通过"];
//                tv_myreone.setText("取消订单");
//                tv_myretwo.setText("查看详情");
//                tv_myrethree.setVisibility(View.GONE);

            }
        }


    }

    //TODO 第一个按钮
    private void makeJj(int position) {

        //getYgb dg auditing  ygb dg auditing     getYgbdgstate
        String ygbdauditing = mJjinfoList.get(position).getYgbdgauditing(); //0 未审核 1 审核通过 2 审核未通过---------------------1
        String ygbdstate = mJjinfoList.get(position).getYgbdgstate();       //  0 未接 1已接 2取消-----------------------------2
        String ygbotype = mJjinfoList.get(position).getYgbotype();//0 进行中 1 已完成 2 已取消  3：施工进行中 4:待业主确认 9：所有进行中---------------3
        String ygboastate = mJjinfoList.get(position).getYgboastate(); //0 未确认 1 已确认

        final String ygboid = mJjinfoList.get(position).getYgbodid();

        if (ygbotype.equals("2")) {

        } else {

            if (ygbdauditing.equals("0")) {//取消订单

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定取消订单?")
                        .setContentText("第一次取消免费扣费，第二次开始需要扣20%的违约金!")
                        .setConfirmText("确定!")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                cancel(ygboid);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            } else if (ygbdauditing.equals("1")) {

                if (ygbdstate.equals("0")) {//ygbdstate

                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定取消订单?")
                            .setContentText("第一次取消免费扣费，第二次开始需要扣20%的违约金!")
                            .setConfirmText("确定!")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                    cancel(ygboid);
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();

//                    tv_myreone.setText("取消订单");
//                    tv_myretwo.setText("查看详情");
//                    tv_myrethree.setText("加价");

                }

            }

        }


    }

    private void cancel(String ygboid) {

        mcancelorderMap.clear();

        mcancelorderMap.put("userid", userid);
        mcancelorderMap.put("ygbdid", ygboid);
        mcancelorderMap.put("type", type);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.CANCEL_ORDERDEMAND_URL, null, mcancelorderMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"取消成功"}
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UpData upData = gson.fromJson(s, UpData.class);

                            if (upData.getData().getSuccess().equals("1")) {

//                                new SweetAlertDialog(mContext)
//                                        .setTitleText(upData.getMsg())
//                                        .show();
                                ToastUtil.show(mContext, upData.getMsg());
                                mRefreshLayout.beginRefreshing();

                            } else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();

                            }
                        }

                    }
                });

    }

    private void finishorder(String ygboid) {

        mfinishorderMap.clear();
        mfinishorderMap.put("userid", "" + userid);
        mfinishorderMap.put("orderid", "" + ygboid);
        mfinishorderMap.put("type", "" + type);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.FINISHORDER_URL, null, mfinishorderMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"取消成功"}
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UpData upData = gson.fromJson(s, UpData.class);

                            if (upData.getData().getSuccess().equals("1")) {

//                                new SweetAlertDialog(mContext)
//                                        .setTitleText(upData.getMsg())
//                                        .show();
                                ToastUtil.show(mContext, upData.getMsg());
                                mRefreshLayout.beginRefreshing();

                            } else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();

                            }
                        }

                    }
                });

    }

    private void startorder(String ygboid) {

        mstartorderMap.clear();
        mstartorderMap.put("userid", "" + userid);
        mstartorderMap.put("orderid", "" + ygboid);
        mstartorderMap.put("type", "" + type);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.STARTORDER_URL, null, mstartorderMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"取消成功"}
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UpData upData = gson.fromJson(s, UpData.class);

                            if (upData.getData().getSuccess().equals("1")) {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();

                                mRefreshLayout.beginRefreshing();

                            } else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();

                            }
                        }

                    }
                });


    }


    private void payhorder(String ygboid) {

        mpayhorderMap.clear();
        mpayhorderMap.put("userid", "" + userid);
        mpayhorderMap.put("orderid", "" + ygboid);
        mpayhorderMap.put("type", "" + type);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.PAYHORDERORDER_URL, null, mpayhorderMap)
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
                    public void onNext(String s) {//{"result":"true","data":{"success":"1"},"msg":"取消成功"}
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            UpData upData = gson.fromJson(s, UpData.class);

                            if (upData.getData().getSuccess().equals("1")) {

//                                new SweetAlertDialog(mContext)
//                                        .setTitleText(upData.getMsg())
//                                        .show();
                                // TODO  确认付款成功 提示 是否马上去开票
//                                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
//                                        .setTitleText(upData.getMsg())
//                                        .setContentText("同意支付成功")
//                                        .setConfirmText("需要开票")
//                                        .setCancelText("暂不开票")
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sDialog) {
//                                                sDialog.dismissWithAnimation();
//                                                Intent inen = new Intent(mContext, MakeInvoiceActivity.class);
//                                                startActivity(inen);
//                                                finish();
//                                            }
//                                        })
//                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sDialog) {
//                                                sDialog.cancel();
//                                            }
//                                        })
//                                        .show();

                                final Dialog builder = new Dialog(mContext, R.style.dialog);
                                builder.setContentView(R.layout.my_dialog);
//                              TextView title = (TextView) builder.findViewById(R.id.dialog_title);
                                TextView content = (TextView) builder.findViewById(R.id.dialog_content);
                                content.setText("同意支付成功");
                                Button confirm_btn = (Button) builder.findViewById(R.id.dialog_sure);
                                Button cancel_btn = (Button) builder.findViewById(R.id.dialog_cancle);
                                confirm_btn.setText("需要开票");
                                cancel_btn.setText("暂不开票");
                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent inen = new Intent(mContext, MakeInvoiceActivity.class);
                                        startActivity(inen);
                                        finish();
                                    }
                                });
                                cancel_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        builder.dismiss();
                                    }
                                });
                                builder.setCanceledOnTouchOutside(false);// 设置点击Dialog外部任意区域关闭Dialog
                                builder.show();

                                mRefreshLayout.beginRefreshing();

                            } else {

                                new SweetAlertDialog(mContext)
                                        .setTitleText(upData.getMsg())
                                        .show();
                            }
                        }

                    }
                });

    }

}
