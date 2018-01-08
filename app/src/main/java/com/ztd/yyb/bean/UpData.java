package com.ztd.yyb.bean;

/**
 * Created by  on 2017/4/5.
 */

public class UpData {


    /**
     * result : true
     * data : {"ygbmlogo":"","success":"1"}    {"data":{"success":"0"},"code":"0","msg":"失败"}
     * msg : 修改成功
     */

    private String result;
    private DataBeanXXX data;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBeanXXX getData() {
        return data;
    }

    public void setData(DataBeanXXX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
