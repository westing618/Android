package com.ztd.yyb.bean.beanCoup;

/**
 * Created by  on 2017/3/28.
 */

public class DataCoup {

    /**
     * result : true
     * data : {"couponarray":[{"state":"1","ygbcstarttime":"2017-3-06 10:43:53","ygbcendtime":"2017-3-08 12:08:53","ygbmid":null,"ygbcnums":100,"ygbcstate":null,"ygbcid":null,"ygbctitle":"rtgr","ygbcamount":100,"ygbcoperator":"","ygbccreattime":"2017-3-09 10:44:04"}],"success":"1"}
     * msg : 获取成功
     */

    private String result;
    private DataBean data;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
