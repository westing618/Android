package com.ztd.yyb.bean;

import java.util.List;

/**
 * Created by  on 2017/3/15.
 */
public class DataBeanX {

//    private PicarrayBean smallimage;
//    private String hotmap;
//    private String success;
//    private List<OrderYginfoBean> orderYginfo;
//
//    private List<PicarrayBeanX> picarray;
//
//    public PicarrayBean getSmallimage() {
//        return smallimage;
//    }
//
//    public void setSmallimage(PicarrayBean smallimage) {
//        this.smallimage = smallimage;
//    }
//
//    public String getHotmap() {
//        return hotmap;
//    }
//
//    public void setHotmap(String hotmap) {
//        this.hotmap = hotmap;
//    }
//
//    public String getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(String success) {
//        this.success = success;
//    }
//
//    public List<OrderYginfoBean> getOrderYginfo() {
//        return orderYginfo;
//    }
//
//    public void setOrderYginfo(List<OrderYginfoBean> orderYginfo) {
//        this.orderYginfo = orderYginfo;
//    }
//
//    public List<PicarrayBeanX> getPicarray() {
//        return picarray;
//    }
//
//    public void setPicarray(List<PicarrayBeanX> picarray) {
//        this.picarray = picarray;
//    }

    private PicarrayBean smallimage;

    private List<OrderYginfoBean> orderYginfo ;

    private String hotmap;

    private String success;

    private List<PicarrayBeanX> picarray ;

    public void setSmallimage(PicarrayBean smallimage){
        this.smallimage = smallimage;
    }
    public PicarrayBean getSmallimage(){
        return this.smallimage;
    }
    public void setOrderYginfo(List<OrderYginfoBean> orderYginfo){
        this.orderYginfo = orderYginfo;
    }
    public List<OrderYginfoBean> getOrderYginfo(){
        return this.orderYginfo;
    }
    public void setHotmap(String hotmap){
        this.hotmap = hotmap;
    }
    public String getHotmap(){
        return this.hotmap;
    }
    public void setSuccess(String success){
        this.success = success;
    }
    public String getSuccess(){
        return this.success;
    }
    public void setPicarray(List<PicarrayBeanX> picarray){
        this.picarray = picarray;
    }
    public List<PicarrayBeanX> getPicarray(){
        return this.picarray;
    }
}
