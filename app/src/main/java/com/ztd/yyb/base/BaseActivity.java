package com.ztd.yyb.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.jaeger.library.StatusBarUtil;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.LoginActivity;
import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.SPUtil;

import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

/**
 *
 * Created by 曾wt on 2016/1/25.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Log tag
     */
    protected static String TAG_LOG = null;
    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;
    public BaseApplication baseApp;
    /**
     * context
     */
    protected Context mContext = null;
    public InputMethodManager mInputMM;//软键盘
    public SpotsDialog progressDialog;
    public SharedPreferences mSp ;
    public Editor mEditor ;
    private SweetAlertDialog mLoadingDialog;

    private android.app.AlertDialog.Builder exceptionBuilder;

    private Notification mNotification;
    private NotificationManager mManager;


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

                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {

//                        Log.e("显示帐号在其他设备登录","显示帐号在其他设备登录");

                        showExceptionDialog();

                    } else {

                    }
                }
            });
        }
    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            if( isForeground(BaseActivity.this)){

                for (int i = 0; i < messages.size(); i++) {

//                    Log.e("EMMessageListener","="+messages.get(i).getBody());

                    EMMessageBody body = messages.get(i).getBody();
                    String s = body.toString();
                    String substring = s.substring(4);

//                    Log.e("EMMessageListener","="+substring);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSp= getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        mEditor= mSp.edit();
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);

        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }
        baseApp = getBaseApplication();
        /**标题栏设置可透明*/
        setTranslucentStatus(false);
        mContext = this;
        TAG_LOG = this.getClass().getSimpleName();
        BaseAppManager.getInstance().addActivity(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;

        /**加载布局文件*/
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            StatusBarUtil.setColor(this, R.color.color_reb);//设置状态栏颜色
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        mInputMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressDialog = new SpotsDialog(this ,"加载中，请稍候");
        ButterKnife.bind(this);
        initViewsAndEvents();

        mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.color_reb));
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setTitleText("请稍等...");



        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        initNotifiManager();

    }

    private void initNotifiManager() {


    }
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
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
//                .setContentInfo("Info");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if(isForeground(this)){
            notificationManager.notify(1, builder.build());
        }


    }

    private void showExceptionDialog() {
        String st = getResources().getString(R.string.Logoff_notification);
//        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {

                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(mContext);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage("同一帐号已在其他设备登录");
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;


                        SPUtil.put("IS_LOGIN", "0");
                        EMClient.getInstance().logout(true);

                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();

                    }
                });
                exceptionBuilder.setCancelable(false);

                if(isForeground(this)){
                    exceptionBuilder.create().show();
                }


            } catch (Exception e) {

            }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
       overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
    }

    protected abstract void initViewsAndEvents();

    protected abstract int getContentViewLayoutID();

    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }


    @Override
    protected void onDestroy() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().removeConnectionListener(new MyConnectionListener());

        super.onDestroy();
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册一个监听连接状态的listener
//        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showLoadingDialog() {
        boolean foreground = isForeground(this);//判断某个界面是否在前台
        if (mLoadingDialog != null) {
            if(foreground){
                mLoadingDialog.show();
            }
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {   //点击空白处隐藏软键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean isShouldHideInput(View v,MotionEvent event){
        if(v !=null&&(v instanceof EditText)){
            int[] l ={0,0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if(event.getX()> left &&event.getX()< right
                    &&event.getY()> top &&event.getY()< bottom){
// 点击EditText的事件，忽略它。
                return false;
            }else{
                return true;
            }
        }
// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    private void hideSoftInput(IBinder token){
        if(token !=null){
            InputMethodManager im =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
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
}
