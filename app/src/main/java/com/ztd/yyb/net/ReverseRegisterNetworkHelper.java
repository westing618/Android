package com.ztd.yyb.net;

import android.content.Context;

import org.json.JSONObject;


import com.android.volley.VolleyError;
import com.ztd.yyb.util.Constants;

//{ "prmOut": {"resp_code": "0","resp_desc ": ""},"rtnCode": 0,"err": ""}

public class ReverseRegisterNetworkHelper extends NetworkHelper<JSONObject> {

    public static final String TAG = "ReverseRegisterNetworkHelper";
    public ReverseRegisterNetworkHelper(Context context ) {
        super(context);
    }

    @Override
    protected void disposeVolleyError(VolleyError error) {
        notifyErrorHappened(
                Constants.VOLLEY_ERROR_CODE,
                error == null ? "NULL" : error.getMessage());
    }

    @Override
    protected void disposeResponse(JSONObject response) {
        notifyDataChanged(response);
    }


    @Override
    public void onDataChanged(JSONObject response) {

    }

    @Override
    public void onErrorHappened(String errorCode, String errorMessage) {

    }

    @Override
    public void onResponse(Object response) {

    }
}