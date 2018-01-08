package com.ztd.yyb.bean.beanDemanJ;

import java.util.List;

/**
 * Created by  on 2017/4/1.
 */
public class DataBean {
    /**
     * orderJjinfo : [{"ygbdgmoment":"1","ygbdgauditing":"1","ygbdgid":142,"ygbdtitle":"","ygbdgprovince":"500","ygbdgcity":"508","ygbdgarea":"3661","ygbdgaddress":"jdjsjs","ygbdgmounttime":"2017-3-31 00:00:00","ygbdgtel":"15959265555","ygbdgcontacts":"谁说的","ygbdgremark":"睡觉睡觉","ygbdgcreatetime":"2017-3-31 14:48:43","ygbdglng":"118.1400180000","ygbdglat":"24.4960660000","ygbeducation":"","ygbtime":"","ygbocreatetime":"","ygbocategory":"","ygbdaddress":"","initiatetel":"","receivetel":"","initiateaddress":"","receiveaddress":"","initiatename":"","receivename":"","yglogo":"23350675FFFFFF9662EBBAF010D9B3B5logo.jpg","totalcount":"650","ygbdaddamount":"","ygbdgsex":"1","ygbgsex":"","ygbmid":11,"ygbsctype":"0","ygbscname":"语文","ygbscprice":"130","ygbotype":"","ygboreceivemid":"","ygboinitiatemid":"","ygbodid":"","ygboid":"329","ygbdgamount":"650","ygblcprice":"","ygbdauditing":"","ygbdgsubject":"2","ygbdgdays":5,"ygbdgmode":"0","ygbdgstate":"0","ygblcunit":""}]
     * success : 1
     */

    private String success;
    private List<OrderJjinfoBean> orderJjinfo;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<OrderJjinfoBean> getOrderJjinfo() {
        return orderJjinfo;
    }

    public void setOrderJjinfo(List<OrderJjinfoBean> orderJjinfo) {
        this.orderJjinfo = orderJjinfo;
    }
}
