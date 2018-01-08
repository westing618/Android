package com.ztd.yyb.bean;

/**
 * Created by  on 2017/3/15.
 */

public class HomeBean {


    /**
     * result : true
     * data : {"weatherinfo":{"weather":"晴","weatherurl":"image/1.png","city":"厦门","cityid":"101230201","temp1":"8℃","temp2":"20℃","img1":"n0.gif","img2":"d0.gif","ptime":"18:00"},"smallimage":{"link":"www.baidu.com","url":"image/banner2@2x.png"},"dqcgrs":"5565","orderYginfo":[{"ygblcunit":"1","ygblcprice":"300","ygbdid":2,"ygbmid":1,"yglogo":"","totalcount":"15000","ygbdtitle":"用工2","ygbdcity":"","ygbdarea":"","ygbdkind":"1","ygbdtel":"123456789","ygbdremark":"要做的好","ygbdlng":"","ygbdlat":"","ygbotype":"","ygbdtype":"1","ygbddays":5,"ygbdamount":null,"ygbdmode":null,"ygbdstate":null,"ygbdprovince":"","ygbdaddress":"万翔商务中心3号楼","ygbdcontacts":"联系","ygbdtimearrival":1489560028000,"ygbdworkers":10,"ygbdcreatetime":1489560028000,"ygbdauditing":null},{"ygblcunit":"1","ygblcprice":"300","ygbdid":1,"ygbmid":1,"yglogo":"","totalcount":"15000","ygbdtitle":"用工1","ygbdcity":"","ygbdarea":"","ygbdkind":"1","ygbdtel":"123456789","ygbdremark":"要做的好","ygbdlng":"","ygbdlat":"","ygbotype":"","ygbdtype":"1","ygbddays":5,"ygbdamount":null,"ygbdmode":null,"ygbdstate":null,"ygbdprovince":"","ygbdaddress":"万翔商务中心3号楼","ygbdcontacts":"联系","ygbdtimearrival":1489560026000,"ygbdworkers":10,"ygbdcreatetime":1489560026000,"ygbdauditing":null}],"dqcgds":"2578","success":"true","picarray":[{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""},{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""},{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""}]}
     * code : 0
     * msg : 获取成功
     */

    private String result;
    private DataBean data;
    private String code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
