package com.ztd.yyb.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/15.
 * 解析JSON的支付
 */
public class JSON_PayList implements Serializable {
    @JSONField(name="msg")
    private String msg="";
    @JSONField(name="success")
    private boolean success;
    @JSONField(name="rows")
    private JSON_Pay rows=new JSON_Pay();

    public JSON_PayList() {

    }

    public JSON_PayList(String msg, boolean success, JSON_Pay rows) {
        this.msg = msg;
        this.success = success;
        this.rows = rows;
    }

    public JSON_Pay getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "JSON_PayList{" +
                "msg='" + msg + '\'' +
                ", success=" + success +
                ", rows=" + rows +
                '}';
    }

    public void setRows(JSON_Pay rows) {
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
}
