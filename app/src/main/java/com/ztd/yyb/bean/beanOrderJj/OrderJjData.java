package com.ztd.yyb.bean.beanOrderJj;

/**
 * Created by  on 2017/3/23.
 */

public class OrderJjData {

    /**
     * result : true
     * data : {"jjinfo":{"ygbdgmoment":"","ygbdgsubject":"","ygbdgprovince":"","ygbdgaddress":"","ygbdgmounttime":"","ygbdgcontacts":"","ygbdgremark":"教小孩认真读书好好学习天天向上","ygbdgcreatetime":"2017-3-23 04:55:40","ygbeducation":"","ygbdaddress":"莲花三村流芳里","initiatetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"","ygboreceivemid":"","ygboinitiatemid":"","ygbocreatetime":"","initiatename":"","receivename":"","ygbdaddamount":"","ygbdtitle":"","ygbdgcity":"","ygbdgarea":"","ygbdgdays":null,"ygbdgtel":"","ygbdglng":"","ygbdglat":"","ygbtime":"","receivetel":"","ygbodid":"","yglogo":"","totalcount":"780","ygbotype":"","ygbdgsex":"","ygbgsex":"","ygbmid":11,"ygbdgid":13,"ygboid":"","ygbdgmode":"0","ygbdgstate":"0","ygbsctype":"1","ygblcunit":"","ygbscname":"英语","ygbscprice":"130","ygblcprice":"","ygbdauditing":"0","ygbdgamount":"","ygbdgauditing":""},"success":"1"}
     * msg : 抢单成功
     */

    private String result;
    private DataBean data;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
