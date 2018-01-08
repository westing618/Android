package com.ztd.yyb.util;

import com.android.volley.Response;
import com.ztd.yyb.base.BaseApplication;

import org.json.JSONObject;

/**
 * volley response listener
 * Created by chenjh on 2015/10/13.
 */
public class RespListener implements Response.Listener<JSONObject> {
    private static String TAG = "RespListener";

    @Override
    public void onResponse(JSONObject response) {
        LogUtil.d(TAG,"RespListener onResponse");
        //解析报文
        try {
            if("6".equals(response.getString("rtnCode"))){//授权码过期
                LogUtil.d(TAG, "RespListener onResponse:" + response.toString());
                if(BaseApplication.isRefreshCode) {
                    BaseApplication.isRefreshCode = false;
//                    InterfaceKit.refreshAuthCode(BaseApplication.getContext().getPhoneNo(), BaseApplication.getContext().getAuthorizationCode(), new InterfaceKit.RefreshCallback() {
//                        @Override
//                        public void onReady(JSONObject obj) {
//                            if (obj == null) {
//                                LogUtil.d(TAG, "授权码过期。");
//                                try {
//                                    BaseApplication.isRefreshCode = true;
//                                    BaseApplication.getContext().gotoLoginActivity();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//                                    BaseApplication.getContext().setAuthorizationCode(obj.getString("authorizationCode"));
//                                    BaseApplication.getContext().setEnableDuration(obj.getInt("enableDuration"));
//
//                                    new CountDownTimer(30000, 1000) {
//
//                                        public void onTick(long millisUntilFinished) {
//                                        }
//
//                                        public void onFinish() {
//                                            BaseApplication.isRefreshCode = true;
//                                        }
//
//                                    }.start();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    });
                }
            }
        }catch (Exception e) {
            BaseApplication.isRefreshCode = true;
            e.printStackTrace();
        }
    }
}
