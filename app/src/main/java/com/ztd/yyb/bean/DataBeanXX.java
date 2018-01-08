package com.ztd.yyb.bean;

import java.util.List;

/**
 * Created by  on 2017/3/16.
 */
public class DataBeanXX {
    /**
     * smallimage : {"link":"www.baidu.com","url":"image/banner2@2x.png"}
     * hotmap : heatmap.jsp
     * orderJjinfo : [{"yglogo":"","totalcount":"","ygbdtitle":"家教","ygbotype":"","ygbdgmode":true,"ygbdgstate":null,"ygbdgsex":"","ygbgsex":"","ygbtime":"","ygbdgcity":"","ygbdgarea":"","ygbdgtel":"15959822336","ygbdglng":"","ygbdglat":"","ygbdgdays":5,"ygbdgid":1,"ygbmid":null,"ygblcprice":"","ygbscprice":"","ygblcunit":"","ygbsctype":"","ygbscname":"","ygbdgremark":"家教","ygbdgaddress":"555699999","ygbdgcreatetime":1489577396000,"ygbdgauditing":null,"ygbdgmoment":"","ygbeducation":"","ygbdgsubject":"1","ygbdgprovince":"","ygbdgcontacts":"联系人","ygbdgamount":"1000","ygbdgmounttime":1489577396000}]
     * success : true
     * picarray : [{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"},{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"},{"type":"1","link":"www.baidu.com","picid":"","url":"image/banner3@2x.png"}]
     */

    private PicarrayBean smallimage;
    private String hotmap;
    private String success;
    private List<OrderJjinfoBean> orderJjinfo;
    private List<PicarrayBeanXX> picarray;

    public PicarrayBean getSmallimage() {
        return smallimage;
    }

    public void setSmallimage(PicarrayBean smallimage) {
        this.smallimage = smallimage;
    }

    public String getHotmap() {
        return hotmap;
    }

    public void setHotmap(String hotmap) {
        this.hotmap = hotmap;
    }

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

    public List<PicarrayBeanXX> getPicarray() {
        return picarray;
    }

    public void setPicarray(List<PicarrayBeanXX> picarray) {
        this.picarray = picarray;
    }
}
