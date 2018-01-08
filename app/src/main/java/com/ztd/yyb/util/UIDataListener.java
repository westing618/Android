package com.ztd.yyb.util;

import com.android.volley.Response;

/**
 * Created by chenjh on 2015/9/23.
 */
public interface UIDataListener<T> extends Response.Listener {
    public void onDataChanged(T response);
    public void onErrorHappened(String errorCode, String errorMessage);
}