package com.ztd.yyb.bean.beanDeman;

/**
 * Created by  on 2017/4/1.
 */

public class DataDemanYg {

    private DataBean data;

    private int code;

    private String msg;

    public void setData(DataBean data){
        this.data = data;
    }
    public DataBean getData(){
        return this.data;
    }
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
