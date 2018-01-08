package com.ztd.yyb.bean.beanFeed;

/**
 * Created by  on 2017/5/15.
 */

public class DataFeed {


    /**
     * result : true
     * data : {"success":"1"}
     * msg : 提交成功
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
