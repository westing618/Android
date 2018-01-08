package com.ztd.yyb.bean.beanMyReleJj;

import java.util.List;

/**
 * Created by  on 2017/3/30.
 */
public class DataBean {
    /**
     * orderJjinfo : [{"sql":"","ygbodid":133,"ygboid":306,"ygbotype":"8","ygbdmode":null,"ygbdstate":"","ygbmid":"","ygbdgmode":"","ygbdgstate":"0","ygbsctype":"0","dqcgrs":"","dqcgds":"","ygbscname":"英语","ygbscprice":"200","ygboastate":"","order":"","ygblcunit":"","ygblcname":"","ygblcprice":"","ygbdgauditing":"1","ygbdgaddress":"四大行","ygbdgmounttime":"2017-3-30 12:00:00","ygbdgremark":"","ygbdgcreatetime":"","ygbeducation":"","ygbdaddress":"","ygbdtimearrival":"","ygbdcreatetime":"","initiatetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"2","ygbocreatetime":"2017-3-30 05:03:59","initiatename":"","receivename":"","ygbdgtimearrival":"","ygbdid":"","ygbdtype":"","ygbdremark":"","receivetel":"","yglogo":"1E373684FFFFFF96230568CB26DF312Alogo.jpg","totalcount":"0","ygbdgsex":"0","ygbeid":"","ygbdgid":"","ygbdtitle":"","ygbtime":"","ygbdkind":"","ygboreceivemid":null,"ygboinitiatemid":11,"ygbdauditing":"","ygbdgamount":""},{"sql":"","ygbodid":132,"ygboid":305,"ygbotype":"8","ygbdmode":null,"ygbdstate":"","ygbmid":"","ygbdgmode":"","ygbdgstate":"0","ygbsctype":"0","dqcgrs":"","dqcgds":"","ygbscname":"英语","ygbscprice":"200","ygboastate":"","order":"","ygblcunit":"","ygblcname":"","ygblcprice":"","ygbdgauditing":"1","ygbdgaddress":"四大行","ygbdgmounttime":"2017-3-30 12:00:00","ygbdgremark":"","ygbdgcreatetime":"","ygbeducation":"","ygbdaddress":"","ygbdtimearrival":"","ygbdcreatetime":"","initiatetel":"","initiateaddress":"","receiveaddress":"","ygbocategory":"2","ygbocreatetime":"2017-3-30 05:03:28","initiatename":"","receivename":"","ygbdgtimearrival":"","ygbdid":"","ygbdtype":"","ygbdremark":"","receivetel":"","yglogo":"1E373684FFFFFF96230568CB26DF312Alogo.jpg","totalcount":"0","ygbdgsex":"0","ygbeid":"","ygbdgid":"","ygbdtitle":"","ygbtime":"","ygbdkind":"","ygboreceivemid":null,"ygboinitiatemid":11,"ygbdauditing":"","ygbdgamount":""}]
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
