package com.ztd.yyb.bean.BeanR;

/**
 * Created by  on 2017/5/11.
 */

public class RenZhen {


    /**
     * result : true
     * data : {"ygbmlogo":"","success":"1"}//{"result":"false","data":"","msg":"身份证未上传，不可提交！"}
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
