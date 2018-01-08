package com.ztd.yyb.bean.beanBank;

/**
 * Created by  on 2017/5/3.
 */

public class BankWith {

    /**
     * result : true
     * data : {"ygbchargeamount":"2","ygbbankinfo":"钱君海尾号910","ygbmbankname":"钱君海","ygbmcard":"6212264100026030910","success":"1","ygbramount":"0.02"}
     * msg : 待审核
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
