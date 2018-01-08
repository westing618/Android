package com.ztd.yyb.bean.beanXQDatil;

/**
 * Created by  on 2017/4/19.
 */

public class DataXqDetail {


    /**
     * result : true
     * data : {"jjinfo":{"ygboinitiatemid":"569","ygbocategory":"2","ygbodid":"848","ygbdgamount":null,"ygbsctype":"0","ygbscname":"物理","ygbscprice":"180.00","totalcount":360,"ygbdgremark":"测试","ygbdgmoment":"22","ygbdgaddress":"北大街","ygbdgmounttime":"2017-8-04 10:22","ygbdgtel":"13888888888","ygbdgcontacts":"","ygbdgcreatetime":"2017-8-01 10:23:39","ygbdgsex":"1","ygbdgauditing":"","ygbdaddress":"","initiatetel":"","ygbotype":"8","ygboreceivemid":"","ygbdgstate":"0","ygboid":"2507","ygblcprice":"","ygbdauditing":"1","straddress":null,"ygbdgsubject":"","ygbdgdays":0.5,"ygbtime":"4小时","ygbdgmode":"3","ygbdglat":"","ygbmid":569,"ygblcunit":"","ygbdgid":848,"ygbdtitle":"","ygbdgprovince":"福建","ygbdgcity":"龙岩","ygbdgarea":"新罗区","ygbdglng":"","ygbeducation":"04","ygbocreatetime":"2017-8-01 10:23:39","yglogo":"","initiatename":"我听过yu","receivename":"","receivetel":"","initiateaddress":"半屏山路","receiveaddress":"","keywords":null,"payState":null,"ygbdgmomentname":"高中二年级","ygbdgrealamount":null,"ygbdgrealaddprice":null,"ygbeducationname":"在校大学生（大四）","ygbdaddamount":"","ygbgsex":"","ygbdgaddprice":null},"success":"1"}
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
