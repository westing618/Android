package com.ztd.yyb.bean.beanPro;

/**
 * Created by  on 2017/5/12.
 */

public class DataPro {


    /**
     * result : true
     * data : {"introduce":"用工贝手机客户端，是一款集装修、置物、家教为一体的APP软件，为您提供市场行情分析、并展示成交的信息、推送最新的订单，让您在第一时间享受服务\r\n保障：\r\n专业的认证，家教和师傅必须通过实名、学历等资质认证\r\n可以查看老师和师傅的工龄时长、真实评价及成果\r\n验收成果后交易，资金安全有保障","success":"1"}
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
