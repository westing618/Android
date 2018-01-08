package com.ztd.yyb.bean.beanOrd;

/**
 * Created by  on 2017/5/2.
 */

public class OrdSuFull {

//{"data":{"success":"0"},"code":"异常","msg":"失败"}

    /**
     * data : {"ygbmcstate":"3","orderstate":"1","success":"0"}
     * msg : 未认证或者认证不通过，不可抢单
     */

    private DataBean data;
    private String msg;

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
