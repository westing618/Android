package com.ztd.yyb.evenbean;

/**
 * Created by  on 2017/5/8.
 */

public class CouEvent {

    public  String msg;

    public  String id;

    public  String proce;

    public  boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getProce() {
        return proce;
    }

    public void setProce(String proce) {
        this.proce = proce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
