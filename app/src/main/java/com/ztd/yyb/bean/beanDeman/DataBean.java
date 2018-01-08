package com.ztd.yyb.bean.beanDeman;

import com.ztd.yyb.bean.beanMainf.OrderYginfo;

import java.util.List;

/**
 * Created by  on 2017/4/1.
 */
public class DataBean {
    private List<OrderYginfoBean> orderYginfo ;

    private int success;

    public void setOrderYginfo(List<OrderYginfoBean> orderYginfo){
        this.orderYginfo = orderYginfo;
    }
    public List<OrderYginfoBean> getOrderYginfo(){
        return this.orderYginfo;
    }
    public void setSuccess(int success){
        this.success = success;
    }
    public int getSuccess(){
        return this.success;
    }
}
