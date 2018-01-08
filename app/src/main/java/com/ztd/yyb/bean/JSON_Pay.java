package com.ztd.yyb.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 * 解析JSON的微信支付
 * "msg":"请求成功！",
 "success":true,
 "rows":{
 "sign":"5187c0b1b768f7426e347672001fc629",
 "timestamp":1488517213,
 "package":"Sign=WXPay",
 "noncestr":"394388375fc1e320ea5311ad1b7d2005",
 "partnerid":"1444972102",
 "appid":"wx1a1f64fcdf4c85a8",
 "prepayid":"wx20170303130013aa2a7caefd0776373688"},
 "code
 */
public class JSON_Pay implements Serializable {
    @JSONField(name="sign")
    private String sign="";
    @JSONField(name="timestamp")
    private String timestamp;
    @JSONField(name="package")
    private String packages="";
    @JSONField(name="noncestr")
    private String noncestr="";
    @JSONField(name="partnerid")
    private String partnerid="";
    @JSONField(name="appid")
    private String appid="";
    @JSONField(name="prepayid")
    private String prepayid="";

    public JSON_Pay() {
    }

    public JSON_Pay(String sign, String timestamp, String packages, String noncestr, String partnerid, String appid, String prepayid) {
        this.sign = sign;
        this.timestamp = timestamp;
        this.packages = packages;
        this.noncestr = noncestr;
        this.partnerid = partnerid;
        this.appid = appid;
        this.prepayid = prepayid;
    }

    @Override
    public String toString() {
        return "JSON_Pay{" +
                "sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", packages='" + packages + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", appid='" + appid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                '}';
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }
}
