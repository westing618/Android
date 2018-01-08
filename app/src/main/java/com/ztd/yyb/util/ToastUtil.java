package com.ztd.yyb.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ztd.yyb.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Toast用于在手机屏幕上向用户显示一条信息，一段时间后自动消失
 * Created by chenjh on 2015/10/21.
 */
public class ToastUtil {
    private static Toast toast = null;
    /**
     * 显示Toast
     *
     * @param ctx
     * @param msg
     */
    public static void show(Context ctx, String msg) {
        show(ctx, msg, Toast.LENGTH_SHORT, false);
    }

    /**
     * 显示Toast
     *
     * @param ctx
     * @param msg
     */
    public static void show(Context ctx, String msg,int duration) {
        show(ctx, msg, duration, false);
    }

    /**
     * 显示Toast
     *
     * @param ctx
     * @param msgId
     */
    public static void show(Context ctx, int msgId,int duration) {
        show(ctx, msgId, duration, false);
    }

    /**
     * 可设置居中显示Toast
     *
     * @param ctx
     * @param msg
     * @param isCenter
     */
    public static void show(Context ctx, String msg,int duration, boolean isCenter) {
        if(toast == null){
            toast = Toast.makeText(ctx,
                    msg, duration);
        }else{
            toast.setText(msg);
            toast.setDuration(duration);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }


    /**
     * 可设置居中显示Toast
     *
     * @param ctx
     * @param msgId
     * @param isCenter
     */
    public static void show(Context ctx, int msgId,int duration, boolean isCenter) {
        if(toast == null){
            toast = Toast.makeText(ctx,
                    msgId, duration);
        }else{
            toast.setText(msgId);
            toast.setDuration(duration);
        }
        if (isCenter) {
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        toast.show();
    }

    /**
     * 带默认图片的Toast
     *
     * @param ctx
     * @param msg
     */
    public static void showDrawable(Context ctx, String msg) {
        showDrawable(ctx, msg, R.mipmap.ic_launcher);
    }

    /**
     * 带图片的Toast
     *
     * @param ctx
     * @param msg
     * @param drawableId
     */
    public static void showDrawable(Context ctx, String msg, int drawableId) {
        if(toast == null){
            toast = Toast.makeText(ctx,
                    msg, Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(ctx);
        imageCodeProject.setImageResource(drawableId);
        toastView.addView(imageCodeProject, 0);
        toast.show();
    }
    /**
     * 关闭Toast
     */
    public static void closeToast(){
//        if(toast==null){
//            return;
//        }
//        Object obj=null;
//        try
//        {
//            //通过反射技术，从toast对象中获取mTN对象
//            Field field = toast.getClass().getDeclaredField("mTN");
//            field.setAccessible(true);
//            obj = field.get(toast);
//            Method method = obj.getClass().getDeclaredMethod("hide",  null);
//            method.invoke(obj, null);
//        }
//        catch (Exception e)
//        {
//
//        }
    }
}
