package com.ztd.yyb.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ztd.yyb.evenbean.DemanEvet;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by  on 2017/5/9.
 */

public class PollingService extends Service {

    public static final String ACTION = "com.ryantang.service.PollingService";

//    private Notification mNotification;
//    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        new PollingThread().start();
    }


    //弹出Notification
    private void showNotification() {

        DemanEvet messageEvent = new DemanEvet();//通知 需求页面 更新数据
        messageEvent.setMsg("paysuccService");
        EventBus.getDefault().post(messageEvent);

    }

    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {

//            Log.e("Polling","Polling");

                count ++;
            //当计数能被5整除时弹出通知
//            if (count % 5 == 0) {
                showNotification();
//                Log.e("New message","New message");
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
