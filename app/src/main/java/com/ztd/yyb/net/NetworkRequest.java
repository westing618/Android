package com.ztd.yyb.net;

/**
 * Created by chenjh on 2015/9/23.
 */

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ztd.yyb.util.UIDataListener;

import org.json.JSONObject;

import java.util.Map;

public class NetworkRequest extends JsonObjectRequest
{

    public NetworkRequest(int method, String url,
                          JSONObject postParams, UIDataListener<JSONObject> listener,
                          ErrorListener errorListener)
    {
        super(method, url, postParams, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public NetworkRequest(String url, UIDataListener<JSONObject> listener, ErrorListener errorListener)
    {
        this(Method.GET, url, null, listener, errorListener);
    }
    public NetworkRequest(String url, JSONObject postParams, UIDataListener<JSONObject> listener, ErrorListener errorListener)
    {
        this(Method.GET, url, postParams, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }


}
