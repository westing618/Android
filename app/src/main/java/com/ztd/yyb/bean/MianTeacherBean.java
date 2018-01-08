package com.ztd.yyb.bean;

/**
 *  学堂的
 * Created by  on 2017/3/16.
 */

public class MianTeacherBean {


    /**
     * result : true
     * data : {"smallimage":{"link":"www.baidu.com","url":"image/banner2@2x.png"},"hotmap":"heatmap.jsp","orderJjinfo":[{"yglogo":"","totalcount":"","ygbdtitle":"家教","ygbotype":"","ygbdgmode":true,"ygbdgstate":null,"ygbdgsex":"","ygbgsex":"","ygbtime":"","ygbdgcity":"","ygbdgarea":"","ygbdgtel":"15959822336","ygbdglng":"","ygbdglat":"","ygbdgdays":5,"ygbdgid":1,"ygbmid":null,"ygblcprice":"","ygbscprice":"","ygblcunit":"","ygbsctype":"","ygbscname":"","ygbdgremark":"家教","ygbdgaddress":"555699999","ygbdgcreatetime":1489577396000,"ygbdgauditing":null,"ygbdgmoment":"","ygbeducation":"","ygbdgsubject":"1","ygbdgprovince":"","ygbdgcontacts":"联系人","ygbdgamount":"1000","ygbdgmounttime":1489577396000}],"success":"true","picarray":[{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"},{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"},{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"}]}
     * code : 0
     * msg : 获取成功
     */

    private String result;
    private DataBeanXX data;
    private String code;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBeanXX getData() {
        return data;
    }

    public void setData(DataBeanXX data) {
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
