package com.ztd.yyb.bean.beanLnfo;

import java.util.List;

/**
 * Created by  on 2017/4/21.
 */
public class DataBean {
    /**
     * picture : [{"ygbmid":127,"ygbmcid":102,"ygbmcipath":"8F4B3199null45D682E8C6586F3D.jpg","ygbmciid":65,"ygbmcicreatetime":"2017-4-21 14:55:21"}]
     * orderinfo : {"order":"0","star":"0"}
     * userinfo : {"order":"","ygbramount":"0.00","ygbrid":115,"ygbarea":null,"ygbtype":"周结算","ygbmtel":"15863652365","ygbmcreatetime":1492756953000,"ygbmauditing":"0","ygboid":null,"ygbmstate":"1","ygbmid":127,"ygbmkl":null,"star":"","ygbmlogo":"","ygbmfront":"8F4B2F2Fnull018F13D7FC2B0EA0appendfixz.jpg","ygbmcon":"8F4B2F2Fnull585F890D412D66A2appendfixf.jpg","ygbmhand":"8F4B2F2Fnull681EE1CE2027BFECappendfixh.jpg","ygbmcard":"","ygbmsex":"0","ygbmaddress":"法拉利咯","ygbmname":"魏老师","ygbmarea":"湖南长沙芙蓉区","ygbmpin":"635884422333355866","ygbmbank":"","ygbmbankarea":"","ygbmbankname":"","ygbmbanktel":"","ygbravailable":null,"ygbmjob":"老师","ygbmjobage":"1","ygisbindcard":"0","ygissetpw":"0","strstarttime":null,"strendtime":null,"arrmid":null,"ygbmdeviceid":"null","ygbmupdatetime":1492757720000,"ygbmcode":"9024","type":""}
     * success : 1
     */

    private OrderinfoBean orderinfo;
    private UserinfoBean userinfo;
    private String success;
    private List<PictureBean> picture;

    public OrderinfoBean getOrderinfo() {
        return orderinfo;
    }

    public void setOrderinfo(OrderinfoBean orderinfo) {
        this.orderinfo = orderinfo;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<PictureBean> getPicture() {
        return picture;
    }

    public void setPicture(List<PictureBean> picture) {
        this.picture = picture;
    }
}
