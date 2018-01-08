package com.ztd.yyb.Alipay;

/**
 * Created by  on 2017/4/18.
 */

public class AipayBean {


    /**
     * data : {"data":"app_id=2017040606569032&biz_content=%7B%22passback_params%22%3A%22orderId%253D604%2526userId%253D11%2526category%253D0%2526type%253D0%2526ygbcId%253D0%2526price%253D3%22%2C%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%223%22%2C%22subject%22%3A%22%E7%94%A8%E5%B7%A5%E8%B4%9D%E8%AE%A2%E5%8D%95%22%2C%22body%22%3A%22%E7%94%A8%E5%B7%A5%E8%B4%9D%E8%AE%A2%E5%8D%95%22%2C%22out_trade_no%22%3A%226041492495550031%22%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fapi.yogobei.com%2FaliPayController%2Fnotify.do&sign_type=RSA&timestamp=2017-04-18+14%3A05%3A50&version=1.0&sign=Ri3vyP9uKOO3HjBAx3EZN%2BuNdg8glaXwHYsgyls7LQRygRNcF%2BHkXbMN6eaEi8qD%2FKYKaeut%2BtgzmKHuPzhE9O%2FxmIEu7xKhwIRHZo1Hp9%2BDyar5%2BXlYY1eu692hbv%2BjCxYvVkY4pvocD781wwkJUwhsRIjUbGNBEbZd0MNsetM%3D","success":0}
     * code : 0
     * msg : 返回成功!
     */

    private DataBean data;
    private String code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
