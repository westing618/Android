package com.ztd.yyb.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.ztd.yyb.R;
import com.ztd.yyb.activity.LoginActivity;
import com.ztd.yyb.activity.MainActivity;
import com.ztd.yyb.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by  on 2017/5/31.
 */

public class MyMessService extends Service {

    public static final String TAG = "MyService";
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            //收到消息
//            if( isForeground(MainActivity.this)){

            for (int i = 0; i < messages.size(); i++) {
                EMMessageBody body = messages.get(i).getBody();
                String s = body.toString();
                String substring = s.substring(4);
                showNotification("" + substring);
            }

            SPUtil.put("messageFragment", "3");//
            SPUtil.put("message", "{}");//
            EventBus.getDefault().post("{}");//显示聊天消息小红点

//            }
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
    private android.app.AlertDialog.Builder exceptionBuilder;

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

        if (isBackground(this)) {
            notificationManager.notify(1, builder.build());
        }


    }

    public boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    System.out.print(String.format("Foreground App:", appProcess.processName));
//                    Log.e("Foreground App", "" + appProcess.processName);
                    return false;
                } else {
//                    System.out.print("Background App:" + appProcess.processName);
//                    Log.e("Background App:", "" + appProcess.processName);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG, "onCreate() executed");
        EMClient.getInstance().chatManager().addMessageListener(msgListener);


        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }

    private void showExceptionDialog() {

        String st = getResources().getString(R.string.Logoff_notification);
//        if (!MainActivity.this.isFinishing()) {
        // clear up global variables
        try {

            if (exceptionBuilder == null)
                exceptionBuilder = new android.app.AlertDialog.Builder(this);
            exceptionBuilder.setTitle(st);
            exceptionBuilder.setMessage("同一帐号已在其他设备登录");
            exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exceptionBuilder = null;

                    SPUtil.put("IS_LOGIN", "0");
                    EMClient.getInstance().logout(true);
                    Intent intent = new Intent(MyMessService.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            exceptionBuilder.setCancelable(false);
//            if(isForeground(this)){
            exceptionBuilder.create().show();
//            }

        } catch (Exception e) {
//            Log.e("onDisconnected", "=0000000");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(TAG, "onDestroy() executed");
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {

//            Log.e("onDisconnected","="+error);

//            this.runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {

//                new Thread(new MyDi()).start();
//                showExceptionDialog();

                if (isBackground(MyMessService.this)) {

//                    SPUtil.put("IS_LOGIN", "0");
//                    EMClient.getInstance().logout(true);
                    Intent intent = new Intent(MyMessService.this, MainActivity.class);
                    intent.putExtra("isBackground", "isBackground");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                }

//                Log.e("显示帐号在其他设备登录","显示帐号在其他设备登录");

            }
//                }
//            });


        }
    }

    private class MyDi implements Runnable {

        @Override
        public void run() {

            showExceptionDialog();

        }
    }
}

