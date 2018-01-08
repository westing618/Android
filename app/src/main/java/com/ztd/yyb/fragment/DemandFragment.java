package com.ztd.yyb.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.order.ReleaseDemandActivity;
import com.ztd.yyb.adapter.DemandAdapter;
import com.ztd.yyb.adapter.ExpandableListGridViewAdapter;
import com.ztd.yyb.base.BaseLazyFragment;
import com.ztd.yyb.bean.beanCheck.DataCheck;
import com.ztd.yyb.bean.beanDeman.DataDemanYg;
import com.ztd.yyb.bean.beanDeman.OrderYginfoBean;
import com.ztd.yyb.bean.beanDemanJ.DataDemanJj;
import com.ztd.yyb.bean.beanDemanJ.OrderJjinfoBean;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHome.DatadictionaryBean;
import com.ztd.yyb.bean.beanHome.DmlistBean;
import com.ztd.yyb.bean.beanHome.GzdjlistBean;
import com.ztd.yyb.bean.beanHome.KmlistBean;
import com.ztd.yyb.bean.beanHome.SubjectBean;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.PollingTask;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.HomeActivity.APPCONFIGBEAN;
import static com.ztd.yyb.activity.LoginActivity.USERID;
import static com.ztd.yyb.activity.MainNActivity.mViewpager;
import static com.ztd.yyb.bean.beanCheck.DataCheck.DataCheckChild;


/**
 * 需求
 * Created by  on 2017/3/13.
 */

public class DemandFragment extends BaseLazyFragment implements
        ExpandableListGridViewAdapter.OnGridItemClickListener {

    private static final int MSG_LOAD_DATA = 0x0001;
    @BindView(R.id.demand_recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.refresh)
    TwinklingRefreshLayout mRefreshLayout;

    @BindView(R.id.image_open)
    ImageView image_open;

    @BindView(R.id.tv_open)
    TextView tv_open;

    @BindView(R.id.ll_voice)
    LinearLayout ll_voice;

    String userid;
    String type;//用工类型 1 师傅 2家教
    AppConfigBean appConfigBean;
    List<GzdjlistBean> gzdjlist;
    List<KmlistBean> kmlist;
    List<DatadictionaryBean> datadictionary;
    String moment = "";//年级
    String subject = "0";//科目
    String workers = "0";//人数
    String kind = "";//工种
    String distance = "0";//公里
    String days = "0";//天
    String ygbeducation = "";// 学历

    String flagCoad= "1"; // 1 筛选 2
    SpeechSynthesizer mTts;
    ExpandableListGridViewAdapter myExpandableGridViewadapter;
    List<String> YongGongList = new ArrayList<>();
    List<String> YongTList = new ArrayList<>();
    ArrayList<String> YongNewGoIdList = new ArrayList<>();//判断新旧语音，用工和家教共用
    ArrayList<String> YongOldGoIdList = new ArrayList<>();//判断新旧语音，用工和家教共用
    ArrayList<DataCheckChild> kemuList;
    PollingTask mPushPollingTask;
    ExpandableListView expandableListView;
    private boolean flag = true;
    private DemandAdapter mMyAdapter;
    private ArrayList<DataCheck> mCheckList = new ArrayList<>();
    private ArrayList<ArrayList<DataCheck.DataCheckChild>> mCheckListChild = new ArrayList<>();
    private int mPageNum = 1;//页码
    private int mPageSize = 15;//一次获取几条数据
    private Boolean genre;
    private Map<String, String> mDemanLimitMap = new HashMap<>();
    private Map<String, String> mDemanLiVli = new HashMap<>();
    private Map<String, String> mFindDemanMap = new HashMap<>();
    private List<OrderYginfoBean> mYgList = new ArrayList<>();
    private List<OrderJjinfoBean> mTList = new ArrayList<>();
    private Dialog mSelectHeadteacherDialog; //对话框
    private int numtype;//0  1  2  3
    private Toast mToast;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            ToastUtil.show(mContext, "开始播放");
        }
        @Override
        public void onSpeakPaused() {
//            ToastUtil.show(mContext, "暂停播放");
        }
        @Override
        public void onSpeakResumed() {
//            ToastUtil.show(mContext, "继续播放");
        }
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }
        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                // TODO 播放完成 继续播放下列 ，5s请求一次数据
                startSpeaking();
            } else if (error != null) {
                ToastUtil.show(mContext, "" + error.getPlainDescription(true));
            }
        }
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
    private Thread thread;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String myappconfig = (String) SPUtil.get(APPCONFIGBEAN, "");
                                Gson gson = new Gson();
                                if (!TextUtils.isEmpty(myappconfig)) {
                                    appConfigBean = gson.fromJson(myappconfig, AppConfigBean.class);
                                    gzdjlist = appConfigBean.getData().getGzdjlist();//用工工种
                                    kmlist = appConfigBean.getData().getKmlist();//年级 科目
                                    datadictionary = appConfigBean.getData().getDatadictionary();
                                    initData();//初始化 搜索数据
                                }
                            }
                        });
                        thread.start();
                    }
                    break;
            }
        }
    };

    @Override
    protected void initViewsAndEvents() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this); }
        userid = (String) SPUtil.get(USERID, "");
        genre = (Boolean) SPUtil.get(HomeActivity.TYPE, true);//true 用工  flase 学堂
        if (genre) {//true 用工  flase 学堂
            type = "1";
        } else {
            type = "2";
        }
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mMyAdapter = new DemandAdapter(getActivity(), mTList, mYgList);
        mRecyclerView.setAdapter(mMyAdapter);

        getDemanData();
//        getDemanVoiceData();
        setToast();
        initSelectHeadteacherDialog();
        setVoice();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);//TODO 子线程 解析数据
//        PollingUtils.startPollingService(getActivity(), 5, PollingService.class, PollingService.ACTION);//  每隔  5min  改为  5s  就发出一个通知 刷新数据 AlarmManager 只能设置大于 1分钟 间隔时间
        setTask();
        setRefresh();

        ygbeducation="";

    }

    private void setToast() {
        mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0, 0);
    }

    private void setTask() {

        mPushPollingTask = new PollingTask();
        mPushPollingTask
                .createTask(5)  // 创建一个5秒为心跳的任务
                .connected()
                .setOnTaskListener(new PollingTask.OnTaskListener() {
                                       @Override
                                       public void executeTask(PollingTask pollingTask) {
                                           getPushNewsInfos();
                                       }
                                   }
                );
    }

    private void setRefresh() {

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageNum=1;
                        getDemanData();
//                        getDemanVoiceData();
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPageNum++;
                        getDemanData();
                        refreshLayout.finishLoadmore();

                    }
                }, 2000);
            }
        });

    }

    /**
     * 5s刷新一次获取语音播报的和列表数据的  关键的一步一旦一次任务执行完后，调用pollingTask的notifyTaskFinish方法，
     * 去通知任务观察者去更新再次发起一个任务的执行，否则轮询就无效， 这个好比ListView、RecyclerView的Adatper里的数据刷新机制是一样的
     */
    private void getPushNewsInfos() {
        mPushPollingTask.notifyTaskFinish();
//        getDemanVoiceData();
        getDemanData();
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Subscribe
    public void onEventMainThread(DemanEvet degeEvent) {
        String type = (String) SPUtil.get(MianFragment.TYPE, "");//1 2  4筛选
        String msg = degeEvent.getMsg();// 接受主页的刷新通知
        if ("Deman".equals(msg)) {
            if (ll_voice == null) {
                return;
            }
            if (type.equals("1")) {
                ll_voice.setVisibility(View.GONE);
            } else if (type.equals("2")) {
                ll_voice.setVisibility(View.VISIBLE);
            }
            mMyAdapter.notifyDataSetChanged();
        } else if (msg.equals("paysucc")) {// 接受付款成功后 ，订单刷新 的通知
            getDemanData();
        } else if (msg.equals("4")) {
            String id = degeEvent.getId();
            String name = degeEvent.getName();
            String price = degeEvent.getPrice();

            kind=id;
            mCheckList.set(0,new DataCheck("工种", "",name));
            myExpandableGridViewadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPushPollingTask != null) {
            mPushPollingTask.destroyTask();
        }
//        PollingUtils.stopPollingService(getActivity(), PollingService.class, PollingService.ACTION);
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    private void setVoice() {
        SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID + "=58eb4eda");
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        if (mTts != null) {
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
            mTts.setParameter(SpeechConstant.SPEED, "40");//设置语速
            mTts.setParameter(SpeechConstant.VOLUME, "90");//设置音量，范围0~100
        } else {
            ToastUtil.show(mContext, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
        }
    }

    /***
     * 获取列表数据
     * */
    private void getDemanData() {
        flagCoad ="2";
        mDemanLimitMap.clear();
        mDemanLimitMap.put("userid", userid);//用户id
        mDemanLimitMap.put("type", type);//用工类型 1 师傅 2家教
        mDemanLimitMap.put("pageNumber", String.valueOf(mPageNum));
        mDemanLimitMap.put("pageSize", String.valueOf(mPageSize));
        String latitude = (String) SPUtil.get("latitude", "");//纬度
        String longitude = (String) SPUtil.get("longitude", "");//经度
//        Log.e("latitude","="+latitude);
//        Log.e("latitude","="+longitude);
        mDemanLimitMap.put("lat", latitude);//纬度
        mDemanLimitMap.put("lng", longitude);//经度
        mDemanLimitMap.put("kind", ""+kind);        //工种 筛选条件为全部时传空
        mDemanLimitMap.put("workers", ""+workers);  //人数 如果筛选条件为全部的传0，这些参数为必填
        mDemanLimitMap.put("moment", ""+moment);    //年级 如果筛选条件为全部的传0，这些参数为必填
        mDemanLimitMap.put("subject", ""+subject);  //科目 如果筛选条件为全部的传0，这些参数为必填
        mDemanLimitMap.put("distance", ""+distance);//公里 如果筛选条件为全部的传0，这些参数为必填
        mDemanLimitMap.put("days", ""+days);        //天数 如果筛选条件为全部的传0，这些参数为必填
        mDemanLimitMap.put("ygbeducation", ygbeducation);        // 学历

        String lcityid = (String) SPUtil.get("lcityid", "");
        mDemanLimitMap.put("lcity", lcityid);        // 城市代码

//        Log.e("ygbeducation","="+ygbeducation);
        OkHttp3Utils.getInstance().doPost(Constants.FINDORDER_URL, null, mDemanLimitMap)
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
//                        Log.e("onNext","="+s);

                        if (!TextUtils.isEmpty(s)) {
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

/***
 * 用工解析
 * */
    private void getYongGonData(String s) {
        Gson gson = new Gson();
        DataDemanYg dataDemanYg = gson.fromJson(s, DataDemanYg.class);
        if (dataDemanYg.getData().getSuccess()==1) {
            List<OrderYginfoBean> orderYginfo = dataDemanYg.getData().getOrderYginfo();
            if (orderYginfo.size() == 0) {
                if("1".equals(flagCoad)){
                    ToastUtil.show(mContext, "没有相关订单");
                    mYgList.clear();
                    mMyAdapter.notifyDataSetChanged();

                }
            } else {
                if (mPageNum == 1) {
                    mYgList.clear();
                }
                mYgList.addAll(orderYginfo);
                mMyAdapter.notifyDataSetChanged();
                //-------------------------------------------------------------------------
                YongGongList.clear();
                YongNewGoIdList.clear();
                for (int i = 0; i < orderYginfo.size(); i++) {
                    String ygblcname = orderYginfo.get(i).getYgblcname();//工种
                    String ygbdtimearrival = orderYginfo.get(i).getYgbdtimearrival();//施工时间
                    String ygbdgprovince = orderYginfo.get(i).getYgbdprovince();//getYgbdprovince
                    String ygbdgcity = orderYginfo.get(i).getYgbdcity();
                    String ygbdarea = orderYginfo.get(i).getYgbdarea();
                    String ygbdaddress = orderYginfo.get(i).getYgbdaddress();//地点
                    String ygbddays = orderYginfo.get(i).getYgbddays();//天数
                    String ygboid = orderYginfo.get(i).getYgboid();

                    String YongGong = "需求工种是" + ygblcname + "到场时间是" + ygbdtimearrival + "地址是在"
                            + ygbdgprovince + ygbdgcity + ygbdarea + ygbdaddress + "用工天数为" + ygbddays + "天"+"。下一条：";

                    YongGongList.add(YongGong);
                    YongNewGoIdList.add(ygboid);
                }
                /**
                 * @param 判断语音 是否最新
                 */
                if (YongOldGoIdList.size() > 0 && YongNewGoIdList.size() > 0) {
                    String Yongold = "";
                    for (int j = 0; j < YongOldGoIdList.size(); j++) {
                        if (Yongold == "") {
                            Yongold = YongOldGoIdList.get(j);
                        } else {
                            Yongold = Yongold + YongOldGoIdList.get(j);
                        }
                    }
                    String Yongnew = "";
                    for (int j = 0; j < YongNewGoIdList.size(); j++) {
                        if (Yongnew == "") {
                            Yongnew = YongNewGoIdList.get(j);
                        } else {
                            Yongnew = Yongnew + YongNewGoIdList.get(j);
                        }
                    }
                    if (Yongold.equals(Yongnew)) {
                    } else {
//                        Log.e("strnew", "不一样");
                        if(!flag){//是否在播放语音 状态
                            stopSpeaking();//停止当前播放
                            startSpeaking();//加载新的订单信息
                            YongOldGoIdList.clear();
                            YongOldGoIdList.addAll(YongNewGoIdList);
                        }
                    }
                }

            }

        }

    }
    /***
     * 学堂解析
     * */
    private void getTeacherData(String s) {
        Gson gson = new Gson();
        DataDemanJj dataDemanJj = gson.fromJson(s, DataDemanJj.class);
        if (dataDemanJj.getData().getSuccess().equals("1")) {
            List<OrderJjinfoBean> orderJjinfo = dataDemanJj.getData().getOrderJjinfo();
            if (orderJjinfo.size() == 0) {
                if("1".equals(flagCoad)){
                    ToastUtil.show(mContext, "没有相关订单");
                    mTList.clear();
                    mMyAdapter.notifyDataSetChanged();
                }
            } else {
                if (mPageNum == 1) {
                    mTList.clear();
                }
                mTList.addAll(orderJjinfo);
                mMyAdapter.notifyDataSetChanged();
                //-----------------------------------------
                    YongTList.clear();
                    YongNewGoIdList.clear();
                    for (int i = 0; i < orderJjinfo.size(); i++) {
                        String ygbscname = orderJjinfo.get(i).getYgbscname();//科目
                        String ygbdgmounttime = orderJjinfo.get(i).getYgbdgmounttime();//开课时间
                        String ygbdgprovince = orderJjinfo.get(i).getYgbdgprovince();
                        String ygbdgcity = orderJjinfo.get(i).getYgbdgcity();
                        String ygbdgarea = orderJjinfo.get(i).getYgbdgarea();
                        String ygbdgaddress = orderJjinfo.get(i).getYgbdgaddress();//地点
                        String ygbdgdays = orderJjinfo.get(i).getYgbdgdays();//天数
                        String ygbodid = orderJjinfo.get(i).getYgboid();
                        String YongGong = "学科是" + ygbscname + "上课时间是" + ygbdgmounttime + "地址是在"
                                + ygbdgprovince + ygbdgcity + ygbdgarea + ygbdgaddress + "上课天数为" + ygbdgdays + "天" + "。下一条：";
                        YongTList.add(YongGong);
                        YongNewGoIdList.add(ygbodid);
                    }
                    /**
                     * @param 判断语音 是否最新
                     */
                    if (YongOldGoIdList.size() > 0 && YongNewGoIdList.size() > 0) {
                        String Yongold = "";
                        for (int j = 0; j < YongOldGoIdList.size(); j++) {
                            if (Yongold == "") {
                                Yongold = YongOldGoIdList.get(j);
                            } else {
                                Yongold = Yongold + YongOldGoIdList.get(j);
                            }
                        }
                        String Yongnew = "";
                        for (int j = 0; j < YongNewGoIdList.size(); j++) {
                            if (Yongnew == "") {
                                Yongnew = YongNewGoIdList.get(j);
                            } else {
                                Yongnew = Yongnew + YongNewGoIdList.get(j);
                            }
                        }
                        if (Yongold.equals(Yongnew)) {
                        } else {
                            Log.e("strnew", "不一样");
                            if (!flag) {//是否在播放语音 状态
                                stopSpeaking();//停止当前播放
                                startSpeaking();//加载新的订单信息
                                YongOldGoIdList.clear();
                                YongOldGoIdList.addAll(YongNewGoIdList);
                            }
                        }
                    }
                }


        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_demand;
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

    @OnClick({R.id.ll_open, R.id.lin_top_right, R.id.lin_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_open:
                if (flag) {
                    image_open.setImageResource(R.mipmap.nav_btn_stop);// nav_btn_stop   nav_btn_play
                    tv_open.setText("关闭");
                    flag = false;
                    startSpeaking();
                } else {
                    image_open.setImageResource(R.mipmap.nav_btn_play);//
                    tv_open.setText("开启");
                    flag = true;
                    stopSpeaking();
                }
                break;
            case R.id.lin_top_right:

                moment = "";
                subject = "0";
                distance = "0";
                days = "0";
                kind = "";
                workers = "0";
                ygbeducation="";

                mSelectHeadteacherDialog.show();
                break;
            case R.id.lin_back:
                mViewpager.setCurrentItem(0);
                break;

        }
    }
    private void startSpeaking() {// YongGongList  YongTList

        if (type.equals("1")) {// 1用工2学堂
            String YongGongMsg = "";
            if (YongGongList.size() == 0) {
                return;
            }
            for (int j = 0; j < YongGongList.size(); j++) {
                if (YongGongMsg == "") {
                    YongGongMsg = YongGongList.get(j);
                } else {
                    YongGongMsg = YongGongMsg + YongGongList.get(j);
                }
            }

            mTts.startSpeaking(YongGongMsg, mTtsListener);

        } else if (type.equals("2")) {
            if (YongTList.size() == 0) {
                return;
            }
            String YongtMsg = "";
            for (int j = 0; j < YongTList.size(); j++) {
                if (YongtMsg == "") {
                    YongtMsg = YongTList.get(j);
                } else {
                    YongtMsg = YongtMsg + YongTList.get(j);
                }
            }
            mTts.startSpeaking(YongtMsg, mTtsListener);
        }

        ToastUtil.show(mContext, "正在加载语音请稍等.....");
    }

    /**
     * 显示对话框
     */
    private void stopSpeaking() {
        mTts.stopSpeaking();
        ToastUtil.show(mContext, "停止播放");
    }
    /**
     * 隐藏对话框
     */
    private void dismissSelectHeadteacherDialog() {
        if (mSelectHeadteacherDialog != null) {
            mSelectHeadteacherDialog.dismiss();
        }
    }
    private void initData() {
        mCheckList.clear();
        ArrayList<DataCheckChild> xlList = new ArrayList<>();
        if (genre) {
            mCheckList.add(new DataCheck("工种", "", ""));
            mCheckList.add(new DataCheck("出工人数", "", ""));
            mCheckList.add(new DataCheck("附近", "", ""));
            mCheckList.add(new DataCheck("用工天数", "", ""));
            ArrayList<DataCheckChild> workList = new ArrayList<>();   //工种
//            if (gzdjlist != null) {
//                for (int i = 0; i < gzdjlist.size(); i++) {
//                    String classname = gzdjlist.get(i).getYgblcname();
//                    String classcd = gzdjlist.get(i).getYgblcid();
//                    workList.add(new DataCheckChild(classname, classcd));
//                }
//            }
            ArrayList<DataCheckChild> peopleList = new ArrayList<>();  //人数
            peopleList.add(new DataCheckChild("全部", "0"));
            peopleList.add(new DataCheckChild("1人", "1"));
            peopleList.add(new DataCheckChild("2人", "2"));
            peopleList.add(new DataCheckChild("3人", "3"));
            peopleList.add(new DataCheckChild("4人", "4"));
            peopleList.add(new DataCheckChild("5人及以上", "5"));
            mCheckListChild.add(0, workList);
            mCheckListChild.add(1, peopleList);
        } else {
            mCheckList.add(new DataCheck("年级", "", ""));
            mCheckList.add(new DataCheck("科目", "", ""));
            mCheckList.add(new DataCheck("附近", "", ""));
            mCheckList.add(new DataCheck("上课天数", "", ""));
            mCheckList.add(new DataCheck("学历", "", ""));
            ArrayList<DataCheckChild> classList = new ArrayList<>();

//            for (int i = 0; i < kmlist.size(); i++) {
//                String classname = kmlist.get(i).getClassname();
//                String classcd = kmlist.get(i).getClasscd();
//                classList.add(new DataCheckChild(classname, classcd));
//            }
            for (int i = 0; i < datadictionary.size(); i++) {
                if ("hznj".equals(datadictionary.get(i).getZldm())) {//孩子年级
                    List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                    for (int ii = 0; ii < dmlist.size(); ii++) {
//                        ChangBean chanbean = new ChangBean();
                        String classname = dmlist.get(ii).getDmmc();
                        String classcd = dmlist.get(ii).getDm();
//                        chanbean.setName(classname);
//                        chanbean.setId(classcd);
//                        mInfoList.add(chanbean);
                        classList.add(new DataCheckChild(classname, classcd));
                    }
                }
            }
            kemuList = new ArrayList<>();
            mCheckListChild.add(0, classList);
            mCheckListChild.add(1, kemuList);
            //子选项的，科目要 先获取 年级来得到，

            for (int i = 0; i < datadictionary.size(); i++) {
                if ("xl".equals(datadictionary.get(i).getZldm())) {//学历
                    List<DmlistBean> dmlist = datadictionary.get(i).getDmlist();
                    for (int ii = 0; ii < dmlist.size(); ii++) {
//                        ChangBean chanbean = new ChangBean();
                        String dmmc = dmlist.get(ii).getDmmc();
                        String dm = dmlist.get(ii).getDm();
//                        chanbean.setName(dmmc);
//                        chanbean.setId(dm);
//                        mInfoList.add(chanbean);
                        xlList.add(new DataCheckChild(dmmc, dm));
                    }
                }
            }
        }
        if (datadictionary == null) {
            return;
        }
        // -------------------------------  距离  用工时间 用工和学堂 共用
        ArrayList<DataCheckChild> nearList = new ArrayList<>();//距离
        for (int i = 0; i < datadictionary.size(); i++) {
            if (datadictionary.get(i).getZldm().equals("gls")) {
                for (int j = 0; j < datadictionary.get(i).getDmlist().size(); j++) {
                    String classname = datadictionary.get(i).getDmlist().get(j).getDmmc();
                    String classcd = datadictionary.get(i).getDmlist().get(j).getDm();
                    nearList.add(new DataCheckChild(classname, classcd));
                }
            }
        }

        ArrayList<DataCheckChild> timeList = new ArrayList<>();//用工时间
        timeList.add(new DataCheckChild("全部", "0"));
        timeList.add(new DataCheckChild("1天", "1"));
        timeList.add(new DataCheckChild("2天", "2"));
        timeList.add(new DataCheckChild("3天", "3"));
        timeList.add(new DataCheckChild("4天", "4"));
        timeList.add(new DataCheckChild("5天及以上", "5"));
        mCheckListChild.add(2, nearList);
        mCheckListChild.add(3, timeList);
        mCheckListChild.add(4, xlList);
    }

    private void initSelectHeadteacherDialog() {

        mSelectHeadteacherDialog = new Dialog(getActivity(), R.style.dialog_bottom_full);
        mSelectHeadteacherDialog.setCanceledOnTouchOutside(true);
        mSelectHeadteacherDialog.setCancelable(true);

        Window window = mSelectHeadteacherDialog.getWindow();

        window.setGravity(Gravity.TOP);

        View view = View.inflate(getActivity(), R.layout.attendance_lay_deman, null);

        expandableListView = (ExpandableListView) view.findViewById(R.id.id_elv);

        myExpandableGridViewadapter
                = new ExpandableListGridViewAdapter(getActivity(),
                expandableListView, mCheckList, mCheckListChild, this);
        expandableListView.setAdapter(myExpandableGridViewadapter);


        view.findViewById(R.id.btn_chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//重置

                dismissSelectHeadteacherDialog();

                moment = "";
                subject = "0";
                distance = "0";
                days = "0";
                kind = "";
                workers = "0";
                ygbeducation="";

                if (genre) {
                    mCheckList.set(0, new DataCheck("工种", "", ""));
                    mCheckList.set(1, new DataCheck("出工人数", "", ""));
                    mCheckList.set(2, new DataCheck("附近", "", ""));
                    mCheckList.set(3, new DataCheck("用工天数", "", ""));
                } else {
                    mCheckList.set(0, new DataCheck("年级", "", ""));
                    mCheckList.set(1, new DataCheck("科目", "", ""));
                    mCheckList.set(2, new DataCheck("附近", "", ""));
                    mCheckList.set(3, new DataCheck("上课天数", "", ""));
                    mCheckList.set(4, new DataCheck("学历", "", ""));
                }
                mPageNum = 1;
                findorder();
                myExpandableGridViewadapter.notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dismissSelectHeadteacherDialog();
                //筛选完重置
                if (genre) {
                    mCheckList.set(0, new DataCheck("工种", "", ""));
                    mCheckList.set(1, new DataCheck("出工人数", "", ""));
                    mCheckList.set(2, new DataCheck("附近", "", ""));
                    mCheckList.set(3, new DataCheck("用工天数", "", ""));
                } else {
                    mCheckList.set(0, new DataCheck("年级", "", ""));
                    mCheckList.set(1, new DataCheck("科目", "", ""));
                    mCheckList.set(2, new DataCheck("附近", "", ""));
                    mCheckList.set(3, new DataCheck("上课天数", "", ""));
                    mCheckList.set(4, new DataCheck("学历", "", ""));
                }

                myExpandableGridViewadapter.notifyDataSetChanged();

                mPageNum = 1;

                findorder();

            }
        });
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    //控制单个组的展开和伸缩，也可以控制展开当前点击的组，关闭其他展开的组

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        for (int i = 0, count = expandableListView
                                .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                            if (groupPosition != i) {// 关闭其他分组
                                expandableListView.collapseGroup(i);
                            }
                        }
                    }
                });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

//用工 ---工种 人数 距离 用工时间      家教 ---年级 科目 距离 用工时间   group  0  1  2  3
                if(genre){//用工

                if (i == 0) {

                    numtype = 0;

                    Intent intent=new Intent(getActivity(),ReleaseDemandActivity.class);
                    intent.putExtra("flag","2");
                    startActivity(intent);


                } else if (i == 1) {
                    numtype = 1;
                } else if (i == 2) {
                    numtype = 2;
                } else if (i == 3) {
                    numtype = 3;
                }

                }else{//家教

                    if (i == 0) {
                        numtype = 0;
                    } else if (i == 1) {
                        numtype = 1;
                    } else if (i == 2) {
                        numtype = 2;
                    } else if (i == 3) {
                        numtype = 3;
                    }else if(i == 4){
                        numtype = 4;
                    }

                }


                return false;
            }
        });


        window.setContentView(view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏

    }

    private void findorder() {

        flagCoad ="1";
        mFindDemanMap.clear();
        mFindDemanMap.put("userid", userid);//用户id
        mFindDemanMap.put("type", type);//用工类型 1 师傅 2家教
        mFindDemanMap.put("pageNumber", String.valueOf(mPageNum));
        mFindDemanMap.put("pageSize", String.valueOf(mPageSize));
        String latitude = (String) SPUtil.get("latitude", "");//纬度
        String longitude = (String) SPUtil.get("longitude", "");//经度
        mFindDemanMap.put("lat", latitude);//纬度
        mFindDemanMap.put("lng", longitude);//经度
        mFindDemanMap.put("kind", kind);        //工种 筛选条件为全部时传空
        mFindDemanMap.put("workers", workers);  //人数 如果筛选条件为全部的传0，这些参数为必填
        mFindDemanMap.put("moment", moment);    //年级 如果筛选条件为全部的传0，这些参数为必填
        mFindDemanMap.put("subject", subject);  //科目 如果筛选条件为全部的传0，这些参数为必填
        mFindDemanMap.put("distance", distance);//公里 如果筛选条件为全部的传0，这些参数为必填
        mFindDemanMap.put("days", days);        //天数 如果筛选条件为全部的传0，这些参数为必填
        mFindDemanMap.put("ygbeducation", ygbeducation);        // 学历
        String lcityid = (String) SPUtil.get("lcityid", "");
        mFindDemanMap.put("lcity", lcityid);        // 城市代码
        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.FINDORDER_URL, null, mFindDemanMap)
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
                    public void onNext(String s) {//{"data":{"success":"0"},"code":"0","msg":"失败"}
//                        Log.e("ygbeducation",""+s);
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {
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

    @Override
    public void onGridItemClick(int groupPosition, int childPosition) {
        String childid = mCheckListChild.get(groupPosition).get(childPosition).getChildid();
        String childname = mCheckListChild.get(groupPosition).get(childPosition).getChildname();
//        Log.e("childname","="+childname);
        String substring = childname.substring(0,2);
        if (genre) {//用工
            if (numtype == 0) {
                kind = childid;
            } else if (numtype == 1) {
                workers = childid;
            } else if (numtype == 2) {
                distance = childid;
            } else if (numtype == 3) {
                days = childid;
            }
        } else {//家教

            if (numtype == 0) {
                moment = childid;
                kemuList.clear();
                for (int i = 0; i < kmlist.size(); i++) {
                    if (substring.equals(kmlist.get(i).getClassname())) {
                        List<SubjectBean> subject = kmlist.get(i).getSubject();
                        for (int ii = 0; ii < subject.size(); ii++) {
                            String classname = subject.get(ii).getSubjectname();
                            String classcd = subject.get(ii).getSubjectcd();
                            kemuList.add(new DataCheckChild(classname, classcd));
                        }
                    }
                }
                mCheckListChild.set(1, kemuList);
                myExpandableGridViewadapter.notifyDataSetChanged();
            } else if (numtype == 1) {
                subject = childid;
            } else if (numtype == 2) {
                distance = childid;
            } else if (numtype == 3) {
                days = childid;
            }else if(numtype == 4){
                ygbeducation=childid;
            }

        }

    }

    //-------------------------------------------
    /***
     * 获取 语音 播报 列表数据
     * */
    // TODO 获取 语音 播报
    private void getDemanVoice() {
//
        String lcityid = (String) SPUtil.get("lcityid", "");

        mDemanLiVli.put("userid", userid);
        mDemanLiVli.put("type", type);
        mDemanLiVli.put("lcity", lcityid);

        OkHttp3Utils.getInstance().doPost(Constants.DEMANDLISTLIMINT_URL, null, mDemanLiVli)
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
//                        SPUtil.put("VoiceData", s);
//                        String SPvoiceData = (String) SPUtil.get("VoiceData", "");
                        Gson gson = new Gson();
                        if (!TextUtils.isEmpty(s)) {
                            if (type.equals("1")) {
                                DataDemanYg dataDemanYg = gson.fromJson(s, DataDemanYg.class);
                                if (dataDemanYg.getData().getSuccess()==1) {
                                    List<OrderYginfoBean> orderYginfo = dataDemanYg.getData().getOrderYginfo();
                                    if (orderYginfo.size() == 0) {
                                        ToastUtil.show(mContext, "没有相关语音订单信息");
                                    } else {

                                        YongGongList.clear();
                                        YongNewGoIdList.clear();
                                        for (int i = 0; i < orderYginfo.size(); i++) {
                                            String ygblcname = orderYginfo.get(i).getYgblcname();//工种
                                            String ygbdtimearrival = orderYginfo.get(i).getYgbdtimearrival();//施工时间
                                            String ygbdgprovince = orderYginfo.get(i).getYgbdprovince();//getYgbdprovince
                                            String ygbdgcity = orderYginfo.get(i).getYgbdcity();
                                            String ygbdarea = orderYginfo.get(i).getYgbdarea();
                                            String ygbdaddress = orderYginfo.get(i).getYgbdaddress();//地点
                                            String ygbddays = orderYginfo.get(i).getYgbddays();//天数
                                            String ygboid = orderYginfo.get(i).getYgboid();

                                            String YongGong = "需求工种是" + ygblcname + "到场时间是" + ygbdtimearrival + "地址是在"
                                                    + ygbdgprovince + ygbdgcity + ygbdarea + ygbdaddress + "用工天数为" + ygbddays + "天"+"。下一条：";

                                            YongGongList.add(YongGong);
                                            YongNewGoIdList.add(ygboid);
                                        }
                                        /**
                                         * @param 判断语音 是否最新
                                         */
                                        if (YongOldGoIdList.size() > 0 && YongNewGoIdList.size() > 0) {
                                            String Yongold = "";
                                            for (int j = 0; j < YongOldGoIdList.size(); j++) {
                                                if (Yongold == "") {
                                                    Yongold = YongOldGoIdList.get(j);
                                                } else {
                                                    Yongold = Yongold + YongOldGoIdList.get(j);
                                                }
                                            }
                                            String Yongnew = "";
                                            for (int j = 0; j < YongNewGoIdList.size(); j++) {
                                                if (Yongnew == "") {
                                                    Yongnew = YongNewGoIdList.get(j);
                                                } else {
                                                    Yongnew = Yongnew + YongNewGoIdList.get(j);
                                                }
                                            }
                                            if (Yongold.equals(Yongnew)) {
                                            } else { Log.e("strnew", "不一样");
                                                if(!flag){//是否在播放语音 状态
                                                    stopSpeaking();//停止当前播放
                                                    startSpeaking();//加载新的订单信息
                                                    YongOldGoIdList.clear();
                                                    YongOldGoIdList.addAll(YongNewGoIdList);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (type.equals("2")) {
                                DataDemanJj dataDemanJj = gson.fromJson(s, DataDemanJj.class);
                                if (dataDemanJj.getData().getSuccess().equals("1")) {
                                    List<OrderJjinfoBean> orderJjinfo = dataDemanJj.getData().getOrderJjinfo();
                                    YongTList.clear();
                                    YongNewGoIdList.clear();
                                    for (int i = 0; i < orderJjinfo.size(); i++) {
                                        String ygbscname = orderJjinfo.get(i).getYgbscname();//科目
                                        String ygbdgmounttime = orderJjinfo.get(i).getYgbdgmounttime();//开课时间
                                        String ygbdgprovince = orderJjinfo.get(i).getYgbdgprovince();
                                        String ygbdgcity = orderJjinfo.get(i).getYgbdgcity();
                                        String ygbdgarea = orderJjinfo.get(i).getYgbdgarea();
                                        String ygbdgaddress = orderJjinfo.get(i).getYgbdgaddress();//地点
                                        String ygbdgdays = orderJjinfo.get(i).getYgbdgdays();//天数
                                        String ygbodid = orderJjinfo.get(i).getYgboid();
                                        String YongGong = "学科是" + ygbscname + "上课时间是" + ygbdgmounttime + "地址是在"
                                                + ygbdgprovince + ygbdgcity + ygbdgarea + ygbdgaddress + "上课天数为" + ygbdgdays + "天"+"。下一条：";
                                        YongTList.add(YongGong);
                                        YongNewGoIdList.add(ygbodid);
                                    }
                                    /**
                                     * @param 判断语音 是否最新
                                     */
                                    if (YongOldGoIdList.size() > 0 && YongNewGoIdList.size() > 0) {
                                        String Yongold = "";
                                        for (int j = 0; j < YongOldGoIdList.size(); j++) {
                                            if (Yongold == "") {
                                                Yongold = YongOldGoIdList.get(j);
                                            } else {
                                                Yongold = Yongold + YongOldGoIdList.get(j);
                                            }
                                        }
                                        String Yongnew = "";
                                        for (int j = 0; j < YongNewGoIdList.size(); j++) {
                                            if (Yongnew == "") {
                                                Yongnew = YongNewGoIdList.get(j);
                                            } else {
                                                Yongnew = Yongnew + YongNewGoIdList.get(j);
                                            }
                                        }
                                        if (Yongold.equals(Yongnew)) {
                                        } else {Log.e("strnew","不一样");
                                            if(!flag){//是否在播放语音 状态
                                                stopSpeaking();//停止当前播放
                                                startSpeaking();//加载新的订单信息
                                                YongOldGoIdList.clear();
                                                YongOldGoIdList.addAll(YongNewGoIdList);
                                            }
                                        }
                                    }

                                }
                            }
                        } else {
                            ToastUtil.show(mContext, "服务器没有数据");
                        }
                    }
                });
    }
}
