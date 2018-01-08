package com.ztd.yyb.bean.beanPare;

/**
 * Created by  on 2017/4/13.
 */

public class DataPare {

    /**
     * result : true
     * data : {"ygbdgamount":1170,"orderid":"456","success":"1"}
     * code : 0
     * msg : 新增成功
     */

    private String result;
    private DataBean data;
    private String code;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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
