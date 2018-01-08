package com.ztd.yyb.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/8/25.
 */

public class WexHandler extends Handler {
    private final WeakReference<Context> mActivity;

    public WexHandler(Context mActivity) {
        this.mActivity = new WeakReference<Context>(mActivity);
    }

    public WeakReference<Context> getWeakctivity() {
        return mActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        Context activity = mActivity.get();
        if (activity == null) {
            return;
        }

    }

}
