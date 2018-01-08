package com.ztd.yyb.bean.beanMainf;

import java.util.List;

/**
 * Created by  on 2017/7/6.
 */

public class Data {

    private Smallimage smallimage;

    private List<OrderYginfo> orderYginfo ;

    private String hotmap;

    private String success;

    private List<Picarray> picarray ;

    public void setSmallimage(Smallimage smallimage){
        this.smallimage = smallimage;
    }
    public Smallimage getSmallimage(){
        return this.smallimage;
    }
    public void setOrderYginfo(List<OrderYginfo> orderYginfo){
        this.orderYginfo = orderYginfo;
    }
    public List<OrderYginfo> getOrderYginfo(){
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
    public void setPicarray(List<Picarray> picarray){
        this.picarray = picarray;
    }
    public List<Picarray> getPicarray(){
        return this.picarray;
    }

}
