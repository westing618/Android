package com.ztd.yyb.bean.beanSuess;

/**
 * Created by  on 2017/3/24.
 */

public class SuessBean {

    /**
     * data : {"success":"0"}
     * code : 0
     * msg : 订单已被抢
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
