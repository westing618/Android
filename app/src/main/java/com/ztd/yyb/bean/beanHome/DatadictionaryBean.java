package com.ztd.yyb.bean.beanHome;

import java.util.List;

/**
 * Created by  on 2017/3/23.
 */
public class DatadictionaryBean {
    /**
     * dmlist : [{"dmlist":null,"zlmc":"","dmmc":"5公里","dm":"01","zldm":""}]
     * zlmc : 公里数
     * dmmc :
     * dm :
     * zldm : gls
     */

    private String zlmc;
    private String dmmc;
    private String dm;
    private String zldm;
    private List<DmlistBean> dmlist;

    public String getZlmc() {
        return zlmc;
    }

    public void setZlmc(String zlmc) {
        this.zlmc = zlmc;
    }

    public String getDmmc() {
        return dmmc;
    }

    public void setDmmc(String dmmc) {
        this.dmmc = dmmc;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getZldm() {
        return zldm;
    }

    public void setZldm(String zldm) {
        this.zldm = zldm;
    }

    public List<DmlistBean> getDmlist() {
        return dmlist;
    }

    public void setDmlist(List<DmlistBean> dmlist) {
        this.dmlist = dmlist;
    }
}
