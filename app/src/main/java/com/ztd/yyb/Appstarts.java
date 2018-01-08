package com.ztd.yyb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.base.BaseActivity;
import com.ztd.yyb.util.SPUtil;


/**
 * App入口
 * Created by 曾wt on 2016/1/27.
 */
public class Appstarts extends BaseActivity {
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){

    }
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();

    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();

    }
    @Override
    protected void initViewsAndEvents() {
        toggleWiFi(this, true);
//        registerMessageReceiver();  // used for receive msg
        init();
//        if (PermissionUtil.needToRequestPermission()) {
//            PermissionUtil.requesReadExternalPermissions(Appstarts.this, 100);
//        }
        toLogin();
//        toRecen();
    }


    private void toRecen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFrist()) {
                    showGuide();
                } else {
                    toLogin();
                }


            }
        }, 2000);
    }
    public boolean isFrist() {
        boolean appVerXml = (boolean) SPUtil.get("isFrist", false);
        if (!appVerXml) {
            SPUtil.put("isFrist", true);
            return true;
        }
        return false;

    }
    private void toLogin(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    // 设置打开Wifiw
    private void toggleWiFi(Context context, boolean enabled) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enabled);
    }
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lay_start;
    }


    @Override
    protected void getBundleExtras(Bundle extras) {

    }


    private void showGuide() {
//        setContentView(R.layout.lay_start_viewpager);
//        int guideImg1[] = {
//                R.mipmap.app_help1
//        };
//
//        int guideImg2[] = {
//                R.mipmap.app_help2
//        };
//
//        int guideImg3[] = {
//                R.mipmap.app_help3
//        };
//        ArrayList<int[]> imgLists = new ArrayList<>();
//        imgLists.add(guideImg1);
//        imgLists.add(guideImg2);
//        imgLists.add(guideImg3);
//        final ViewPager vp_GuideInfo = (ViewPager) findViewById(R.id.vp_GuideInfo);
//
//        OnLoginListener onloginListener = new OnLoginListener() {
//
//            @Override
//            public void onLogin() {
//                toLogin();
//            }
//        };
//        findViewById(R.id.bntBreak).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toLogin();
//            }
//        });
//        vp_GuideInfo.setAdapter(new UserGuideAdapter(this, imgLists, onloginListener));

    }
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }

    public interface OnLoginListener {
        public void onLogin();
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
//                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
//                Log.e("MessageReceiver", showMsg.toString());
            }
        }
    }
}
