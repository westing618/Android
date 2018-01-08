package com.ztd.yyb.bean.beanWallet;

/**
 * Created by  on 2017/3/27.
 */

public class WalletData {

    /**
     * result : true
     * data : {"ygbmbank":"","ygisbindcard":"1","ygbmbankname":"","ygbuseamount":630.9,"ygbmbanktel":"","ygbmbankarea":"","ygbmcard":"6228480682970862000","success":"1","ygbramount":701}
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
