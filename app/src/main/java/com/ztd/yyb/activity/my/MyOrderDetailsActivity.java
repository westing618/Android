package com.ztd.yyb.activity.my;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.hss01248.lib.MyItemDialogListener;
import com.hss01248.lib.StytledDialog;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.order.LaborAdapter;
import com.ztd.yyb.activity.uppaypw.ChatActivity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanDetailss.DataDetailss;
import com.ztd.yyb.bean.beanJJDetail.DataJJdetail;
import com.ztd.yyb.bean.beanJJDetail.JjinfoBean;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的订单 详情--- 抢单成功  发布成功  我的订单 我的发布 四个页面跳转过来，用同一个页面展示 订单详情
 * Created by  on 2017/3/13.
 */

public class MyOrderDetailsActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private final String BAIDUMAP = "com.baidu.BaiduMap";
    private final String MINIMAP = "com.autonavi.minimap ";//com.autonavi.minimap
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_detaikind)
    TextView tv_detaikind;
    @BindView(R.id.tv_myodtname)
    TextView tv_myodtname;
    @BindView(R.id.tv_myodtphone)
    TextView tv_myodtphone;
    @BindView(R.id.tv_myodtpaddress)
    TextView tv_myodtpaddress;
    @BindView(R.id.detail_connect)
    TextView detail_connect;
    @BindView(R.id.tv_top_right)
    TextView mTvTopRight;
    @BindView(R.id.tv_detaitime)
    TextView tv_detaitime;
    @BindView(R.id.detal_state)
    TextView detal_state;
    @BindView(R.id.tv_mode)
    TextView tv_mode;
    @BindView(R.id.tv_peoplenum)
    TextView tv_peoplenum;
    @BindView(R.id.tv_pedays)
    TextView tv_pedays;
    @BindView(R.id.tv_finishdays)
    TextView tv_finishdays;
    @BindView(R.id.tv_mprice)
    TextView tv_mprice;

    @BindView(R.id.tv_mpricegd)
    TextView tv_mpricegd;

    @BindView(R.id.tv_mbprice)
    TextView tv_mbprice;

    @BindView(R.id.tv_im)
    TextView tv_im;//图片概述

    String ygboid;
    String type;
    String phone;
    String ygbotypem;//订单状态 8为未开始，不能聊天
    ArrayList<String> pic = new ArrayList<String>();
    LaborAdapter laborAdapter;
    String latitude;
    String longitude;
    String mm;
    private Map<String, String> morderDetailMap = new HashMap<>();
    private boolean where;//true我的订单  false我的发布

    @Override
    protected void initViewsAndEvents() {

        initUI();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        laborAdapter = new LaborAdapter(this, pic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(laborAdapter);
    }

    private void initUI() {

        mTvTitle.setText("订单详情");
        mTvTopRight.setVisibility(View.VISIBLE);
        mTvTopRight.setText("咨询");

        Intent intent = getIntent();

        mm = intent.getStringExtra("MM");// 1 用工详情  2 家教详情

        if (mm == null) {
            return;
        }
        if (mm.equals("1")) {//TODO 我的订单来的用工--
            type = "1";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = true;
//            tv_myodtname.setText("师傅");
        } else if (mm.equals("2")) {//TODO 我的订单来的家教--
//            tv_myodtname.setText("老师");
            type = "2";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = true;
        } else if (mm.equals("3")) {//TODO 发布订单成功家教
//            tv_myodtname.setText("家长");
            type = "2";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = false;
        } else if (mm.equals("4")) {//TODO 发布订单成功用工
            type = "1";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = false;
//            tv_myodtname.setText("业主");
        } else if (mm.equals("5")) {//TODO 我的发布来的用工--
            type = "1";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = false;
//            tv_myodtname.setText("师傅");
        } else if (mm.equals("6")) { //TODO 我的发布来的家教--
//            tv_myodtname.setText("老师");
            type = "2";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = false;
        } else if (mm.equals("7")) {//TODO 抢单成功来的师傅--
            type = "1";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = true;
//            tv_myodtname.setText("业主：");
        } else if (mm.equals("8")) {//TODO --抢单成功来的家教 --
//            tv_myodtname.setText("家长：");
            type = "2";
            ygboid = (String) SPUtil.get("putorderid", "");
            where = true;
        }


        getData();


    }

    private void getData() {

        morderDetailMap.clear();
        morderDetailMap.put("type", type);
        morderDetailMap.put("ygboid", ygboid);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.MYORDERDETAIL_URL, null, morderDetailMap)
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
                            if (type.equals("1")) {
                                parSingYG(s);
                            } else if (type.equals("2")) {
                                parSingJJ(s);
                            }

                        }

                    }
                });


    }

    private void parSingYG(String s) {

        Gson gson = new Gson();

        DataDetailss dataDetailss = gson.fromJson(s, DataDetailss.class);

        if (dataDetailss.getData().getSuccess().equals("1")) {

            com.ztd.yyb.bean.beanDetailss.YginfoBean yginfo = dataDetailss.getData().getYginfo();



            String initiatetel = yginfo.getInitiatetel();

            String ygbdaddress = yginfo.getYgbdaddress();

            String ygbdremark = yginfo.getYgbdremark();

            String ygbdcreatetime = yginfo.getYgbdcreatetime();

            String ygbdtimearrival = yginfo.getYgbdtimearrival();

            String ygbprovince = yginfo.getYgbprovince();

            String ygbcity = yginfo.getYgbcity();

            String ygbarea = yginfo.getYgbarea();

            String ygbdworkers = yginfo.getYgbdworkers();

            String ygbddays = yginfo.getYgbddays();


            String ygbchattel = yginfo.getYgbchattel(); //接单人 ，电话，  我的发布

            String receivetel = yginfo.getReceivetel();

            latitude = yginfo.getLat();

            longitude = yginfo.getLng();


            if (where) {//我的订单
                if (ygbchattel != null) {
                    phone = ygbchattel;
                }
            } else {//我的发布

                if (receivetel != null) {
                    phone = receivetel;
                }
            }

            tv_peoplenum.setText("用工人数：" + ygbdworkers + "人");
            tv_pedays.setText("用工天数：" + ygbddays + "天");
            tv_myodtphone.setText("手机号：" + phone);
            tv_myodtpaddress.setText("地址：" + ygbprovince + ygbcity + ygbarea + ygbdaddress);
            detail_connect.setText("    " + ygbdremark);
            tv_detaitime.setText("到场时间：" + ygbdtimearrival);
            tv_detaikind.setText("需要工种：" + yginfo.getYgblcname());
            tv_finishdays.setText("完成时间：" + yginfo.getFinishtime());
            String ygbdaddprice = yginfo.getYgbdaddprice();
            String totalcount = yginfo.getTotalcount();
            double bbygbdgamount = Double.parseDouble(totalcount);//总金额
            double aaproce = Double.parseDouble(ygbdaddprice);    //加价价格

//            if (bbygbdgamount -aaproce > 0.1) {

            BigDecimal bd = new BigDecimal(bbygbdgamount - aaproce);
            BigDecimal setScale = bd.setScale(2, bd.ROUND_DOWN);//保留1位小数

            tv_mprice.setText("￥" + (setScale) + "元");

//            }


            if (ygbdaddprice != null) {
                if (ygbdaddprice.equals("0.00")) {
                    tv_mpricegd.setText("");
                } else {
                    tv_mpricegd.setText("+" + ygbdaddprice + "元");//加价
                }
            }


//            String ygbdgmode = yginfo.getYgbdmode();//付款方式：1：支付宝 2：微信 3：余额

            tv_mode.setText("支付方式：" + yginfo.getStrygbdmode());
            tv_mbprice.setText("￥" + yginfo.getYgbpayamount() + "元");

            String ygbotype = yginfo.getYgbotype();
            ygbotypem = ygbotype;
            detal_state.setText("状态：" + yginfo.getOrderstate());


            String ygblcname = yginfo.getInitiatename();
            String receivename = yginfo.getReceivename();


            if (ygbotype != null) {//状态：0 进行中 1 已完成 2 已取消 3：施工进行中 8：未开始
                if (ygbotype.equals("8")) {
//                    detal_state.setText("状态：" + "未开始");
                    tv_myodtname.setText("业主：" + "订单未开始");
                    tv_myodtphone.setVisibility(View.INVISIBLE);
                } else {

                    if(mm.equals("5")|| mm.equals("4")){//TODO 我的发布来的用工
                        settitleYg(receivename);
                    }else {
                        settitleYg(ygblcname);
                    }


                }
            }


            pic.clear();

            List<String> images = yginfo.getImages();


            if (images != null) {

                for (int i = 0; i < images.size(); i++) {

                    String s1 = images.get(i);

                    if (s1 != null) {
                        if (!s1.equals("")) {
                            pic.add(s1);
                        }
                    }

                }

                laborAdapter.notifyDataSetChanged();

            }


        }

    }

    private void parSingJJ(String s) {

        Gson gson = new Gson();

        DataJJdetail dataJJdetail = gson.fromJson(s, DataJJdetail.class);

        if (dataJJdetail.getData().getSuccess().equals("1")) {


            JjinfoBean jjinfo = dataJJdetail.getData().getJjinfo();



//            String initiatetel = jjinfo.getInitiatetel();

            String ygbdgtel = jjinfo.getInitiatetel();//getInitiatetel

            String ygbdaddress = jjinfo.getYgbdaddress();
            String ygbdremark = jjinfo.getYgbdgremark();
            String ygbdcreatetime = jjinfo.getYgbdgmounttime();

            String ygbprovince = jjinfo.getYgbprovince();
            String ygbcity = jjinfo.getYgbcity();
            String ygbarea = jjinfo.getYgbarea();

            String ygbchattel = jjinfo.getYgbchattel();

            String receivetel = jjinfo.getReceivetel();

            latitude = jjinfo.getLat();
            longitude = jjinfo.getLng();

            if (where) {//我的订单
                if (ygbchattel != null) {
                    phone = ygbchattel;
                }
            } else {//我的发布

                if (receivetel != null) {
                    phone = receivetel;
                }
            }


            tv_finishdays.setText("完成时间：" + jjinfo.getFinishtime());

//            tv_myodtname.setText("老师：" + ygblcname);

            tv_myodtphone.setText("手机号：" + phone);

            tv_myodtpaddress.setText("地址：" + ygbprovince + ygbcity + ygbarea + ygbdaddress);

            detail_connect.setText("    " + ygbdremark);

            tv_detaitime.setText("开课时间：" + ygbdcreatetime);

            String ygbscname = jjinfo.getYgbscname();

            String ygbdgdays = jjinfo.getYgbdgdays();
            String ygbtime = jjinfo.getYgbtime();

            String totalcount = jjinfo.getTotalcount();

            tv_mprice.setText("￥" + totalcount + "元");

            tv_peoplenum.setText("上课时长：" + ygbtime);

            tv_pedays.setText("上课天数：" + ygbdgdays + "天");

            tv_detaikind.setText("科目：" + ygbscname);

//            String ygbdgmode = jjinfo.getYgbdgmode();//付款方式：1：支付宝 2：微信 3：余额

            tv_mode.setText("支付方式：" + jjinfo.getStrygbdmode());

            tv_mbprice.setText("￥" + jjinfo.getYgbpayamount() + "元");

            String ygbotype = jjinfo.getYgbotype();

            ygbotypem = ygbotype;

            detal_state.setText("状态：" + jjinfo.getOrderstate());



            String ygblcname = jjinfo.getInitiatename();
            String receivename = jjinfo.getReceivename();

            if (ygbotype != null) {//状态：0 进行中 1 已完成 2 已取消 3：施工进行中   8：未开始

                if (ygbotype.equals("8")) {

                    tv_myodtname.setText("家长：" + "订单未开始");
                    tv_myodtphone.setVisibility(View.INVISIBLE);

                }else {

                    if(mm.equals("6")|| mm.equals("3")){//TODO 我的发布来的家教
                        settitleJj(receivename);
                    }else {
                        settitleJj(ygblcname);
                    }

                }
            }


        }


    }

    private void settitleJj(String ygblcname) {

        if (mm.equals("1")) {//TODO 我的订单来的用工
            tv_myodtname.setText("业主："+ ygblcname);
        } else if (mm.equals("2")) {//TODO 我的订单来的家教
            tv_myodtname.setText("家长："+ ygblcname);
        } else if (mm.equals("3")) {//TODO 发布订单成功家教
            tv_myodtname.setText("老师："+ ygblcname);
        } else if (mm.equals("4")) {//TODO 发布订单成功用工
            tv_myodtname.setText("师傅："+ ygblcname);
        } else if (mm.equals("5")) {//TODO 我的发布来的用工
            tv_myodtname.setText("师傅："+ ygblcname);
        } else if (mm.equals("6")) { //TODO 我的发布来的家教
            tv_myodtname.setText("老师："+ ygblcname);
        } else if (mm.equals("7")) {//TODO 抢单成功来的师傅
            tv_myodtname.setText("业主："+ ygblcname);
        } else if (mm.equals("8")) {//TODO 抢单成功来的家教
            tv_myodtname.setText("家长："+ ygblcname);
        }

    }

    private void settitleYg(String ygblcname) {

        if (mm.equals("1")) {//TODO 我的订单来的用工
            tv_myodtname.setText("业主："+ ygblcname);
        } else if (mm.equals("2")) {//TODO 我的订单来的家教
            tv_myodtname.setText("家长："+ ygblcname);
        } else if (mm.equals("3")) {//TODO 发布订单成功家教
            tv_myodtname.setText("老师："+ ygblcname);
        } else if (mm.equals("4")) {//TODO 发布订单成功用工
            tv_myodtname.setText("师傅："+ ygblcname);
        } else if (mm.equals("5")) {//TODO 我的发布来的用工
            tv_myodtname.setText("师傅："+ ygblcname);
        } else if (mm.equals("6")) { //TODO 我的发布来的家教
            tv_myodtname.setText("老师："+ ygblcname);
        } else if (mm.equals("7")) {//TODO 抢单成功来的师傅
            tv_myodtname.setText("业主："+ ygblcname);
        } else if (mm.equals("8")) {//TODO 抢单成功来的家教
            tv_myodtname.setText("家长："+ ygblcname);
        }


    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_myorderdetails;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    private void openBaiduMap() {

//        String latitude = (String) SPUtil.get("latitude", "");//纬度
//        String longitude = (String) SPUtil.get("longitude", "");//经度

        String location = latitude + "," + longitude;
        String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息

        try {

            Intent intent = Intent.getIntent("intent://map/marker?location=" + location +
                    "&title=我的位置&content=" + locationDescribe +
                    "&" +
                    "src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

            startActivity(intent); //启动调用

        } catch (Exception e) {

        }

    }

    private void openGaoDeMap() {

//        String location = latitude + "," + longitude;

        String locationDescribe = (String) SPUtil.get("locationDescribe", "");//位置信息

        try {

            Intent intent = Intent.getIntent("androidamap://viewMap?sourceApplication" +
                    "=厦门通&poiname=" +
                    locationDescribe +
                    "&lat=" +
                    latitude +
                    "&lon=" +
                    longitude +
                    "&dev=0");
            startActivity(intent);

        } catch (Exception e) {
            ToastUtil.show(mContext, "请先安装高德地图");
        }
    }

    @OnClick({R.id.lin_back, R.id.tv_top_right, R.id.tv_map, R.id.tv_myodtphone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.tv_myodtphone:

                new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)

                        .setTitleText("拨打电话")

                        .setContentText(phone)

                        .setConfirmText("呼叫")

                        .setCancelText("取消")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.dismissWithAnimation();

                                getReadState(phone);

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })

                        .show();


                break;
            case R.id.tv_map:


                final boolean instalbaidu = isInstallByread(BAIDUMAP);

//                final boolean instalmini = isInstallByread(MINIMAP);
                final boolean instalmini = isAppInstalled(mContext, MINIMAP);



                toast(instalbaidu);


                break;


            case R.id.tv_top_right:


                if (ygbotypem.equals("8")) {
                    ToastUtil.show(mContext, "订单未开始咨询不可用");
                    return;
                }


                if (!phone.equals("")) {

                    Log.e("对方账号phone", "=" + phone);


                    Intent intent = new Intent(mContext, ChatActivity.class);
                    // it's single chat  单聊
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                    intent.putExtra(Constants.EXTRA_USER_ID, "YGB" + phone);//EXTRA_USER_ID
                    startActivity(intent);


                } else {
                    ToastUtil.show(mContext, "未获取到对方手机号咨询不可用");
                }

                break;
        }
    }

    private void toast(final boolean instalbaidu) {

        final List<String> strings = new ArrayList<>();
        strings.add("请选择地图");
        strings.add("百度地图");
        strings.add("高德地图");

        StytledDialog.showBottomItemDialog(mContext, strings, "取消", true, true, new MyItemDialogListener() {
            @Override
            public void onItemClick(String text, int position) {
//               Log.e("onItemClick",""+position);
                if (position == 1) {//百度地图
                    if (instalbaidu) {
                        openBaiduMap();
                    } else {
                        ToastUtil.show(mContext, "请先安装百度地图");
                    }
                } else if (position == 2) {//高德地图
                    openGaoDeMap();
                }
            }
            @Override
            public void onBottomBtnClick() {
            }
        });
    }


    private void getReadState(final String phone) {

        if (PermissionsUtil.hasPermission(mContext, Manifest.permission.CALL_PHONE)) {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mContext.startActivity(intent);

        } else {

            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    mContext.startActivity(intent);

                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {

                }
            }, new String[]{Manifest.permission.CALL_PHONE});
        }

    }

}
