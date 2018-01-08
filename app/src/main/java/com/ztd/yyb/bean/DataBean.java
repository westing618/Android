package com.ztd.yyb.bean;

import java.util.List;

/**
 * Created by  on 2017/3/16.
 */
public class DataBean {
    /**
     * weatherinfo : {"weather":"晴","weatherurl":"image/1.png","city":"厦门","cityid":"101230201","temp1":"8℃","temp2":"20℃","img1":"n0.gif","img2":"d0.gif","ptime":"18:00"}
     * smallimage : {"link":"www.baidu.com","url":"image/banner2@2x.png"}
     * dqcgrs : 5565
     * orderYginfo : [{"ygblcunit":"1","ygblcprice":"300","ygbdid":2,"ygbmid":1,"yglogo":"","totalcount":"15000","ygbdtitle":"用工2","ygbdcity":"","ygbdarea":"","ygbdkind":"1","ygbdtel":"123456789","ygbdremark":"要做的好","ygbdlng":"","ygbdlat":"","ygbotype":"","ygbdtype":"1","ygbddays":5,"ygbdamount":null,"ygbdmode":null,"ygbdstate":null,"ygbdprovince":"","ygbdaddress":"万翔商务中心3号楼","ygbdcontacts":"联系","ygbdtimearrival":1489560028000,"ygbdworkers":10,"ygbdcreatetime":1489560028000,"ygbdauditing":null},{"ygblcunit":"1","ygblcprice":"300","ygbdid":1,"ygbmid":1,"yglogo":"","totalcount":"15000","ygbdtitle":"用工1","ygbdcity":"","ygbdarea":"","ygbdkind":"1","ygbdtel":"123456789","ygbdremark":"要做的好","ygbdlng":"","ygbdlat":"","ygbotype":"","ygbdtype":"1","ygbddays":5,"ygbdamount":null,"ygbdmode":null,"ygbdstate":null,"ygbdprovince":"","ygbdaddress":"万翔商务中心3号楼","ygbdcontacts":"联系","ygbdtimearrival":1489560026000,"ygbdworkers":10,"ygbdcreatetime":1489560026000,"ygbdauditing":null}]
     * dqcgds : 2578
     * success : true
     * picarray : [{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""},{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""},{"type":"1","link":"www.baidu.com","url":"image/banner3@2x.png","picid":""}]
     */

    private WeatherinfoBean weatherinfo;
    private SmallimageBean smallimage;
    private String dqcgrs;
    private String dqcgds;
    private String success;
    private List<OrderYginfoBeanX> orderYginfo;


    public WeatherinfoBean getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherinfoBean weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    public SmallimageBean getSmallimage() {
        return smallimage;
    }

    public void setSmallimage(SmallimageBean smallimage) {
        this.smallimage = smallimage;
    }

    public String getDqcgrs() {
        return dqcgrs;
    }

    public void setDqcgrs(String dqcgrs) {
        this.dqcgrs = dqcgrs;
    }

    public String getDqcgds() {
        return dqcgds;
    }

    public void setDqcgds(String dqcgds) {
        this.dqcgds = dqcgds;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<OrderYginfoBeanX> getOrderYginfo() {
        return orderYginfo;
    }

    public void setOrderYginfo(List<OrderYginfoBeanX> orderYginfo) {
        this.orderYginfo = orderYginfo;
    }
    private List<PicarrayBean> picarray;
    public List<PicarrayBean> getPicarray() {
        return picarray;
    }

    public void setPicarray(List<PicarrayBean> picarray) {
        this.picarray = picarray;
    }
}
