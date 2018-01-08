package com.ztd.yyb.bean.beanUpPaypw;

/**
 * Created by  on 2017/4/6.
 */

public class Datauppay {


    /**
     * data : {"success":"1"}   {"msg":"请输入正确的手机号","success":"0"}   {"orderNo":1111,"error":"false","msg":"付款成功"}
     * code : 0
     * msg : 成功
     */

    private DataBean data;
    private String code;
    private String msg;

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
