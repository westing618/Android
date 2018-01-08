package com.ztd.yyb.activity.my;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.UpData;
import com.ztd.yyb.bean.beanMyOrderBean.DataMyOrder;
import com.ztd.yyb.bean.beanMyOrderBean.OrderJjinfoBean;
import com.ztd.yyb.bean.beanOrder.OrderBean;
import com.ztd.yyb.bean.beanOrder.OrderYginfoBean;
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
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;
import static rx.Observable.timer;

/**
 * 我的订单 2
 * Created by  on 2017/3/13. item_order_footer
 */

public class MyOrderTwoActivity extends BaseActivity implements
        BGARefreshLayout.BGARefreshLayoutDelegate, MyOrdAdapter.OnItemClickListener {


    public static String ORDERTYPETWO = "ordertype";// 我的订单类型  1 师傅  2 家教
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.recycler_myorder)
    RecyclerView recycler_myorder;
    @BindView(R.id.tv_allorders)
    TextView tv_allorders;
    @BindView(R.id.tv_ongoing)
    TextView tv_ongoing;
    @BindView(R.id.tv_completed)
    TextView tv_completed;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.tv_yname)
    TextView tv_yname;

    List<OrderYginfoBean> LeaveYgdata = new ArrayList<>();
    List<OrderJjinfoBean> LeavedJjata = new ArrayList<>();

    MyOrdAdapter myorderAdapter;

    String userid;

    private BGARefreshLayout mRefreshLayout;

    private String type = "1"; //1 师傅 2家教
    private String ygbotype = ""; //0 进行中 1 已完成0 进行中 1 已完成 空全部
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据

    private Map<String, String> mMyorderMap = new HashMap<>();

    private Map<String, String> mcancelorderMap = new HashMap<>();//取消
    private Map<String, String> mstartorderMap = new HashMap<>();//开始
    private Map<String, String> mfinishorderMap = new HashMap<>();//完成

    private boolean code = true;

    private Dialog mSelectHeadteacherDialog; //对话框

    @Override
    protected void initViewsAndEvents() {

        SPUtil.put(ORDERTYPETWO, "1");//默认先展示师傅订单

        userid = (String) SPUtil.get(USERID, "");

        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_myordername);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(this, true));

        myorderAdapter = new MyOrdAdapter(this, LeaveYgdata, LeavedJjata, this);

        myorderAdapter.setOnItemClickListener(this);

        recycler_myorder.setHasFixedSize(true);
        recycler_myorder.setLayoutManager(new LinearLayoutManager(this));
        recycler_myorder.setAdapter(myorderAdapter);

//        inSpinner();

        initSelectHeadteacherDialog();

        initData();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_myorder2;
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

                        initData();

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

                        initData();

                        mRefreshLayout.endLoadingMore();

                        return null;
                    }
                }).subscribe();

        return true;
    }


    private void initData() {//0 进行中 1 已完成0 进行中 1 已完成


        mMyorderMap.clear();
        mMyorderMap.put("userid", userid);
        mMyorderMap.put("type", type);//1 师傅 2家教
        mMyorderMap.put("ygbotype", ygbotype);// 订单状态：0 进行中 1 已完成 2 已取消       3：施工进行中 空字符串：全部

        mMyorderMap.put("pageNumber", String.valueOf(mPageNum));
        mMyorderMap.put("pageSize", String.valueOf(mPageSize));

//        Log.e("订单状态", "==" + ygbotype);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.MY_ORDER_URL, null, mMyorderMap)
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

//                        Log.e("onNextOrder", "==" + s);

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {


                            Gson gson = new Gson();

                            if (type.equals("1")) {

                                OrderBean orderBean = gson.fromJson(s, OrderBean.class);

                                if (orderBean.getData().getSuccess().equals("1")) {

                                    List<OrderYginfoBean> orderYginfo = orderBean.getData().getOrderYginfo();

                                    if (orderYginfo.size() == 0) {

                                        myorderAdapter.notifyDataSetChanged();

                                        ToastUtil.show(mContext, "没有相关数据");//{"result":"true","data":{"orderYginfo":[],"success":"1"},"msg":"获取成功"}
                                        //{"result":"true","data":"","msg":"获取成功"}
                                    } else {

                                        if (mPageNum == 1) {
                                            LeaveYgdata.clear();
                                        }
                                        LeaveYgdata.addAll(orderYginfo);
                                        myorderAdapter.notifyDataSetChanged();
                                    }
                                }


                            } else if (type.equals("2")) {


                                DataMyOrder dataMyOrder = gson.fromJson(s, DataMyOrder.class);

                                if (dataMyOrder.getData().getSuccess().equals("1")) {

                                    List<OrderJjinfoBean> orderJjinfo = dataMyOrder.getData().getOrderJjinfo();

                                    if (orderJjinfo.size() == 0) {

                                        myorderAdapter.notifyDataSetChanged();
                                        ToastUtil.show(mContext, "没有相关数据");

                                    } else {

                                        if (mPageNum == 1) {
                                            LeavedJjata.clear();
                                        }
                                        LeavedJjata.addAll(orderJjinfo);
                                        myorderAdapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        }

                    }
                });

    }


    @OnClick({R.id.lin_back, R.id.tv_allorders, R.id.tv_ongoing, R.id.tv_completed, R.id.tv_cancel, R.id.ll_yname})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.ll_yname:
                mSelectHeadteacherDialog.show();
                break;

            case R.id.tv_allorders:
                ygbotype = "";
                mPageNum = 1;
//                mRefreshLayout.beginRefreshing();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_reb));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_completed.setTextColor(getResources().getColor(R.color.color_333333));
                tv_cancel.setTextColor(getResources().getColor(R.color.color_333333));

                LeaveYgdata.clear();
                LeavedJjata.clear();
                initData();

                break;
            case R.id.tv_ongoing:

                ygbotype = "0";
                mPageNum = 1;
//                mRefreshLayout.beginRefreshing();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_reb));
                tv_completed.setTextColor(getResources().getColor(R.color.color_333333));
                tv_cancel.setTextColor(getResources().getColor(R.color.color_333333));

                LeaveYgdata.clear();
                LeavedJjata.clear();
                initData();
                break;
            case R.id.tv_completed:

                ygbotype = "1";
                mPageNum = 1;
//                mRefreshLayout.beginRefreshing();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_completed.setTextColor(getResources().getColor(R.color.color_reb));
                tv_cancel.setTextColor(getResources().getColor(R.color.color_333333));

                LeaveYgdata.clear();
                LeavedJjata.clear();
                initData();

                break;

            case R.id.tv_cancel:

                ygbotype = "2";
                mPageNum = 1;
//                mRefreshLayout.beginRefreshing();

                tv_allorders.setTextColor(getResources().getColor(R.color.color_333333));
                tv_ongoing.setTextColor(getResources().getColor(R.color.color_333333));
                tv_completed.setTextColor(getResources().getColor(R.color.color_333333));
                tv_cancel.setTextColor(getResources().getColor(R.color.color_reb));

                LeaveYgdata.clear();
                LeavedJjata.clear();

                initData();
                break;

        }
    }

    @Override
    public void onItemClick(View view, int potion) {


        switch (view.getId()) {

            case R.id.myorder_one://控制 取消 按钮


                if (type.equals("1")) {// LeaveYgdata.clear();
                    makeYg(potion);
                } else {//LeavedJjata.clear();
                    makeJj(potion);
                }


                break;
            case R.id.myorder_two://控制 查看详情按钮

                if (type.equals("1")) {

                    Intent intent = new Intent(mContext, MyOrderDetailsActivity.class);
                    intent.putExtra("MM", "1");
                    startActivity(intent);

                    OrderYginfoBean orderYginfoBean = LeaveYgdata.get(potion);
                    SPUtil.put("putorderid", orderYginfoBean.getYgboid());

                } else {

                    Intent intent2 = new Intent(mContext, MyOrderDetailsActivity.class);
                    intent2.putExtra("MM", "2");
                    startActivity(intent2);

                    OrderJjinfoBean orderJjinfoBean = LeavedJjata.get(potion);
                    SPUtil.put("putorderid", orderJjinfoBean.getYgboid());


                }


                break;

            case R.id.myorder_three://控制  出工  施工 完成   //上课 结束 课程

                if (type.equals("1")) {  // LeaveYgdata.clear();

                    startorderYg(potion);

                } else {//LeavedJjata.clear();

                    startorderJj(potion);
                }

                break;
        }

    }

    private void startorderYg(int potion) {//控制  出工  施工 完成


        String ygbotype = LeaveYgdata.get(potion).getYgbotype();

//      String ygboastate = LeaveYgdata.get(potion).getYgboastate();

        final String ygboid = LeaveYgdata.get(potion).getYgboid();

        if (ygbotype.equals("0")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定出工?")
                    .setConfirmText("确定!")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startorder(ygboid);
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        } else if (ygbotype.equals("3")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("施工完成?")
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

        }

    }

    private void startorderJj(int potion) {//上课 结束 课程

//      String ygboastate = LeavedJjata.get(potion).getYgboastate();

        String ygbotype = LeavedJjata.get(potion).getYgbotype();

        final String ygboid = LeavedJjata.get(potion).getYgboid();


        if (ygbotype.equals("0")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定开始上课?")
                    .setConfirmText("确定!")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            startorder(ygboid);
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();

        } else if (ygbotype.equals("3")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("课程结束?")
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
        }

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

//                                new SweetAlertDialog(mContext)
//                                        .setTitleText(upData.getMsg())
//                                        .show();
                                ToastUtil.show(mContext, "" + upData.getMsg());

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
        mfinishorderMap.put("userid", userid);
        mfinishorderMap.put("orderid", ygboid);
        mfinishorderMap.put("type", type);

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
                                ToastUtil.show(mContext, "" + upData.getMsg());

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

    private void cancel(String ygboid, final int potion) {

        mcancelorderMap.clear();
        mcancelorderMap.put("userid", userid);
        mcancelorderMap.put("orderid", ygboid);
        mcancelorderMap.put("type", type);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.CANCEL_ORDER_URL, null, mcancelorderMap)
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

                                ToastUtil.show(mContext,upData.getMsg());
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

    private void makeJj(final int potion) {//控制 取消 按钮

//      String ygbdtype = LeaveYgdata.get(potion).getYgbdtype();
//        String ygboastate = LeavedJjata.get(potion).getYgboastate();

        String ygbotype = LeavedJjata.get(potion).getYgbotype();
        final String ygboid = LeavedJjata.get(potion).getYgboid();

        if (ygbotype.equals("0")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定取消订单?")
                    .setContentText("取消超过3次，账号异常")
                    .setConfirmText("确定!")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();

                            cancel(ygboid, potion);
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

    private void makeYg(final int potion) {  //用工  第一个按钮控制  取消

        String ygbotype = LeaveYgdata.get(potion).getYgbotype();
        final String ygboid = LeaveYgdata.get(potion).getYgboid();

//        String ygboastate = LeaveYgdata.get(potion).getYgboastate();

        if (ygbotype.equals("0")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定取消订单?")
                    .setContentText("取消超过3次，账号异常")
                    .setConfirmText("确定!")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            cancel(ygboid, potion);
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


    private void initSelectHeadteacherDialog() {

        mSelectHeadteacherDialog = new Dialog(this, R.style.dialog_bottom_full);
        mSelectHeadteacherDialog.setCanceledOnTouchOutside(true);
        mSelectHeadteacherDialog.setCancelable(true);

        Window window = mSelectHeadteacherDialog.getWindow();
        window.setGravity(Gravity.TOP);

        View view = View.inflate(this, R.layout.lay_myo, null);

        view.findViewById(R.id.tv_yname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSelectHeadteacherDialog();
                tv_yname.setText("师傅");
                type = "1";
                SPUtil.put(ORDERTYPETWO, "1");
                mRefreshLayout.beginRefreshing();

            }
        });
        view.findViewById(R.id.tv_xname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissSelectHeadteacherDialog();
                tv_yname.setText("家教");
                type = "2";
                SPUtil.put(ORDERTYPETWO, "2");
                mRefreshLayout.beginRefreshing();
            }
        });


        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

    }

    /**
     * 隐藏对话框
     */
    private void dismissSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.dismiss();
        }
    }

}
