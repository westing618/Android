package com.ztd.yyb.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.umeng.socialize.UMShareAPI;
import com.ztd.yyb.R;
import com.ztd.yyb.adapter.VPFragmentAdapter;
import com.ztd.yyb.base.BaseAppManager;
import com.ztd.yyb.base.BaseApplication;
import com.ztd.yyb.evenbean.DemanEvet;
import com.ztd.yyb.presenter.MainView;
import com.ztd.yyb.presenter.impl.MainPresenterImpl;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.view.MyQViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

//import com.umeng.socialize.UMShareAPI;

/**
 * 主界面
 * Created by 曾wt on 2016/1/26.
 */
public class MainActivity extends FragmentActivity implements MainView {
    public static MyQViewPager mViewpager;
    public ArrayList<View> mTabViewList = new ArrayList<>();
    @BindView(R.id.bottomview)
    LinearLayout mBottomview;
    //    private static long DOUBLE_CLICK_TIME = 0L;
    private String titles[] = {"首页", "需求", "消息", "我的"};
    private BaseApplication mBaseApp;
    private MainPresenterImpl mMainPresenter;
    //    private final int REQUEST_READ_SDCARD = 200, REQUEST_WRITE_SDCARD = 300;

    private android.app.AlertDialog.Builder exceptionBuilder;
    private ViewGroup emptyViewGroup = null;
    private int[] mTitlebar_PhoneImage = {R.drawable.main_menu1, R.drawable.main_menu2, R.drawable.main_menu3, R.drawable.main_menu4};

    ImageView mImg_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        overridePendingTransition(R.anim.right_in, R.anim.right_out);
//        setTranslucentStatus(true);
        BaseAppManager.getInstance().addActivity(this);
//        setSystemBarTintDrawable(getResources().getDrawable(R.color.themecolor));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        EventBus.getDefault().register(this);

        mBaseApp = (BaseApplication) getApplication();

        initUI();

        String isBackground = getIntent().getStringExtra("isBackground");
        if(isBackground!=null){
            if(isBackground.equals("isBackground")){
                if(isForeground(MainActivity.this)){
                    showExceptionDialog();
                }
            }
        }

        String myReceiver = getIntent().getStringExtra("MyReceiver");
            if(myReceiver!=null){
                if("MyReceiver".equals(myReceiver)){

                }
            }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void toAddtBus(String message) {
//        Log.e("MyReceiver", "[MyReceiver] 接收到推送下来的通知"+message);
//
//
//    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        //注册一个监听连接状态的listener
//        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //    onResume

    private void initUI() {

        mViewpager = (MyQViewPager) findViewById(R.id.viewpager);

        mMainPresenter = new MainPresenterImpl(this, this);
        mMainPresenter.initialized();
        addTabView();
        mViewpager.setNoScroll(true);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mTabViewList.size(); i++) {
                    if (position == i) {
                        mTabViewList.get(i).setSelected(true);
                    } else {
                        mTabViewList.get(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


//        String myReceiver = getIntent().getStringExtra("MyReceiver");
//        if( myReceiver!=null&& !myReceiver.equals("")){
//            mViewpager.setCurrentItem(2);
//        }

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
//        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    private void showExceptionDialog() {

        String st = getResources().getString(R.string.Logoff_notification);
//        if (!MainActivity.this.isFinishing()) {
        // clear up global variables
        try {
            if (exceptionBuilder == null)
                exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
            exceptionBuilder.setTitle(st);
            exceptionBuilder.setMessage("同一帐号已在其他设备登录");
            exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    exceptionBuilder = null;

                    finish();

                    SPUtil.put("IS_LOGIN", "0");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            exceptionBuilder.setCancelable(false);
            exceptionBuilder.create().show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initializeViews(List<Fragment> fragments) {
        if (null != fragments && !fragments.isEmpty()) {
            mViewpager.setOffscreenPageLimit(fragments.size());
            mViewpager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));

        }
    }

    private void addTabView() {
        mTabViewList.clear();
        for (int i = 0; i < mTitlebar_PhoneImage.length; i++) {
            View layview = getLayoutInflater().inflate(
                    R.layout.lay_tabitem, emptyViewGroup, false);
            ImageView imgs = (ImageView) layview.findViewById(R.id.imgs);

             mImg_point = (ImageView) layview.findViewById(R.id.img_point);

            TextView tv_title = (TextView) layview.findViewById(R.id.tv_title);

            tv_title.setText(titles[i]);

            imgs.setImageResource(mTitlebar_PhoneImage[i]);

            imgs.setScaleType(ImageView.ScaleType.FIT_CENTER);

            layview.setTag(mImg_point);

            mImg_point.setVisibility(View.GONE);

            mTabViewList.add(layview);
            layview.setOnClickListener(new BottomOnClick(i));
            mBottomview.addView(layview, new LinearLayout.LayoutParams(0,MATCH_PARENT, 1));

            if (i == 0) {
                layview.setSelected(true);
            }else if(i == 2){
//                mImg_point.setVisibility(View.VISIBLE);
            }
//            setvi(mImg_point,0);
        }
    }

    private void setvi(ImageView mImg_point, int i) {
        if(i==2){
            mImg_point.setVisibility(View.VISIBLE);
        }
    }


    private void addTabViewt() {
        mTabViewList.clear();
        for (int i = 0; i < mTitlebar_PhoneImage.length; i++) {
            View layview = getLayoutInflater().inflate(
                    R.layout.lay_tabitem, emptyViewGroup, false);
            ImageView imgs = (ImageView) layview.findViewById(R.id.imgs);

            ImageView mImg_point = (ImageView) layview.findViewById(R.id.img_point);

            TextView tv_title = (TextView) layview.findViewById(R.id.tv_title);

            tv_title.setText(titles[i]);

            imgs.setImageResource(mTitlebar_PhoneImage[i]);

            imgs.setScaleType(ImageView.ScaleType.FIT_CENTER);

            layview.setTag(mImg_point);

            mImg_point.setVisibility(View.GONE);
            mTabViewList.add(layview);
            layview.setOnClickListener(new BottomOnClick(i));
            mBottomview.addView(layview, new LinearLayout.LayoutParams(0,MATCH_PARENT, 1));

            if (i == 0) {
                layview.setSelected(true);
            }else if(i == 2){
                mImg_point.setVisibility(View.VISIBLE);
            }
        }
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {

//                        Log.e("显示帐号在其他设备登录","显示帐号在其他设备登录");
                        if(isForeground(MainActivity.this)){
                            showExceptionDialog();
                        }


                    } else {

                    }
                }
            });
        }
    }

    private class BottomOnClick implements View.OnClickListener {

        private int position;

        public BottomOnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ImageView mImg_point = (ImageView) v.findViewById(R.id.img_point);
//            mTvTitle.setText(titles[position]);
            for (int i = 0; i < mTabViewList.size(); i++) {
                if (position == i) {
                    mTabViewList.get(i).setSelected(true);
                } else {
                    mTabViewList.get(i).setSelected(false);
                }
            }
            mViewpager.setCurrentItem(position);

            if (position == 1) {//用户点击需求 ，刷新数据
                DemanEvet messageEvent = new DemanEvet();
                messageEvent.setMsg("Deman");
                EventBus.getDefault().post(messageEvent);
            }else if(position == 2){//点击消息  隐藏小红点
//                mImg_point.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MainActivity.this).onActivityResult(requestCode, resultCode, data);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
////            Log.e("onKeyDown", "返回时间System.currentTimeMillis() - DOUBLE_CLICK_TIME=" + (System.currentTimeMillis() - DOUBLE_CLICK_TIME));
//            if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
//                ToastUtil.show(MainActivity.this, "再按一次退出程序");
//                DOUBLE_CLICK_TIME = System.currentTimeMillis();
//                mViewpager.setCurrentItem(0);
//
//            } else {
//                mBaseApp.exitApp();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 判断某个界面是否在前台
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public  boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public  boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            if( isForeground(MainActivity.this)){

                for (int i = 0; i < messages.size(); i++) {

                    EMMessageBody body = messages.get(i).getBody();
                    String s = body.toString();
                    String substring = s.substring(4);

                    showNotification(""+substring);

                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    //弹出Notification
    private void showNotification(String msg) {

        int icon = R.mipmap.logo;

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icon)
//                .setTicker("Hearty365")
                .setContentTitle("您有新的聊天消息")
                .setContentText(msg)
//                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
//                .setContentInfo("Info");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

//        if(isForeground(this)){
            notificationManager.notify(1, builder.build());
//        }


    }
}
