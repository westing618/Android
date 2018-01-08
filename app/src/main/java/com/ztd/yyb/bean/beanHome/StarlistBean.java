package com.ztd.yyb.bean.beanHome;

import java.util.List;

/**
 * Created by  on 2017/3/23.
 */
public class StarlistBean {
    /**
     * ygbsnums : 1
     * ygbsname : 一星
     * mean : [{"ygbsmid":6,"ygbsmname":"粗心","ygbsid":1},{"ygbsmid":5,"ygbsmname":"迟到","ygbsid":1},{"ygbsmid":4,"ygbsmname":"态度差","ygbsid":1},{"ygbsmid":7,"ygbsmname":"无耐心","ygbsid":1}]
     * ygbsid : 1
     */

    private String ygbsnums;
    private String ygbsname;
    private int ygbsid;
    private List<MeanBean> mean;

    public String getYgbsnums() {
        return ygbsnums;
    }

    public void setYgbsnums(String ygbsnums) {
        this.ygbsnums = ygbsnums;
    }

    public String getYgbsname() {
        return ygbsname;
    }

    public void setYgbsname(String ygbsname) {
        this.ygbsname = ygbsname;
    }

    public int getYgbsid() {
        return ygbsid;
    }

    public void setYgbsid(int ygbsid) {
        this.ygbsid = ygbsid;
    }

    public List<MeanBean> getMean() {
        return mean;
    }

    public void setMean(List<MeanBean> mean) {
        this.mean = mean;
    }
}
