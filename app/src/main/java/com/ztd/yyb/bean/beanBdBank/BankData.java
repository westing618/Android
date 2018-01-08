package com.ztd.yyb.bean.beanBdBank;

/**
 * Created by  on 2017/4/6.
 */

public class BankData {

    /**
     * result : true
     * data : {"ygbmbank":"中国工商银行","ygisbindcard":"1","ygbmbankname":"钱君海","ygbmbanktel":"18650801384","ygbmbankarea":"厦门市","ygbmcard":"6212264100026030910","success":"1"}
     * msg : 绑定成功
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
