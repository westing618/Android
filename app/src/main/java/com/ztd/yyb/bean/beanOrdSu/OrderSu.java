package com.ztd.yyb.bean.beanOrdSu;

/**
 * Created by  on 2017/4/27.
 */

public class OrderSu {


    /** {"data":{"orderstate":"0","success":"1"},"msg":"抢单成功"}
     * result : false
     * data : {"ygbmcstate":"3","code":"1","success":"0"}   ygbmcstate：状态：0审核中1认证通过2认证未通过 3未认证
     * msg : 未认证或者认证不通过，不可抢单
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
