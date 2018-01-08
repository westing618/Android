package com.ztd.yyb.bean.beanPare;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2017/4/13.
 */

public class WeiXPay {

    /**
     * msg : ok
     * appid : wx27de7d2ab7c7bff2
     * timestamp : 1492071371
     * noncestr : 1616104250
     * partnerid : 1456534802
     * prepayid : wx20170413161611cded74b3be0365186029
     * package : Sign=WXPay
     * sign : 421BAF69AF60101DC746257E2EB25414
     */

    private String msg;
    private String appid;
    private String timestamp;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    @JSONField(name = "package")
    private String packageX;
    private String sign;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
