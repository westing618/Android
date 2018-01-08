package com.ztd.yyb.bean.beanInvoice;

import java.util.List;

/**
 * Created by  on 2017/5/19.
 */
public class DataBean {
    /**
     * sumamount : 4739.58
     * success : 1
     * receiptinfo : [{"ygbramount":200.56,"ygbrid":111,"ygbmid":396,"orderid":"","ygbrtel":"15860754228","ygbrtitle":"公司老板","ygbrcontact":"工业园","ygbrstate":"0","ygbrtype":"","ygbrcontent":"费用结算","ygbrprovince":"福建","ygbrcity":"福州","ygbrarea":"鼓楼区","ygbraddress":"在万翔国际商务中心附近","ygbrcreatetime":"2017-5-19 13:45:10","ygbroperator":"","ygbroperattime":"2017-5-19 13:45:10","useamount":0,"ygbrname":"公司老板","ygbraddressee":""}]
     */

    private double sumamount;
    private String success;
    private List<ReceiptinfoBean> receiptinfo;

    public double getSumamount() {
        return sumamount;
    }

    public void setSumamount(double sumamount) {
        this.sumamount = sumamount;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ReceiptinfoBean> getReceiptinfo() {
        return receiptinfo;
    }

    public void setReceiptinfo(List<ReceiptinfoBean> receiptinfo) {
        this.receiptinfo = receiptinfo;
    }
}
