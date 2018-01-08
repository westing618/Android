package com.ztd.yyb.bean.beanCoup;

import java.util.List;

/**
 * Created by  on 2017/3/28.
 */
public class DataBean {
    /**
     * couponarray : [{"state":"1","ygbcstarttime":"2017-3-06 10:43:53","ygbcendtime":"2017-3-08 12:08:53","ygbmid":null,"ygbcnums":100,"ygbcstate":null,"ygbcid":null,"ygbctitle":"rtgr","ygbcamount":100,"ygbcoperator":"","ygbccreattime":"2017-3-09 10:44:04"}]
     * success : 1
     */

    private String success;
    private List<CouponarrayBean> couponarray;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CouponarrayBean> getCouponarray() {
        return couponarray;
    }

    public void setCouponarray(List<CouponarrayBean> couponarray) {
        this.couponarray = couponarray;
    }
}
