package com.ztd.yyb.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.HomeActivity;
import com.ztd.yyb.activity.my.InvoiceActivity;
import com.ztd.yyb.activity.my.MyAcceptEvaluationActivity;
import com.ztd.yyb.activity.my.MyCouponActivity;
import com.ztd.yyb.activity.my.MyEvaluationActivity;
import com.ztd.yyb.activity.my.MyOrderTwoActivity;
import com.ztd.yyb.activity.my.MyReleaseActivity;
import com.ztd.yyb.activity.my.MyWalletActivity;
import com.ztd.yyb.activity.my.SettingActivity;
import com.ztd.yyb.bean.beanLnfo.DataInfo;
import com.ztd.yyb.bean.beanLnfo.OrderinfoBean;
import com.ztd.yyb.bean.beanLnfo.UserinfoBean;
import com.ztd.yyb.evenbean.PerfEvent;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.GlideCircleTransform;
import com.ztd.yyb.util.OkHttp3Utils;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ztd.yyb.activity.LoginActivity.USERID;

//import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareConfig;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;

/**
 * 我的
 * Created by 曾wt on 2016/1/27.
 */
public class MyFragment extends Fragment {

    private final String TENCENT = "com.tencent.mm";//判断 用户是否安装微信客户端
    private final String MOBILEQQ = "com.tencent.mobileqq";//判断 用户是否安装QQ客户端
    private final String WEIBO = "com.sina.weibo";//判断 用户是否安装微博客户端

    @BindView(R.id.ll_share)
    LinearLayout mLl_share;

    @BindView(R.id.ll_about)
    LinearLayout ll_about;

    @BindView(R.id.lin_mys)
    LinearLayout lin_mys;

    @BindView(R.id.ll_myrelea)
    LinearLayout ll_myrelea;

    @BindView(R.id.ll_wallet)
    LinearLayout ll_wallet;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_userphone)
    TextView tv_userphone;

    @BindView(R.id.tv_a_money)
    TextView tv_a_star;
    @BindView(R.id.tv_b_money)
    TextView tv_b_order;

    @BindView(R.id.img_user)
    ImageView img_user;

    @BindView(R.id.img_point)
    ImageView img_point;

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),  "分享成功", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), " 分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
//                Log.e("throw", "throw:" + t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };
    private Map<String, String> mMyinfoMap = new HashMap<>();
    protected void initViewsAndEvents() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);}

        String message = (String) SPUtil.get("message", "");
        if("".equals(message)){
            img_point.setVisibility(View.INVISIBLE);
        }else {
            try {
                Gson gson=new Gson();
                JsonRootBean jsonRootBean = gson.fromJson(message, JsonRootBean.class);
                String isAccount = jsonRootBean.getIsAccount();
                if("1".equals(isAccount)){
                    img_point.setVisibility(View.VISIBLE);
                }else {
                    img_point.setVisibility(View.INVISIBLE);
                }
            }catch (Exception e){ }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(PerfEvent perageEvent) {
        String msg = perageEvent.getMsg();
        if (msg.equals("true")) {
            getUserData();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAddtBus(String message) {// {"isAccount":"1"} 红包提醒
//        Log.e("toAddtBus", "[toAddtBus] 接收到推送下来的通知"+message);
//        SPUtil.put("message",message);
        try {
            Gson gson=new Gson();
            JsonRootBean jsonRootBean = gson.fromJson(message, JsonRootBean.class);
            String isAccount = jsonRootBean.getIsAccount();
            if("1".equals(isAccount)){
                img_point.setVisibility(View.VISIBLE);
            }else {
                img_point.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){ }
    }
    public class JsonRootBean {
        private String isAccount;
        public void setIsAccount(String isAccount) {
            this.isAccount = isAccount;
        }
        public String getIsAccount() {
            return isAccount;
        }

    }

    private void getUserData() {

        mMyinfoMap.clear();

        String userid = (String) SPUtil.get(USERID, "");
//        Log.e("userid","="+userid);
        mMyinfoMap.put("userid", userid);

        OkHttp3Utils.getInstance().doPost(Constants.GRTINFO_URL, null, mMyinfoMap)
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

//                        Log.e("onNext", "=" + s);

                        if (!TextUtils.isEmpty(s)) {

                            Gson gson = new Gson();

                            DataInfo dataInfo = gson.fromJson(s, DataInfo.class);
                            if (dataInfo.getData().getSuccess().equals("1")) {

                                OrderinfoBean orderinfo = dataInfo.getData().getOrderinfo();

                                String order = orderinfo.getOrder();

                                String star = orderinfo.getStar();

                                tv_a_star.setText(star);

                                tv_b_order.setText(order);

                                UserinfoBean userinfo = dataInfo.getData().getUserinfo();
                                String ygbmname = userinfo.getYgbmname();
                                String ygbmtel = userinfo.getYgbmtel();
                                String ygbmlogo = userinfo.getYgbmlogo();
                                tv_userphone.setText(ygbmtel);

                                if (ygbmname.equals("")) {
                                    tv_username.setText("- - -");
                                } else {
                                    tv_username.setText(ygbmname);
                                    SPUtil.put("ygbmname", ygbmname);
                                }


                                String test = Constants.BASE_URL + "logo/" + ygbmlogo;

//                                PicassoUtil.getInstance().getPicasso().with(getActivity())
//                                        .load(test)
//                                        .error(R.mipmap.list_icon_user)
//                                        .placeholder(R.mipmap.list_icon_user)
//                                        .networkPolicy(NetworkPolicy.NO_CACHE)
//                                        .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop()
//                                        .fit()
//                                        .into(img_user);

                                Glide.with(getActivity()).load(test).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.mipmap.list_icon_user)
                                        .placeholder(R.mipmap.list_icon_user)
                                        .transform(new GlideCircleTransform(getActivity())).into(img_user);

                            }
                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();


    }



    protected int getContentViewLayoutID() {
        return R.layout.lay_my;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(getContentViewLayoutID(), null);
        ButterKnife.bind(this, rootView);
        getUserData();
        initViewsAndEvents();
        return rootView;
    }

    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
    }

    @OnClick({R.id.ll_share, R.id.ll_about, R.id.lin_mys,
            R.id.ll_myrelea, R.id.ll_wallet, R.id.img_user,
            R.id.imga_setting, R.id.ll_myevaluation,
            R.id.ll_invoice, R.id.ll_coupons, R.id.ll_evaluation,
            R.id.imga_phone, R.id.lin_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share:

                install();

                share();

                break;

            case R.id.lin_back:
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;

            case R.id.imga_phone:

                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("拨打电话")
                        .setContentText("05925501192")
                        .setConfirmText("呼叫")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getReadState();
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

            case R.id.ll_evaluation:
                startActivity(new Intent(getActivity(), MyAcceptEvaluationActivity.class));
                break;
            case R.id.ll_coupons:
                startActivity(new Intent(getActivity(), MyCouponActivity.class));
                break;
            case R.id.ll_invoice:
                startActivity(new Intent(getActivity(), InvoiceActivity.class));
                break;
            case R.id.ll_myevaluation:
                startActivity(new Intent(getActivity(), MyEvaluationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(getActivity(), MyOrderTwoActivity.class));
                break;
            case R.id.ll_myrelea:
                startActivity(new Intent(getActivity(), MyReleaseActivity.class));
                break;
            case R.id.ll_wallet:
                SPUtil.put("message","");
                img_point.setVisibility(View.INVISIBLE);
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
                break;
            case R.id.imga_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
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
            Toast.makeText(getActivity(),"您还未安装微信,可能会分享失败",Toast.LENGTH_SHORT).show();
        }
        if(!installmob){
            Toast.makeText(getActivity(),"您还未安装QQ,可能会分享失败",Toast.LENGTH_SHORT).show();
        }
//        if(!installwei){
//            Toast.makeText(getActivity(),"您还未安装微博,可能会分享失败",Toast.LENGTH_SHORT).show();
//        }

    }

//TODO 拨打电话

    private void getReadState() {

        if (PermissionsUtil.hasPermission(getActivity(), Manifest.permission.CALL_PHONE)) {

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "05925501192"));
            startActivity(intent);

        } else {

            PermissionsUtil.requestPermission(getActivity(), new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "05925501192"));
                    startActivity(intent);

                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {

                }
            }, new String[]{Manifest.permission.CALL_PHONE});
        }

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

        UMImage image = new UMImage(getActivity(),
                BitmapFactory.decodeResource(getResources(), R.mipmap.logo));
        String url = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ztd.yyb";
        UMWeb web = new UMWeb(url);
        web.setTitle("用工贝");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("一款集装修、置物、家教为一体的APP软件");//描述
        new ShareAction(getActivity()).setDisplayList(displaylist)
                .withText("用工贝应用")
                .withSubject("用工贝")
//                .withTargetUrl("http://www.hrk.com.cn")
                .withMedia(web)
                .setListenerList(umShareListener)
                .open();


    }

}
