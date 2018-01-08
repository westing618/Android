package com.ztd.yyb.bean.beanTran;

import java.util.List;

/**
 * Created by  on 2017/5/3.
 */
public class DataBean {
    /**
     * totaloutput : 0.07
     * totalinput : 0.00
     * success : 1
     * moneyarray : [{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-05-02 17:00:56","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-05-02 17:00:56","ygbrdoperattime":"2017-05-02 17:00:56","ygbrdid":418},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-05-02 16:52:52","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-05-02 16:52:52","ygbrdoperattime":"2017-05-02 16:52:52","ygbrdid":416},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-05-02 16:49:11","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-05-02 16:49:11","ygbrdoperattime":"2017-05-02 16:49:11","ygbrdid":415},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"余额支付加价","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-05-02 16:06:47","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-05-02 16:06:47","ygbrdoperattime":"2017-05-02 16:06:47","ygbrdid":408},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-04-27 17:22:13","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-04-27 17:22:13","ygbrdoperattime":"2017-04-27 17:22:13","ygbrdid":334},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-04-27 17:15:53","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-04-27 17:15:53","ygbrdoperattime":"2017-04-27 17:15:53","ygbrdid":333},{"ygbrcreatetime":"","ygtitle":"交易明细","ygbramount":null,"ygbrid":158,"ygbrdremark":"支出","ygbmid":"169","ygbrdtype":"2","ygbrdtradetime":"2017-04-27 17:08:07","ygbrdamount":0.01,"ygbrdstate":"1","ygbrdoperator":"user","ygbrdcreatetime":"2017-04-27 17:08:07","ygbrdoperattime":"2017-04-27 17:08:07","ygbrdid":332}]
     */

    private String totaloutput;
    private String totalinput;
    private String success;
    private List<MoneyarrayBeanX> moneyarray;

    public String getTotaloutput() {
        return totaloutput;
    }

    public void setTotaloutput(String totaloutput) {
        this.totaloutput = totaloutput;
    }

    public String getTotalinput() {
        return totalinput;
    }

    public void setTotalinput(String totalinput) {
        this.totalinput = totalinput;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<MoneyarrayBeanX> getMoneyarray() {
        return moneyarray;
    }

    public void setMoneyarray(List<MoneyarrayBeanX> moneyarray) {
        this.moneyarray = moneyarray;
    }
}
