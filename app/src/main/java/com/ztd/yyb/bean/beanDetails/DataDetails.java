package com.ztd.yyb.bean.beanDetails;

/**
 * Created by  on 2017/4/11.
 */

public class DataDetails {


    /**
     * result : true
     * data : {"yginfo":{"order":"","ygboinitiatemid":49,"ygbocategory":"1","ygbodid":195,"ygbdgamount":"","sql":"","ygbmid":"","ygbsctype":"","ygbscname":"","ygbscprice":"","dqcgrs":"","dqcgds":"","totalcount":"2700.00","ygbdgremark":"","ygbotype":"2","ygboreceivemid":11,"ygboid":372,"ygbdkind":"21","ygblcprice":"300","ygbdaddprice":"0.00","ygbdmode":"3","ygbdstate":"2","ygbdauditing":"1","ygbdgmode":"","ygbdgstate":"","ygblcname":"模板","ygblcunit":"1","ygbdgauditing":"","ygbdgid":"","ygbdtitle":"","ygbdgaddress":"","ygbdgmounttime":"","ygbdgcreatetime":"","ygbeducation":"","ygbtime":"","ygbocreatetime":"2017-4-07 14:07:12","initiatetel":"13999999999","receivetel":"18650801384","initiateaddress":"","receiveaddress":"国防大学","ygbdid":"","ygbdtype":"1","ygbdaddress":"我家门前","ygbdtimearrival":"2017-4-10 00:00:00","ygbdremark":"没什么好说的","ygbdcreatetime":"2017-4-07 15:54:37","ygboastate":"","ygbreceipt":"","ygbopaystate":null,"ygbdgsex":"","ygbdgaddprice":"","initiatename":"","receivename":"","yglogo":"","ygbdgtimearrival":"","ygbeid":""},"success":"1"}
     * msg : 获取成功
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
