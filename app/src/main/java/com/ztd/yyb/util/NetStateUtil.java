
package com.ztd.yyb.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接状态
 *
 * @author 作者: 王勋
 * @version 创建时间：2013-7-6 下午03:46:52 类说明 :
 */
public class NetStateUtil {

    private static final String TAG = "NetStateUtil";

    /**
     * 无连接
     */
    public static final int STATE_CONNECT_NONE = 2;

    /**
     * WIFI连接
     */
    public static final int STATE_CONNECT_WIFI = 1;

    /**
     * 移动网络 2G/3G
     */
    public static final int STATE_CONNECT_MOBILE = 0;

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return true 表示正常
     */
    public static boolean isNetworkAvailable(Context context) {
        if(context!=null){
            ConnectivityManager cManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cManager != null) {
                NetworkInfo info = cManager.getActiveNetworkInfo();
                return info != null && info.isAvailable();
            }
        }
            return false;
    }

    /**
     * 检查网络是否可用,没网络显示toast
     *
     * @param context
     * @return true 表示正常
     */
    public static boolean isNetworkAvailableShowToast(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        boolean state = info != null && info.isAvailable();
        if (state) {
            return true;
        } else {
//            ToastView.showToastShort(R.string.error_nonet_again);
            return false;
        }
    }

    /**
     * 返回网络的连接状态
     *
     * @param context
     * @return
     */
    public static int getNetConnectState(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
            return STATE_CONNECT_NONE;
        } else if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
            return ConnectivityManager.TYPE_WIFI;
        } else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
            return ConnectivityManager.TYPE_MOBILE;
        } else {
            return STATE_CONNECT_NONE;
        }
        // return networkInfo.getType();
    }

    /**
     * 联网是否是GPRS??true表示GPRS连接
     *
     * @param context
     * @return
     */
    public static boolean isNetConnectGPRS(Context context) {
        return getNetConnectState(context) == ConnectivityManager.TYPE_MOBILE;
    }
}
