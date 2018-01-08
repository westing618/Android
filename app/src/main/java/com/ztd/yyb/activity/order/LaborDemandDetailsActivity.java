package com.ztd.yyb.activity.order;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.my.PerfectInformationActicity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.bean.beanOrd.OrdSuFull;
import com.ztd.yyb.bean.beanYgDetail.Ygdetail;
import com.ztd.yyb.bean.beanYgDetail.YginfoBean;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.R.id.lin_top_right;
import static com.ztd.yyb.activity.LoginActivity.USERID;


/**
 * 用工 需求 详情
 * Created by  on 2017/3/15.
 */

public class LaborDemandDetailsActivity extends BaseActivity {

    private final String TENCENT = "com.tencent.mm";//判断 用户是否安装微信客户端
    private final String MOBILEQQ = "com.tencent.mobileqq";//判断 用户是否安装QQ客户端
    private final String WEIBO = "com.sina.weibo";//判断 用户是否安装微博客户端


    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.btn_la_rob_order)
    Button btn_la_rob_order;

    @BindView(lin_top_right)
    LinearLayout lltopRight;
    @BindView(R.id.img_top_right)
    ImageView imgtopRight;

    @BindView(R.id.tv_labtitletime)
    TextView tv_labtitletime;
    @BindView(R.id.tv_labtitlename)
    TextView tv_labtitlename;
    @BindView(R.id.tv_labtitlephone)
    TextView tv_labtitlephone;
    @BindView(R.id.tv_labtitleaddress)
    TextView tv_labtitleaddress;
    @BindView(R.id.tv_labhome_work)
    TextView tv_labhome_work;
    @BindView(R.id.tv_lab_starttime)
    TextView tv_lab_starttime;
    @BindView(R.id.tv_labnum)
    TextView tv_labnum;
    @BindView(R.id.tv_lab_daynum)
    TextView tv_lab_daynum;
    @BindView(R.id.tv_lab_price)
    TextView tv_lab_price;
    @BindView(R.id.tv_lab_totalcount)
    TextView tv_lab_totalcount;

    @BindView(R.id.tv_lab_connect)
    TextView tv_lab_connect;
    @BindView(R.id.tv_imagname)
    TextView tv_imagname;

    @BindView(R.id.image_labtype)
    ImageView image_labtype;

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;


    String ygboid;
    String type;
    ArrayList<String> pic = new ArrayList<String>();
    YginfoBean yginfo;
    LaborAdapter laborAdapter;
    private Map<String, String> mLaborDemandMap = new HashMap<>();
    private Map<String, String> mLaborDetailMap = new HashMap<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_labordemanddetails;
    }

    @Override
    protected void initViewsAndEvents() {

        mTvTitle.setText("用工需求详情");
        lltopRight.setVisibility(View.VISIBLE);
        imgtopRight.setBackground(getResources().getDrawable(R.mipmap.nav_btn_share));

        ygboid = getIntent().getStringExtra("ygboid");
        type = (String) SPUtil.get("flag", "");


        if (type.equals("2")) {
            btn_la_rob_order.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        getDataDetails();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        laborAdapter = new LaborAdapter(this, pic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(laborAdapter);


//        UMShareAPI.get(this).getPlatformInfo(this,share_media, authListener);
    }


    private void getDataDetails() {

        mLaborDetailMap.clear();
        mLaborDetailMap.put("ygbdid", "" + ygboid);
        mLaborDetailMap.put("type", "1");//1 师傅 2家教

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.GETDEMANDDETAIL_URL, null, mLaborDetailMap)
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
                    public void onNext(String s) { //Ygdetail

                        Log.e("onCompleted","="+s);

                        dismissLoadingDialog();

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            Ygdetail ygdetail = gson.fromJson(s, Ygdetail.class);

                            if (ygdetail.getResult().equals("true")) {
                                yginfo = ygdetail.getData().getYginfo();
                                String ygbdaddress = yginfo.getYgbdaddress();
                                String ygbdcreatetime = yginfo.getYgbdcreatetime();
                                String ygbdtimearrival = yginfo.getYgbdtimearrival();
                                double ygbddays = yginfo.getYgbddays();
                                String initiatetel = yginfo.getInitiatetel();
                                double totalcount = yginfo.getTotalcount();
                                String ygblcprice = yginfo.getYgblcprice();
                                String initiatename = yginfo.getInitiatename();
                                String ygbdremark = yginfo.getYgbdremark();
                                double ygbdworkers = yginfo.getYgbdworkers();
                                String ygblcname = yginfo.getYgblcname();
                                String ygbdprovince = yginfo.getYgbdprovince();
                                String ygbdcity = yginfo.getYgbdcity();
                                String ygbdarea = yginfo.getYgbdarea();
                                tv_lab_connect.setText(ygbdremark);
                                String ygbdtype = yginfo.getYgbdtype();//1：普通订单 2：抢修订单
                                if (ygbdtype != null) {//ygbdtype
                                    if (ygbdtype.equals("1")) {
                                        image_labtype.setImageResource(R.mipmap.list_icon_putong);
                                    } else {
                                        image_labtype.setImageResource(R.mipmap.list_icon_qiangxiu);
                                    }
                                    tv_labtitletime.setText("发布时间：" + ygbdcreatetime);
                                    tv_labtitlename.setText("业主姓名：" + initiatename);
//                                tv_labtitlephone.setText("    手机号：" + initiatetel);
                                    tv_labtitleaddress.setText("" + ygbdprovince + ygbdcity + ygbdarea + ygbdaddress);
                                    tv_labhome_work.setText("需要工种：" + ygblcname);
                                    tv_lab_starttime.setText("到场时间：" + ygbdtimearrival);
                                    tv_labnum.setText("用工人数：" + ygbdworkers + "人");
                                    tv_lab_daynum.setText("用工天数：" + ygbddays + "天");
                                    tv_lab_price.setText("价格：￥" + ygblcprice + "/天");
                                    tv_lab_totalcount.setText("(共计：￥" + totalcount + "元)");
                                    String ygbimage = yginfo.getYgbimage();
                                    if (!ygbimage.equals("")) {
                                        tv_imagname.setVisibility(View.VISIBLE);
                                        String[] aa = ygbimage.split("&");
                                        pic.clear();
                                        for (int i = 0; i < aa.length; i++) {
                                            pic.add(aa[i]);
                                        }
                                        laborAdapter.notifyDataSetChanged();
                                    }else {
                                        tv_imagname.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }
                });

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    @OnClick({R.id.lin_back, R.id.btn_la_rob_order, R.id.lin_top_right})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_top_right:

                install();

                share();


                break;


            case R.id.btn_la_rob_order:

                String userid = (String) SPUtil.get(USERID, "");

                String orderid = yginfo.getYgboid();
//
                robOrder(orderid, userid);

//

                break;
        }
    }

    private void robOrder(final String ygbdid, String userid) {


        mLaborDemandMap.clear();
        mLaborDemandMap.put("userid", userid); //抢单用户id
        mLaborDemandMap.put("orderid", ygbdid); //需求id
        mLaborDemandMap.put("type", "1");   // 用工类型 1 师傅 2家教

//        Log.e("orderid1","="+ygbdid);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.ROB_ORDER_URL, null, mLaborDemandMap)
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

                            Gson gson = new Gson();

                            OrdSuFull ordSuFull = gson.fromJson(s, OrdSuFull.class);

                            if (ordSuFull.getData().getSuccess().equals("1")) {

                                SPUtil.put("putorderid", "" + ygbdid);
                                SPUtil.put("puttype", "" + "1");
                                startActivity(new Intent(mContext, GrabOrderSuccessActivity.class));


                                DemanEvet messageEvent = new DemanEvet();
                                messageEvent.setMsg("paysucc");
                                EventBus.getDefault().post(messageEvent);

                            } else if (ordSuFull.getData().getSuccess().equals("0")) {//ygbmcstate：状态：0审核中   1认证通过   2认证未通过   3未认证

                                String ygbmcstate = ordSuFull.getData().getYgbmcstate();
                                String msg = "";

                                if (ygbmcstate.equals("0")) {
                                    msg = "证书审核中请等待";
                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText(msg)
//                                            .setContentText(orderSu.getMsg())
                                            .show();

                                } else if (ygbmcstate.equals("3")) {

                                    msg = "资料未认证";

                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText(msg)
                                            .setContentText("马上去认证!")
                                            .setConfirmText("确定!")//确定
                                            .setCancelText("取消!")//取消
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {

                                                    sDialog.dismissWithAnimation();

                                                    Intent intent = new Intent(mContext, PerfectInformationActicity.class);

//                                                    if (type.equals("1")) {
                                                    intent.putExtra("FLAG", "2");
//                                                    } else {
//                                                        intent.putExtra("FLAG", "4");
//                                                    }

                                                    startActivity(intent);

                                                }
                                            })
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
//                                                    Log.e("取消","取消");
                                                }
                                            })
                                            .show();


                                } else {

                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("提示")
                                            .setContentText(ordSuFull.getMsg())
                                            .show();

                                }


                            }


                        }
//
                    }

                });


    }

    private void install() {

        boolean installten = isInstallByread(TENCENT);

        boolean installmob = isInstallByread(MOBILEQQ);

        boolean installwei = isInstallByread(WEIBO);

//        Log.e("installten","="+installten);
//        Log.e("installmob","="+installmob);
//        Log.e("installwei","="+installwei);

        if(!installten){
            Toast.makeText(mContext,"您还未安装微信,可能会分享失败",Toast.LENGTH_SHORT).show();
        }
        if(!installmob){
            Toast.makeText(mContext,"您还未安装QQ,可能会分享失败",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
    private void share() {


        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ
                };
        UMImage image = new UMImage(mContext,
                BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        String url="http://a.app.qq.com/o/simple.jsp?pkgname=com.ztd.yyb";
        UMWeb web = new UMWeb(url);
        web.setTitle("用工贝");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("一款集装修、置物、家教为一体的APP软件");//描述

        new ShareAction(this).setDisplayList(displaylist)
                .withText("用工贝应用")
                .withSubject("用工贝")
//                .withTargetUrl("http://www.hrk.com.cn")
                .withMedia(web)
                .setListenerList(umShareListener)
                .open();

    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.e("plat", "platform" + platform);

            Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, " 分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.e("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {


        }
    };

}
