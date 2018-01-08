package com.ztd.yyb.bean.beanWith;

/**
 * Created by  on 2017/5/12.
 */

public class DataWithR {


    /**
     * result : true
     * data : {"evaarray":[{"ygboid":null,"ygbrid":384,"ygbmid":"396","ygbramount":9996330,"ygbrcreatetime":"2017-5-11 16:26:55","ygbrdid":846,"ygbrdtype":"2","ygbrdremark":"支出","ygbrdtradetime":"2017-5-11 16:26:55","ygbrdamount":1200,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-5-11 16:26:55","ygbrdoperattime":"2017-5-11 16:26:55","ygtitle":"交易明细"},{"ygboid":null,"ygbrid":384,"ygbmid":"396","ygbramount":9996330,"ygbrcreatetime":"2017-5-11 16:26:55","ygbrdid":842,"ygbrdtype":"2","ygbrdremark":"支出","ygbrdtradetime":"2017-5-11 15:37:46","ygbrdamount":1230,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-5-11 15:37:46","ygbrdoperattime":"2017-5-11 15:37:46","ygtitle":"交易明细"},{"ygboid":null,"ygbrid":384,"ygbmid":"396","ygbramount":9996330,"ygbrcreatetime":"2017-5-11 16:26:55","ygbrdid":835,"ygbrdtype":"2","ygbrdremark":"支出","ygbrdtradetime":"2017-5-11 15:13:03","ygbrdamount":1240,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-5-11 15:13:03","ygbrdoperattime":"2017-5-11 15:13:03","ygtitle":"交易明细"}],"success":"1"}
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
