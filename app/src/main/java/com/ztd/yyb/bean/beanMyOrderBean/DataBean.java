package com.ztd.yyb.bean.beanMyOrderBean;

import java.util.List;

/**
 * Created by  on 2017/3/27.
 */
public class DataBean {
    /**
     * orderJjinfo : [{"order":"","sql":"","ygbmid":"","ygbsctype":"0","ygbscname":"英语","ygbscprice":"130","ygboreceivemid":21,"ygbodid":101,"ygboinitiatemid":11,"ygbdid":"","ygbdgid":"","ygboid":183,"ygbdmode":null,"ygbdstate":"","ygbdauditing":"","ygbdgamount":"","ygbdgmode":"","ygbdgstate":"","ygbdgauditing":"0","ygblcunit":"","ygblcprice":"","ygbdtitle":"","ygbdgaddress":"莲花三村流芳里","ygbdgmounttime":"2017-3-24 02:37:31","ygbdgremark":"","ygbdgcreatetime":"","ygbeducation":"","ygbtime":"","ygbdkind":"","ygbdaddress":"","ygbdtimearrival":"","ygbdtype":"","ygbdremark":"","ygbdcreatetime":"","initiatetel":"","receivetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"2","ygbocreatetime":"2017-3-24 11:36:29","initiatename":"","receivename":"","yglogo":"defaultlogo.png","totalcount":"0","ygbotype":"0","ygbdgtimearrival":"","ygbdgsex":"1","ygboastate":""},{"order":"","sql":"","ygbmid":"","ygbsctype":"0","ygbscname":"英语","ygbscprice":"130","ygboreceivemid":21,"ygbodid":100,"ygboinitiatemid":11,"ygbdid":"","ygbdgid":"","ygboid":182,"ygbdmode":null,"ygbdstate":"","ygbdauditing":"","ygbdgamount":"","ygbdgmode":"","ygbdgstate":"","ygbdgauditing":"0","ygblcunit":"","ygblcprice":"","ygbdtitle":"","ygbdgaddress":"莲花三村流芳里","ygbdgmounttime":"2017-3-24 02:37:26","ygbdgremark":"","ygbdgcreatetime":"","ygbeducation":"","ygbtime":"","ygbdkind":"","ygbdaddress":"","ygbdtimearrival":"","ygbdtype":"","ygbdremark":"","ygbdcreatetime":"","initiatetel":"","receivetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"2","ygbocreatetime":"2017-3-24 11:35:29","initiatename":"","receivename":"","yglogo":"defaultlogo.png","totalcount":"0","ygbotype":"0","ygbdgtimearrival":"","ygbdgsex":"1","ygboastate":""},{"order":"","sql":"","ygbmid":"","ygbsctype":"","ygbscname":"","ygbscprice":"","ygboreceivemid":21,"ygbodid":63,"ygboinitiatemid":11,"ygbdid":"","ygbdgid":"","ygboid":185,"ygbdmode":null,"ygbdstate":"","ygbdauditing":"","ygbdgamount":"","ygbdgmode":"","ygbdgstate":"","ygbdgauditing":"","ygblcunit":"","ygblcprice":"","ygbdtitle":"","ygbdgaddress":"","ygbdgmounttime":"","ygbdgremark":"","ygbdgcreatetime":"","ygbeducation":"","ygbtime":"","ygbdkind":"","ygbdaddress":"","ygbdtimearrival":"","ygbdtype":"","ygbdremark":"","ygbdcreatetime":"","initiatetel":"","receivetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"1","ygbocreatetime":"2017-3-24 01:57:12","initiatename":"","receivename":"","yglogo":"","totalcount":"","ygbotype":"0","ygbdgtimearrival":"","ygbdgsex":"","ygboastate":""}]
     * success : 1
     */

    private String success;
    private List<OrderJjinfoBean> orderJjinfo;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<OrderJjinfoBean> getOrderJjinfo() {
        return orderJjinfo;
    }

    public void setOrderJjinfo(List<OrderJjinfoBean> orderJjinfo) {
        this.orderJjinfo = orderJjinfo;
    }
}
