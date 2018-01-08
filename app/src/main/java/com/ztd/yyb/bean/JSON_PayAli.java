package com.ztd.yyb.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 * 解析JSON的阿里支付
 */
public class JSON_PayAli implements Serializable {
    @JSONField(name="msg")
    private String msg="";
    @JSONField(name="success")
    private boolean success;
    @JSONField(name="rows")
    private String rows="";

    public JSON_PayAli() {
    }

    public JSON_PayAli(String msg, boolean success, String rows) {
        this.msg = msg;
        this.success = success;
        this.rows = rows;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "JSON_PayAli{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", rows='" + rows + '\'' +
                '}';
    }
}
