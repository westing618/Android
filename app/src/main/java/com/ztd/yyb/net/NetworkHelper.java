package com.ztd.yyb.net;

/**
 * Created by chenjh on 2015/9/23.
 */

import android.app.AlertDialog;
import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.ztd.yyb.util.Constants;
import com.ztd.yyb.util.DesUtil;
import com.ztd.yyb.util.LogUtil;
import com.ztd.yyb.util.NetStateUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.util.UIDataListener;
import com.ztd.yyb.util.VolleyUtil;

import org.json.JSONObject;

import java.util.Map;

import dmax.dialog.SpotsDialog;


public abstract class NetworkHelper<T> implements UIDataListener<JSONObject>, ErrorListener
{
    private Context context;
    private int act;
    private boolean isdialogshow;
    public AlertDialog progressDialog;
    private String dialogtitle;
    private static NetworkHelper mInstance;
    public NetworkHelper(Context context)
    {
        this.context = context;
    }

    protected Context getContext()
    {
        return context;
    }


    protected NetworkRequest getRequestForGet(String url, JSONObject params)
    {
        if(params == null)
        {
            return new NetworkRequest(url, this, this);
        }
        else
        {
            return new NetworkRequest(url, params, this, this);
        }
    }

    protected NetworkRequest getRequestForPost(String TAG, JSONObject params)
    {
        LogUtil.d(TAG, "请求数据[prmIn]：" + params.toString());
        if(!NetStateUtil.isNetworkAvailable(context)){
            LogUtil.d(TAG, "当前网络不可用！");
            ToastUtil.show(context, "当前网络不可用,请检查网络设置");
            this.onErrorResponse(new VolleyError("当前网络不可用"));
        }
        return new NetworkRequest(Method.POST,  Constants.BASE_URL,getRequestParams(params), this, this);
    }
    /**
     * 请求参数
     * @param prmIn
     * @return
     */
    public static JSONObject getRequestParams(JSONObject prmIn){
        JSONObject requestJSON = new JSONObject();
        try {
//            requestJSON.put("version", "1");//接口版本
//            requestJSON.put("fromclient", "0");//客户端标识：0=android；1=ios
//            requestJSON.put("client_code", BaseApplication.versionCode);
//            requestJSON.put("client_version", BaseApplication.versionName);
//            requestJSON.put("channelno",BaseApplication.channelno);

            //加密
//            requestJSON.put("prmIn", AesUtil.encryptAndcompress(prmIn.toString()));
            requestJSON.put("prmIn", DesUtil.encryptDES(prmIn.toString(), Constants.DES_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prmIn;
    }
    public void sendGETRequest(String url, JSONObject params  )
    {
        VolleyUtil.getInstance(context).
                getRequestQueue().add(getRequestForGet(url, params));
    }

    public void sendPostRequest(String TAG, JSONObject params,boolean dialogshow,String dialogtitle )
    {
        this.isdialogshow = dialogshow;
        this.dialogtitle = dialogtitle == null ? "" : dialogtitle;
        if(isdialogshow){
            progressDialog = new SpotsDialog(context,dialogtitle);
            progressDialog.show();
        }
        VolleyUtil.getInstance(context).
                getRequestQueue().add(getRequestForPost(TAG, params));
    }
    public MyRequest myRequest;
    public void sendPostMapRequest(String url,int method, Map<String, String> map,UIDataListener<JSONObject> uiDataListener)
    {
        if (!NetStateUtil.isNetworkAvailable(context)) {
            ToastUtil.show(context, "当前网络不可用,请检查网络设置");
            uiDataListener.onErrorHappened("error404", "当前网络不可用");
            return;
        }
         myRequest = new MyRequest(method,url, uiDataListener, this, map,context,dialogtitle);
        VolleyUtil.getInstance(context).
                getRequestQueue().add(myRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        disposeVolleyError(error);
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    protected abstract void disposeVolleyError(VolleyError error);


    protected abstract void disposeResponse(JSONObject response);

    private UIDataListener<T> uiDataListener;

    public void setUiDataListener(UIDataListener<T> uiDataListener)
    {
        this.uiDataListener = uiDataListener;
    }

    protected void notifyDataChanged(T data)
    {
        if(uiDataListener != null)
        {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            uiDataListener.onDataChanged(data);
        }
    }

    protected void notifyErrorHappened(String errorCode, String errorMessage)
    {
        if(uiDataListener != null)
        {
            uiDataListener.onErrorHappened(errorCode, errorMessage );
        }
    }

}
