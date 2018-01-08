package com.ztd.yyb.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.yanzhenjie.nohttp.NoHttp;
import com.ztd.yyb.imagepicker.PicassoImageLoader;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 曾wt on 2016/1/25.
 */
public class BaseApplication extends Application {
    public static Context context;
    public static int versionCode = 1;
    public static String versionName = "1.1.4";
    public static boolean isInfo = false;
    public static boolean isRefreshCode = true;

    // 记录是否已经初始化
    private boolean isInit = false;

    {

        PlatformConfig.setWeixin("wx27de7d2ab7c7bff2", "b1a626388b5295faa6e1c4e963f05691");
        PlatformConfig.setQQZone("1106012576", "fFSBKrH9noSt9t7u");
        PlatformConfig.setSinaWeibo("505031379", "b45f210c3b9fa5d20d79dcb447bc000e", "http://sns.whalecloud.com");

    }

    public static synchronized BaseApplication getContext() {
        return (BaseApplication) context;
    }

    public static boolean checkNet() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
//            Log.e("Net Error", e.toString());
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush


        context = this.getApplicationContext();
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        // 初始化环信SDK
        initEasemob();

        Fresco.initialize(this);//图片加载

        setPicker();

        NoHttp.initialize(this);//直接初始化后，一切采用默认设置。

        Config.DEBUG = false;
        UMShareAPI.get(this);//友盟分享


        CrashReport.initCrashReport(getApplicationContext(), "de621dd6c2", false);//Bugly会为自动检测环境并完成配置 （输出详细的Bugly SDK的Log建议在测试阶段建议设置成true，发布时设置为false）


        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(false);
        config.isOpenShareEditActivity(false);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        config.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
        config.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);




    }

    private void setPicker() {//TODO 初始化 ImagePicker

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(3);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(1000);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(1000);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(100);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(100);//保存文件的高度。单位像素


    }

    private void initEasemob() {

        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }

        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(context, initOptions());

        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        EaseUI.getInstance().init(this, null);

        // 设置初始化已经完成
        isInit = true;
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
//        options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);

        // 设置集成小米推送的appid和appkey

//        options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
//
//        options.setHuaweiPushAppId("10492024");

        return options;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    public void exitApp() {
        BaseAppManager.getInstance().clear();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

}
