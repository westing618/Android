package com.ztd.yyb.activity.order;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.ztd.yyb.bean.beanXQDatil.DataXqDetail;
import com.ztd.yyb.bean.beanXQDatil.JjinfoBean;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

//import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;

/**
 * 学堂 需求 详情
 * Created by  on 2017/3/15.
 */

public class SchoolNeedsDetailsActivity extends BaseActivity {

    private final String TENCENT = "com.tencent.mm";//判断 用户是否安装微信客户端
    private final String MOBILEQQ = "com.tencent.mobileqq";//判断 用户是否安装QQ客户端
    private final String WEIBO = "com.sina.weibo";//判断 用户是否安装微博客户端

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lin_top_right)
    LinearLayout lltopRight;
    @BindView(R.id.img_top_right)
    ImageView imgtopRight;

//    @BindView(R.id.tv_schname)  TextView tv_schname;

    @BindView(R.id.tv_schtime)
    TextView tv_schtime;
    @BindView(R.id.tv_parents_name)
    TextView tv_parents_name;
    @BindView(R.id.tv_schphone)
    TextView tv_schphone;
    @BindView(R.id.tv_sch_address)
    TextView tv_sch_address;
    @BindView(R.id.tv_schsubjects)
    TextView tv_schsubjects;
    @BindView(R.id.tv_schgrade)
    TextView tv_schgrade;
    @BindView(R.id.tv_schsex)
    TextView tv_schsex;
    @BindView(R.id.tv_schrecord)
    TextView tv_schrecord;
    @BindView(R.id.tv_sch_price)
    TextView tv_sch_price;
    @BindView(R.id.tv_sch_starttime)
    TextView tv_sch_starttime;
    @BindView(R.id.tv_sch_totalcount)
    TextView tv_sch_totalcount;
    @BindView(R.id.tv_sch_daynum)
    TextView tv_sch_daynum;

    @BindView(R.id.sch_connect)
    TextView sch_connect;

    @BindView(R.id.tv_sch_time)
    TextView tv_sch_time;


    @BindView(R.id.btn_sch_rob_order)
    Button btn_sch_rob_order;
    String ygboid;
    String type;
    JjinfoBean jjinfo;
    private Map<String, String> mLaborSchoolMap = new HashMap<>();
    private Map<String, String> mLaborDetailMap = new HashMap<>();

    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_schoolneedsdetails;
    }

    @Override
    protected void initViewsAndEvents() {
        mTvTitle.setText("家教需求详情");
        lltopRight.setVisibility(View.VISIBLE);
        imgtopRight.setBackground(getResources().getDrawable(R.mipmap.nav_btn_share));

        ygboid = getIntent().getStringExtra("ygboid");
        type = (String) SPUtil.get("flag", "");


        if (type.equals("2")) {
            btn_sch_rob_order.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        getSchDetil();


    }

    private void getSchDetil() {

        mLaborDetailMap.put("ygbdid", "" + ygboid);
        mLaborDetailMap.put("type", "2");//1 师傅 2家教

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
                    public void onNext(String s) {
//                        Log.e("dismissLoadingDialog","="+s);

                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            DataXqDetail dataXqDetail = gson.fromJson(s, DataXqDetail.class);
                            if (dataXqDetail.getData().getSuccess().equals("1")) {
                                jjinfo = dataXqDetail.getData().getJjinfo();
                                String ygbdaddress = jjinfo.getYgbdgaddress();
                                String ygbdgtel = jjinfo.getYgbdgtel();
                                String ygbdgmounttime = jjinfo.getYgbdgmounttime();
                                String ygbdgcreatetime = jjinfo.getYgbdgcreatetime();
                                String ygbdgcontacts = jjinfo.getInitiatename();
                                String ygbscname = jjinfo.getYgbscname();
                                double ygbdgdays = jjinfo.getYgbdgdays();
                                double totalcount = jjinfo.getTotalcount();
                                String ygbdgremark = jjinfo.getYgbdgremark();
                                String ygblcprice = jjinfo.getYgbscprice();
                                String ygbdgmomentname = jjinfo.getYgbdgmomentname();//年级
                                String ygbdgsex = jjinfo.getYgbdgsex();              //性别要求 1:男 0:女
                                String ygbeducationname = jjinfo.getYgbeducationname();
                                String ygbtime = jjinfo.getYgbtime();
                                jjinfo.getYgbdgmounttime();
                                tv_schtime.setText("发布时间：" + ygbdgcreatetime);
                                tv_parents_name.setText("家长姓名：" + ygbdgcontacts);
//                                tv_schphone.setText("   手机号 ：" + ygbdgtel);
                                String ygbdgprovince = jjinfo.getYgbdgprovince();
                                String ygbdgcity = jjinfo.getYgbdgcity();
                                String ygbdgarea = jjinfo.getYgbdgarea();
                                tv_sch_address.setText(ygbdgprovince + ygbdgcity + ygbdgarea + ygbdaddress);
                                tv_schsubjects.setText("        科目：" + ygbscname);
                                tv_schgrade.setText("        年级：" + ygbdgmomentname);
                                if (ygbdgsex != null) {
                                    if (ygbdgsex.equals("0")) {
                                        tv_schsex.setText("性别要求：" + "女");
                                    } else if (ygbdgsex.equals("1")) {
                                        tv_schsex.setText("性别要求：" + "男");
                                    }
                                }
                                tv_schrecord.setText("学历要求：" + "" + ygbeducationname);
                                tv_sch_starttime.setText("开课时间：" + "" + ygbdgmounttime);
                                tv_sch_time.setText("上课时长：" + "" + ygbtime );
                                tv_sch_daynum.setText("上课天数：" + "" + ygbdgdays + "天");
                                sch_connect.setText("" + ygbdgremark);
                                tv_sch_price.setText("价格：￥" + ygblcprice + "/小时");
                                tv_sch_totalcount.setText("(共计：￥" + "" + totalcount + "元)");
                            }else {
                                ToastUtil.show(mContext,"获取数据失败");
                            }

                        }


                    }
                });

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
    @OnClick({R.id.lin_back, R.id.btn_sch_rob_order,R.id.lin_top_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_back:
                finish();
                break;

            case R.id.lin_top_right:




                install();

                share();

                break;
            case R.id.btn_sch_rob_order:

                String ygbdgid = jjinfo.getYgboid();
                String userid = (String) SPUtil.get(USERID, "");
                robSchoolOrder(ygbdgid, userid);

                break;
        }
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

    private void robSchoolOrder(final String ygbdid, String userid) {


        mLaborSchoolMap.clear();
        mLaborSchoolMap.put("userid", userid); //抢单用户id
        mLaborSchoolMap.put("orderid", ygbdid); //需求id
        mLaborSchoolMap.put("type", "2");   //用工类型 1 师傅 2家教

//        Log.e("orderid2","="+ygbdid);

        showLoadingDialog();

        OkHttp3Utils.getInstance().doPost(Constants.ROB_ORDER_URL, null, mLaborSchoolMap)
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
                    public void onNext(String s) {//{"data":{"success":"0"},"code":"异常","msg":"失败"}
                        dismissLoadingDialog();
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            try {
                                OrdSuFull ordSuFull = gson.fromJson(s, OrdSuFull.class);
                                if (ordSuFull.getData().getSuccess().equals("1")) {
                                    SPUtil.put("putorderid",""+ygbdid);
                                    SPUtil.put("puttype",""+"2");
                                    startActivity(new Intent(mContext, GrabOrderSuccessActivity.class));

                                    DemanEvet messageEvent = new DemanEvet();
                                    messageEvent.setMsg("paysucc");
                                    EventBus.getDefault().post(messageEvent);

                                } else if(ordSuFull.getData().getSuccess().equals("0")){//ygbmcstate：状态：0审核中   1认证通过   2认证未通过   3未认证
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
//                                                    Log.e("确定","确定"); 1 师傅 2家教 ==========  2师傅  4老师
                                                        Intent intent = new Intent(mContext, PerfectInformationActicity.class);
                                                        intent.putExtra("FLAG", "4");
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.cancel();
                                                    }
                                                })
                                                .show();

                                    }else {

                                        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("提示")
                                                .setContentText(ordSuFull.getMsg())
                                                .show();

                                    }

                                }


                            }catch (Exception e){

                                ToastUtil.show(mContext,"后台返回数据格式有变化");

                            }
                        }

                    }
                });


    }

    private void share() {

//        PlatformConfig.setWeixin(Constants.WENXIN_APP_ID, Constants.WENXIN_APP_SECRET);
//        //微信 appid appsecret
//        PlatformConfig.setSinaWeibo(Constants.SINA_APP_KEY, Constants.SINA_APP_SECRET, Constants.SINA_REDIRECT_URL);
//        //新浪微博 appkey appsecret
//        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_KEY);

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

//        UMEmoji emoji = new UMEmoji(getActivity(),"http://img5.imgtn.bdimg.com/it/u=2749190246,3857616763&fm=21&gp=0.jpg");
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
            Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.e("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {


        }
    };


}
