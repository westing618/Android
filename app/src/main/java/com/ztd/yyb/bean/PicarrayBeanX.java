package com.ztd.yyb.bean;

/**
 * Created by  on 2017/3/15.
 */
public class PicarrayBeanX {

    private String type;// 1 资讯 2 订单 3 外链接
    private String link;// 外链接网址
    private String url;// 图片路径
    private String picid;//picid:需求id

    private String kind;// 种类 1 广告 2 用工 3 家教
    private String aid;// 广告id

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }
}
