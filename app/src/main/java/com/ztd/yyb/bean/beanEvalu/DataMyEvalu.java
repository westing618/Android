package com.ztd.yyb.bean.beanEvalu;

/**
 * Created by  on 2017/3/28.
 */

public class DataMyEvalu {

    /**
     * result : true
     * data : {"evaluatearray":[{"ygbdtitle":"","ygbdid":1,"ygbname":"用工贝用户","ygblogo":"defaultlogo.png","ygbeid":1,"ygbestar":5,"star":"","ygboid":"","ygbmid":1,"ygbeevaluate":"","ygbeevaluatemc":"态度还可以,不太准时","ygbecreatetime":"2017-3-10 06:05:06"}],"success":"1"}
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
