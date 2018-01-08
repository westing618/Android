package com.ztd.yyb.bean.beanChackInfo;

import java.util.List;

/**
 * Created by  on 2017/5/11.
 */
public class DataBean {
    /**
     * picture : []
     * workerinfo : {"ygbmcstate":"3","ygbmid":396,"ygbmcid":"662","ygbmchand":null,"ygbmccon":null,"ygbmcfront":null,"ygbmcpin":null,"ygbmcname":null,"ygbmccreatetime":"2017-5-11 15:10:53","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"2","ygbmcjobage":null,"ygbmcjob":"","id":""}
     * orderinfo : {"order":"0","star":"0"}
     * userinfo : {"order":"","ygbramount":"9996330.00","ygbrid":384,"ygbmcreatetime":1494486653000,"ygbmauditing":"1","ygbmtel":"15860754228","ygboid":null,"ygbarea":null,"ygbtype":"周结算","ygbmstate":"1","ygbmid":396,"ygbmkl":"bdc031bff64cc1ff","star":"","ygbmlogo":"80ed65d60d9d46fba7d799b090a724f7logo.jpg","ygbmfront":"","ygbmcon":"","ygbmhand":"","ygbmcid":null,"ygbmcard":"","ygbmsex":"1","ygbmaddress":"在万翔国际商务中心附近","ygbmname":"大钱","ygbmarea":"福建莆田城厢区","ygbmpin":"350825194875553522","ygbmjob":"","ygbmjobage":"","ygbmbank":"","ygbmbankarea":"","ygbmbankname":"","ygbmbanktel":"","ygbravailable":"0.00","ygisbindcard":"0","ygissetpw":"1","strstarttime":null,"strendtime":null,"arrmid":null,"ygbmdeviceid":"null","ygbmupdatetime":1494491703000,"ygbmcode":"6418","type":""}
     * success : 1
     * genearchinfo : {"ygbmcstate":"3","ygbmid":396,"ygbmcid":"663","ygbmchand":null,"ygbmccon":null,"ygbmcfront":null,"ygbmcpin":null,"ygbmcname":null,"ygbmccreatetime":"2017-5-11 15:10:53","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"4","ygbmcjobage":null,"ygbmcjob":"","id":""}
     */

    private WorkerinfoBean workerinfo;
    private OrderinfoBean orderinfo;
    private UserinfoBean userinfo;
    private String success;
    private GenearchinfoBean genearchinfo;
    private List<?> picture;

    public WorkerinfoBean getWorkerinfo() {
        return workerinfo;
    }

    public void setWorkerinfo(WorkerinfoBean workerinfo) {
        this.workerinfo = workerinfo;
    }

    public OrderinfoBean getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(OrderinfoBean orderinfo) {
        this.orderinfo = orderinfo;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public GenearchinfoBean getGenearchinfo() {
        return genearchinfo;
    }

    public void setGenearchinfo(GenearchinfoBean genearchinfo) {
        this.genearchinfo = genearchinfo;
    }

    public List<?> getPicture() {
        return picture;
    }

    public void setPicture(List<?> picture) {
        this.picture = picture;
    }
}
