package com.ztd.yyb.util;

import android.os.CountDownTimer;
import android.widget.TextView;



/**
 * Created by Administrator on 2016/8/9.
 */

public class TimeCount extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */

    TextView button;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public TimeCount(long millisInFuture, long countDownInterval, TextView button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        Log.e("TimeCount","onTick");
        button.setEnabled(false);
        button.setText(millisUntilFinished / 1000 + "秒后重新获取");
    }

    @Override
    public void onFinish() {
//        Log.e("TimeCount","onFinish");
//        button.setText(R.string.getcode);
//        button.setEnabled(true);
//        button.setBackgroundResource(R.drawable.bnt_logbg);
    }
}
