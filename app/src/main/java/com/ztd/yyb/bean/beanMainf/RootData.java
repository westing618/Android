package com.ztd.yyb.bean.beanMainf;

/**
 * Created by  on 2017/7/6.
 */

public class RootData{

    private String result;
    private Data data;
    private String code;
    private String msg;
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
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
