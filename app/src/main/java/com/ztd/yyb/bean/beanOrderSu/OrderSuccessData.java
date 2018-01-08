package com.ztd.yyb.bean.beanOrderSu;

/**
 * Created by  on 2017/3/23.
 */

public class OrderSuccessData {


    /**
     * result : true
     * data : {"yginfo":{"ygbdauditing":"0","ygbdgauditing":null,"ygbdgaddress":"","ygbdgremark":"","ygbdgcreatetime":"","ygbdaddress":"544554444","ygbdprovince":"","ygbdtimearrival":"2017-3-22 07:01:04","ygbdworkers":null,"ygbdcontacts":"","ygbdcreatetime":"2017-3-22 07:01:04","initiatetel":"13888880000","initiateaddress":"福建省厦门市湖里区港中路1690号万翔国际商务中心","receiveaddress":"","ygbocategory":"1","ygboreceivemid":"00000000018","ygboinitiatemid":"1","ygbocreatetime":"2017-03-21 13:46:24.0","initiatename":"开发者","receivename":"","ygbdgtimearrival":"","order":"","ygbimage":"","ygbcid":"","ygbdtitle":"","ygbdkind":"1","ygbdamount":0,"ygbdtype":"1","ygbdcity":"","ygbdarea":"","ygbddays":null,"ygbdtel":"","ygbdremark":"","ygbdlng":"","ygbdlat":"","receivetel":"15960252881","ygbodid":"3","yglogo":"","totalcount":"","ygbotype":"3","ygbdid":20,"ygbmid":1,"ygboid":"20","ygbdmode":"0","ygbdstate":"1","ygbdgmode":"","ygbdgstate":"","sumamount":"","ygblcunit":"","ygblcprice":""},"success":"1"}
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
