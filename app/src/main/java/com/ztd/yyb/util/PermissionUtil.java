package com.ztd.yyb.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/9/1.
 */

public class PermissionUtil {
    public PermissionUtil() {
    }

    public static boolean needToRequestPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static void requestAllPermissions(Activity context, int requestCode) {
//        String[] mPermissionList = new String[]{
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.INTERNET, Manifest.permission.CALL_PHONE
//
//        };
        String[] mPermissionList = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET
        };
        for (int i = 0; i < mPermissionList.length; i++) {
            if (ContextCompat.checkSelfPermission(context, mPermissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context,
                        new String[]{mPermissionList[i]},
                        requestCode);
            }
        }

    }

    public static boolean requesReadExternalPermissions(Activity context, int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDialog(context, Manifest.permission.READ_EXTERNAL_STORAGE, "读取文件需要权限，不开启将无法读取，是否开启该权限？", requestCode);
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean requestWriteSDCardPermissions(Activity context, int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionDialog(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储文件需要权限，不开启将无法存储，是否开启该权限？", requestCode);
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCode);
            }
            return false;
        } else {
            return true;
        }
    }
    private static void showPermissionDialog(final Activity context, final String permission, String msg, final int requestCode) {
        Dialog dialog = new AlertDialog.Builder(context).setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(context,
                        new String[]{permission},
                        requestCode);
            }
        }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    public static void showForbiddenPermissionDialog(final Context context, String msg) {
        Dialog dialog = new AlertDialog.Builder(context).setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", null).create();
        dialog.show();
    }

    public static boolean requestReadSDCardPermissions(Activity context, int requestCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//没有权限
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return false;
        } else {
            return true;
        }
    }
}
