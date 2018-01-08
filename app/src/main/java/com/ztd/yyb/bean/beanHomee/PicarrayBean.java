package com.ztd.yyb.bean.beanHomee;

import java.io.Serializable;

/**
 * Created by  on 2017/3/16.
 */
public class PicarrayBean implements Serializable {
    /**
     * type : 1
     * link : www.baidu.com
     * url : image/banner3@2x.png
     * picid :
     */

    private String type;
    private String link;
    private String url;
    private String picid;

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
