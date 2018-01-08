package com.ztd.yyb.bean.beanInvoice;

/**
 * Created by  on 2017/5/19.
 */

public class Datainvoice {

    /**
     * result : true
     * data : {"sumamount":4739.58,"success":"1","receiptinfo":[{"ygbramount":200.56,"ygbrid":111,"ygbmid":396,"orderid":"","ygbrtel":"15860754228","ygbrtitle":"公司老板","ygbrcontact":"工业园","ygbrstate":"0","ygbrtype":"","ygbrcontent":"费用结算","ygbrprovince":"福建","ygbrcity":"福州","ygbrarea":"鼓楼区","ygbraddress":"在万翔国际商务中心附近","ygbrcreatetime":"2017-5-19 13:45:10","ygbroperator":"","ygbroperattime":"2017-5-19 13:45:10","useamount":0,"ygbrname":"公司老板","ygbraddressee":""}]}
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
