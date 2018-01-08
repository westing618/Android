package com.ztd.yyb.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.order.LaborDemandDetailsActivity;
import com.ztd.yyb.activity.order.SchoolNeedsDetailsActivity;
import com.ztd.yyb.activity.order.SearchActivity;
import com.ztd.yyb.adapter.HomeAdapter;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.base.BaseApplication;
import com.ztd.yyb.bean.beanHome.AppConfigBean;
import com.ztd.yyb.bean.beanHomee.HomeData;
import com.ztd.yyb.bean.beanHomee.OrderYginfoBean;
import com.ztd.yyb.bean.beanHomee.PicarrayBean;
import com.ztd.yyb.bean.beanWeather.DataWeath;
import com.ztd.yyb.bean.beanWeather.WeatherinfoBean;
import com.ztd.yyb.evenbean.MessageEvent;
import com.ztd.yyb.receiver.MyMessService;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.view.RollHeaderView;

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

import static rx.Observable.timer;


/**
 * Created by  on 2017/3/10. 第一个 首页(用工 学堂)
 */

public class HomeActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String PAR_KEY_BANNER = "banner";
    public static String TYPE = "tppe";// 跳转类型 true 用工  flase 学堂
    public static String APPCONFIGBEAN = "APPCONFIGBEAN";// 获取 系统参数数据字典
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private final String TAG = getClass().getSimpleName();
    //TODO 百度地图 定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    Map<String, String> mConfigMap = new HashMap<>();
    //    private int mPageNum = 1;//页码
//    private int mPageSize = 10;//一次获取几条数据
    TextView tv_weather;
    TextView tv_temp;
    TextView tv_dqcgds;
    TextView mTvhonmecityname;
    TextView tv_dqcgrs;
    ImageView imaSmile;
    ImageView imae_weather;
//    String lcity = "";
    String lcityName = "厦门";
    String lcityid = "508";
    @BindView(R.id.home_refresh)
    BGARefreshLayout mRefreshLayout;
    //    String link;//小图地址
    com.ztd.yyb.bean.beanHomee.SmallimageBean smallimage;
    private RecyclerView mRecyclerView;
    private HomeAdapter mMyAdapter;
    private RollHeaderView banner;
    private Map<String, String> mHomeMap = new HashMap<>();
    private Map<String, String> mWeaMap = new HashMap<>();
    private List<OrderYginfoBean> mjobarrayList = new ArrayList<>();

    RelativeLayout relative_three;

    TextView mTvpcnt;
    private boolean state = true;
    private int num=0;

    public void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAddtBus(String message) {
        if (message != null) {
//-------------------- 控制 定制消息 红包提醒
            if("{}".equals(message)){
                state = true;
            }else {
                state = false;
//                try {
//                    Gson gson = new Gson();
//                    MainNActivity.JsonRootBean jsonRootBean = gson.fromJson(message, MainNActivity.JsonRootBean.class);
//                    String isAccount = jsonRootBean.getIsAccount();
//                    if ("1".equals(isAccount)) {
//                        tvMTipcnt.setVisibility(View.VISIBLE);
//                    } else {
//                        tvMTipcnt.setVisibility(View.INVISIBLE);
//                    }
//                } catch (Exception e) {
//                }
//                num++;
                mTvpcnt.setVisibility(View.VISIBLE);
                mTvpcnt.setText("");

            }

        }
//-------------------- 控制 系统消息 提示  1 系统消息    3 聊天的消息
        if (state) {
            String mymessage = (String) SPUtil.get("messageFragment", "");
            if ("1".equals(mymessage)) {
                num++;
                mTvpcnt.setVisibility(View.VISIBLE);
                mTvpcnt.setText(""+num);
            } else if("3".equals(mymessage)){
                num++;
                mTvpcnt.setVisibility(View.VISIBLE);
                mTvpcnt.setText(""+num);
            }else {
                mTvpcnt.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void initViewsAndEvents() {

        EventBus.getDefault().register(this);

        mLocationClient = new LocationClient(this);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        inView();
        initData();

        verifyStoragePermissions(this);
        SPUtil.put("lcityid", "508");//z默认厦门
        lcityid="508";

    }

    private void inView() {

        mTvhonmecityname = (TextView) findViewById(R.id.tv_honmecityname);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));

        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new HomeAdapter(HomeActivity.this, mjobarrayList);
        mRecyclerView.setAdapter(mMyAdapter);
        setHeaderView(mRecyclerView);
        setFooterView(mRecyclerView);

    }

    private void initData() {

        getHomeData();    //可以

        getAppConfig();

        initLocation();

        getReadState();

        getWeather();

        Intent startIntent = new Intent(this, MyMessService.class);
        startService(startIntent);// TODO 开个服务 APP 在后台时可以接收消息


    }

    private void getWeather() {

        mWeaMap.clear();
        mWeaMap.put("city",lcityName);

        OkHttp3Utils.getInstance().doPost(Constants.WEATHER_CONFIG_URL, null, mWeaMap)
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
//                        Log.e("getWeather","="+s);

                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            DataWeath dataWeath = gson.fromJson(s, DataWeath.class);
                            WeatherinfoBean weatherinfo = dataWeath.getWeatherinfo();

                            String weather = weatherinfo.getWeather();
                            String weatherbackground = weatherinfo.getWeatherbackground();
                            String weatherurl = weatherinfo.getWeatherurl();
                            String temp1 = weatherinfo.getTemp1();
                            String temp2 = weatherinfo.getTemp2();

                            tv_weather.setText(weather);

                              // 加载图片  天气图片
                                Glide.with(mContext).load(Constants.BASE_URL + weatherurl)
                                        .error(R.mipmap.mo_cal)
                                        .placeholder(R.mipmap.mo_cal)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imae_weather);

                            tv_temp.setText(temp1+"~"+temp2+"℃");

//                            int COLOR = Color.parseColor(weatherbackground);
//                            relative_three.setBackgroundColor(COLOR);

                        }
                    }
                });

    }

    public String getVersion() {
        String version="";
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
             version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    //主动 获取 定位 权限 安卓 6.0
    private void getReadState() {

        if (PermissionsUtil.hasPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            mLocationClient.start();

        } else {

            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {

                    mLocationClient.start();

                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {

                }
            }, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
        }

    }

    //获取 系统参数数据字典
    private void getAppConfig() {

        OkHttp3Utils.getInstance().doPost(Constants.APP_CONFIG_URL, null, mConfigMap)
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

//                        Log.e("系统参数数据字典","="+s);

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            try {

                                AppConfigBean appConfigBean = gson.fromJson(s, AppConfigBean.class);

                                String androidVersion = appConfigBean.getData().getAndroidVersion();
                                String androidUrl = appConfigBean.getData().getAndroidUrl();

                                SPUtil.put(APPCONFIGBEAN, s);

//                                Log.e("androidUrl","="+androidUrl);
//                                Log.e("androidVersion","="+androidVersion);

                                String version = getVersion();

                                if (androidVersion != null) {//比对APP版本号
                                    if (!androidVersion.equals(version)) {
                                        showVer(androidUrl);
                                    }
                                }

                            } catch (Exception e) {

                                ToastUtil.show(mContext, "系统参数数据字典数据异常");

                            }


                        }

                    }
                });
    }

    private void showVer(final String url) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        builder.setMessage("发现新版本");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Uri uri = Uri.parse(url);
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(it);
                        }catch (Exception e){
                        }

                    }
                });
        builder.setNegativeButton("取消", null);
        builder.setCancelable(false);
        builder.show();

    }

    private void getHomeData() {

        if (!BaseApplication.checkNet()) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("没有网络哦亲...")
                    .show();
            return;
        }


        mHomeMap.clear();
        mHomeMap.put("lcity", lcityid);
        Log.e("lcityid","="+lcityid);

        showLoadingDialog();
        OkHttp3Utils.getInstance().doPost(Constants.MAIN_BANNER_URL, null, mHomeMap)
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
//                        mjobarrayList.clear();//第一首页 ，没有上拉加载，只有刷新
//                        try {
                        if (!TextUtils.isEmpty(s)) {
                            mjobarrayList.clear();
                            Gson gson = new Gson();
                            HomeData homeData = gson.fromJson(s, HomeData.class);
                            if ("true".equals(homeData.getResult())) {
//                                com.ztd.yyb.bean.beanHomee.WeatherinfoBean weatherinfo = homeData.getData().getWeatherinfo();
//                                String weather = weatherinfo.getWeather();//天气 晴，阴，雨
//                                String temp1 = weatherinfo.getTemp1();//早上温度
//                                String temp2 = weatherinfo.getTemp2();//下午温度
//                                tv_weather.setText(weather);
                                smallimage = homeData.getData().getSmallimage();
                                String url = smallimage.getUrl();
//                                String weatherurl = homeData.getData().getWeatherinfo().getWeatherurl();
                                // 加载图片  小广告图
                                Glide.with(mContext).load(Constants.BASE_URL + "upload/image/" + url)
                                        .error(R.mipmap.mo_cal)
                                        .placeholder(R.mipmap.mo_cal)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imaSmile);
//                                // 加载图片  天气图片
//                                Glide.with(mContext).load(Constants.BASE_URL + weatherurl)
//                                        .error(R.mipmap.mo_cal)
//                                        .placeholder(R.mipmap.mo_cal)
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .into(imae_weather);

//                                GregorianCalendar ca = new GregorianCalendar();//判断上午 下午
//                                int a = ca.get(GregorianCalendar.AM_PM);//结果为“0”是上午     结果为“1”是下午
//
//                                if (a == 0) {
//                                    tv_temp.setText(temp1);
//                                } else if (a == 1) {
//                                    tv_temp.setText(temp2);
//                                }

                                String dqcgds = homeData.getData().getDqcgds();//当前出工单数
                                String dqcgrs = homeData.getData().getDqcgrs();//当前出工人数

                                tv_dqcgds.setText("-  共" + dqcgds + "单  -");
                                if(dqcgrs.equals("")){
                                    tv_dqcgrs.setText("0");
                                }else {
                                    tv_dqcgrs.setText(dqcgrs);
                                }


                                final List<PicarrayBean> picarray = homeData.getData().getPicarray();
//                                    final List<PicarrayBean> picarray = homeData.getData().getPicarray();
                                List<String> imgUrlList = new ArrayList<>();

                                for (int i = 0; i < picarray.size(); i++) {//url : "banner3@2x.png"
                                    String imagurl = Constants.BASE_URL + "upload/image/" + picarray.get(i).getUrl();
                                    imgUrlList.add(imagurl);
                                }

                                banner.setImgUrlData(imgUrlList);// banner设置图片显示

                                banner.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
                                    @Override
                                    public void HeaderViewClick(int position) {//type： 1 资讯 2 订单 3 外链接

                                        String type = picarray.get(position).getType();
                                        String picid = picarray.get(position).getPicid();
                                        String link = picarray.get(position).getLink();
                                        String kind = picarray.get(position).getKind();
                                        String aid = picarray.get(position).getAid();
                                        if (type != null) {
                                            if (type.equals("3")) {
                                                Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
                                                intent.putExtra(PAR_KEY_BANNER, link);
                                                startActivity(intent);
                                            } else if (type.equals("2")) {
                                                if (kind != null) {// 种类 1 广告 2 用工 3 家教
                                                    if (kind.equals("2")) {
                                                        SPUtil.put("flag", "1");
                                                        Intent intent1 = new Intent(HomeActivity.this, LaborDemandDetailsActivity.class);
                                                        intent1.putExtra("ygboid", picid);
                                                        startActivity(intent1);
                                                    } else if (kind.equals("1")) {
                                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                                        Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
                                                        intent.putExtra("banner", url);
                                                        startActivity(intent);
                                                    } else if (kind.equals("3")) {
                                                        SPUtil.put("flag", "1");
                                                        Intent intent2 = new Intent(HomeActivity.this, SchoolNeedsDetailsActivity.class);
                                                        intent2.putExtra("ygboid", picid);
                                                        startActivity(intent2);
                                                    }
                                                }

                                            } else if (type.equals("1")) {
                                                if (kind != null) {
                                                    if (kind.equals("1")) {
                                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                                        Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
                                                        intent.putExtra("banner", url);
                                                        startActivity(intent);
                                                    } else if (kind.equals("2")) {
                                                        String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;
                                                        Intent intent = new Intent(HomeActivity.this, BannerActivity.class);
                                                        intent.putExtra("banner", url);
                                                        startActivity(intent);

                                                    }

                                                }

                                            }

                                        }


                                    }
                                });


                                List<OrderYginfoBean> orderYginfo = homeData.getData().getOrderYginfo();
                                mjobarrayList.addAll(orderYginfo);
                                mMyAdapter.notifyDataSetChanged();

                            }

                        } else {

                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("没有数据...")
                                    .show();
                        }


//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            ToastUtil.show(HomeActivity.this, "数据错误");
//                        }


                    }
                });


    }

    private void setHeaderView(RecyclerView view) {

        View header = LayoutInflater.from(this).inflate(R.layout.item_home_tutor_onehead, view, false);
        mMyAdapter.setHeaderView(header);

         mTvpcnt = (TextView) header.findViewById(R.id.tv_htipcnt);

        header.findViewById(R.id.re_labor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SPUtil.put(HomeActivity.TYPE, true);

                String ISL = (String) SPUtil.get("IS_LOGIN", "");

                if (ISL.equals("1")) {
                    startActivity(new Intent(HomeActivity.this, MainNActivity.class));

                    num=0;
//                    SPUtil.put("messageFragment", "2");
                    mTvpcnt.setVisibility(View.INVISIBLE);

                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

            }

        });//用工

        header.findViewById(R.id.re_school).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SPUtil.put(HomeActivity.TYPE, false);


                String ISL = (String) SPUtil.get("IS_LOGIN", "");

                if (ISL.equals("1")) {

                    startActivity(new Intent(HomeActivity.this, MainNActivity.class));

                } else {

                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));

                }

            }

        });//学堂

        tv_weather = (TextView) header.findViewById(R.id.tv_weather);
        imae_weather = (ImageView) header.findViewById(R.id.imae_weather);
        tv_temp = (TextView) header.findViewById(R.id.tv_temp);
        imaSmile = (ImageView) header.findViewById(R.id.image_smile);

         relative_three = (RelativeLayout) header.findViewById(R.id.relative_three);


        imaSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (smallimage == null) {
                    return;
                }

                String link = smallimage.getLink();
                String picid = smallimage.getPicid();
                String kind = smallimage.getKind();
                String aid = smallimage.getAid();
                String type = smallimage.getType();
                String url = "http://api.yogobei.com/ad.jsp?aid=" + aid;

                if (type != null) {

                    if (type.equals("3")) {

                        Intent intent = new Intent(mContext, BannerActivity.class);
                        intent.putExtra(PAR_KEY_BANNER, "" + link);
                        startActivity(intent);

                    } else if (type.equals("2")) {

                        if (kind != null) {

                            if (kind.equals("2")) {//用工

                                Intent intent1 = new Intent(mContext, LaborDemandDetailsActivity.class);
                                intent1.putExtra("ygboid", picid);
                                startActivity(intent1);

                            } else if (kind.equals("3")) {

                                Intent intent2 = new Intent(mContext, SchoolNeedsDetailsActivity.class);
                                intent2.putExtra("ygboid", picid);
                                startActivity(intent2);

                            } else if (kind.equals("1")) {

                                Intent intent = new Intent(mContext, BannerActivity.class);
                                intent.putExtra("banner", url);
                                startActivity(intent);

                            }
                        }


                    } else if (type.equals("1")) {

                        if (kind != null) {

                            if (kind.equals("1")) {

                                Intent intent = new Intent(mContext, BannerActivity.class);
                                intent.putExtra("banner", url);
                                startActivity(intent);

                            } else if (kind.equals("2")) {
                                Intent intent = new Intent(mContext, BannerActivity.class);
                                intent.putExtra("banner", url);
                                startActivity(intent);
                            }

                        }


                    }

                }


            }
        });

        tv_dqcgds = (TextView) header.findViewById(R.id.tv_dqcgds);
        tv_dqcgrs = (TextView) header.findViewById(R.id.tv_dqcgrs);
        banner = (RollHeaderView) header.findViewById(R.id.banner);

    }

    private void setFooterView(RecyclerView view) {
        View footer = LayoutInflater.from(this).inflate(R.layout.item_home_tutor_foot, view, false);
        mMyAdapter.setFooterView(footer);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_home_oneutor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    private void initLocation() {//该类用来设置定位SDK的定位方式

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度， 低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;//即仅定位一次
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(MessageEvent messageEvent) {

        String msg = messageEvent.getMsg();// 接受城市选择的结果
        String id = messageEvent.getId();
        lcityid=id;
        SPUtil.put("lcityid", id);
        lcityName=msg;
//        lcity = msg;
        mTvhonmecityname.setText(msg);
        getWeather();
        getHomeData();

    }

    @OnClick({R.id.ll_home_search, R.id.lin_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home_search:
                SPUtil.put(HomeActivity.TYPE, true);
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                break;
            case R.id.lin_back:
                startActivity(new Intent(HomeActivity.this, CitySelectionActivity.class));
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {

                        getHomeData();

                        mRefreshLayout.endRefreshing();

                        return null;
                    }
                }).subscribe();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            double latitude = location.getLatitude();//获取纬度信息
            double longitude = location.getLongitude();//获取经度信息
            String locationDescribe = location.getLocationDescribe();//位置语义化信息
            SPUtil.put("locationDescribe", locationDescribe);

            SPUtil.put("latitude", "" + latitude);
            SPUtil.put("longitude", "" + longitude);


        }
    }

}
