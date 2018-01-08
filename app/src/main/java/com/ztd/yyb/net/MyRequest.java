package com.ztd.yyb.net;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.ztd.yyb.util.SPUtil;
import com.ztd.yyb.util.ToastUtil;
import com.ztd.yyb.util.UIDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyRequest extends Request<JSONObject> implements DialogInterface.OnDismissListener {

    private String params;
    private int SOCKET_TIMEOUT = 3000;
    private UIDataListener<JSONObject> mListener;
    private Map mMap;
    private Context context;
    public MyRequest(int method, String url, UIDataListener<JSONObject> listener, Response.ErrorListener errorListener, Map map, Context context, String dialogtitle) {
        super(method, url, errorListener);
//        this.sessionId = sessionId;
        mListener = listener;
        mMap = map;
        this.params = url;
        this.context = context;
        if(mMap==null){
            mMap=new  HashMap<String, String>();
        }
        setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        progressDialog = new SpotsDialog(context, dialogtitle);
//        progressDialog.show();
//        progressDialog.setOnDismissListener(this);
    }
//    public SpotsDialog progressDialog;
    public MyRequest(int method, String sessionId, String url, UIDataListener<JSONObject> listener, Response.ErrorListener errorListener, Map map) {
        super(method, url, errorListener);
//        this.sessionId = sessionId;
        mListener = listener;
        mMap = map;
        this.params = url;
        if(mMap==null){
            mMap=new  HashMap<String, String>();
        }
        setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String sessionId= (String) SPUtil.get("sessionid", "");
        if (!TextUtils.isEmpty(sessionId)) {
//            headers.put("JSESSIONID", "" + sessionId);
//            Log.e("getHeaders", "sessionId:" + sessionId);
//            headers.put("cookie", "" + sessionId);
            headers.put("cookie", "" + sessionId);
            Log.e("SSSSSSS", "验证验证码----的sessionId:" + sessionId);
        }


        return headers;

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try
        {
            Map<String, String> responseHeaders = response.headers;
            String rawCookies = responseHeaders.get("Set-Cookie");
            if (rawCookies != null && rawCookies.contains(";")) {

//                SharedPreferencesUtil.put("sessionid", rawCookies.substring(0, rawCookies.indexOf(";")).replace("JSESSIONID=",""));
//                Log.e("parseNetworkResponse","sessionid："+rawCookies.substring(0, rawCookies.indexOf(";")));
//                response.setRequestProperty("cookie", rawCookies.substring(0, rawCookies.indexOf(";")));
            }
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            this.response=jsonObject;
            if(mListener != null)
            {
                mListener.onDataChanged(jsonObject);
            }
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        this.response=response;
//        Log.e("TGttt", "jsonObject:" + response.toString());
        if(response==null){
                return;
        }

    }

    private JSONObject response;
    @Override
    public void onDismiss(DialogInterface dialog) {
        if(response==null){return;}
        try {
            JSONObject resultObject=response.getJSONObject("resultObject");
            String result=resultObject.getString("result");
            String mess=resultObject.getString("mess");
            if("false".equals(result)){
                ToastUtil.show(context, mess, Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}