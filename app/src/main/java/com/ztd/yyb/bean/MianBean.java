package com.ztd.yyb.bean;

/**
 * Created by  on 2017/3/15.
 */

public class MianBean {

//    private String result;
//    private DataBeanX data;
//    private String code;
//    private String msg;
//
//    public String getResult() {
//        return result;
//    }
//
//    public void setResult(String result) {
//        this.result = result;
//    }
//
//    public DataBeanX getData() {
//        return data;
//    }
//
//    public void setData(DataBeanX data) {
//        this.data = data;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    private String result;

    private DataBeanX data;

    private String code;

    private String msg;

    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
    public void setData(DataBeanX data){
        this.data = data;
    }
    public DataBeanX getData(){
        return this.data;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

}
