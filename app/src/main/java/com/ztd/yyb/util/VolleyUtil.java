package com.ztd.yyb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ztd.yyb.base.BaseApplication;

import org.json.JSONObject;

/**
 * 网络请求
 * Created by chenjh on 2015/9/16.
 */
public class VolleyUtil {
    private static VolleyUtil mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;


    private VolleyUtil(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyUtil(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void postRequest(final String TAG,JSONObject prmIn,Response.Listener<JSONObject> listener,Response.ErrorListener errorListener,int act,String title) {
        LogUtil.d(TAG, "["+title+"]["+act+"]请求数据[prmIn]：" + prmIn.toString());


        if(errorListener == null){
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogUtil.i(TAG, "Error: " + error.getMessage());
                }
            };
        }

        if(!NetStateUtil.isNetworkAvailable(BaseApplication.getContext())){
            LogUtil.i(TAG, "["+title+"]["+act+"]当前网络不可用！");
            ToastUtil.show(BaseApplication.getContext(), "当前网络不可用,请检查网络设置");
            errorListener.onErrorResponse(new VolleyError("当前网络不可用"));
            return;
        }


//        JsonObjectRequest postRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                Constants.BASE_URL,
//                ApiUtil.getRequestParams( prmIn),
//                listener,
//                errorListener);
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.SOCKET_TIMEOUT,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        postRequest.setTag(TAG);
//        VolleyUtil.getInstance(mCtx).addToRequestQueue(postRequest);
    }

}
