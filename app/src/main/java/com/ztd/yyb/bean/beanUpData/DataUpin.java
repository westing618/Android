package com.ztd.yyb.bean.beanUpData;

/**
 * Created by  on 2017/3/30.
 */

public class DataUpin {//{"data":{"success":"0"},"code":"0","msg":"身份证未上传，不可提交！"}

    /**
     * result : true
     * data : {"ygbmlogo":"1E163935FFFFFF965E5F3833A85C85D3logo.jpg","success":"1"}
     * msg : 修改成功
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
