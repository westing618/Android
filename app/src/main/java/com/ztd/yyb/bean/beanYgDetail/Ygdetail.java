package com.ztd.yyb.bean.beanYgDetail;

/**
 * Created by  on 2017/4/19.
 */

public class Ygdetail {
    private String result;
    private DataBean data;
    private String msg;
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
    public void setData(DataBean data){
        this.data = data;
    }
    public DataBean getData(){
        return this.data;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
}
