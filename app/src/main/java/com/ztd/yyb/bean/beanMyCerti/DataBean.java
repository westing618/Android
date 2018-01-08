package com.ztd.yyb.bean.beanMyCerti;

import java.util.List;

/**
 * Created by  on 2017/4/5.
 */
public class DataBean {
    /**
     * ygbmaddress : 国防大学
     * ygbmfront : 2none.png
     * ygbmlogo : 23350675FFFFFF9662EBBAF010D9B3B5logo.jpg
     * ygbmhand : 2none.png
     * cerarray : [{"ygbmid":11,"ygbmcid":null,"ygbmcstate":"3","ygbmccreatetime":"","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"2","id":"26"},{"ygbmid":11,"ygbmcid":null,"ygbmcstate":"3","ygbmccreatetime":"","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"4","id":"27"}]
     * ygbmarea : 安徽合肥长丰县
     * ygbmsex : 0
     * success : 1
     * ygbmcon : 2none.png
     * ygbmname : 开发者
     */

    private String ygbmaddress;
    private String ygbmfront;
    private String ygbmlogo;
    private String ygbmhand;
    private String ygbmarea;
    private String ygbmsex;
    private String success;
    private String ygbmcon;
    private String ygbmname;
    private String ygbmpin;

    public String getYgbmpin() {
        return ygbmpin;
    }

    public void setYgbmpin(String ygbmpin) {
        this.ygbmpin = ygbmpin;
    }

    private List<CerarrayBean> cerarray;

    public String getYgbmaddress() {
        return ygbmaddress;
    }

    public void setYgbmaddress(String ygbmaddress) {
        this.ygbmaddress = ygbmaddress;
    }

    public String getYgbmfront() {
        return ygbmfront;
    }

    public void setYgbmfront(String ygbmfront) {
        this.ygbmfront = ygbmfront;
    }

    public String getYgbmlogo() {
        return ygbmlogo;
    }

    public void setYgbmlogo(String ygbmlogo) {
        this.ygbmlogo = ygbmlogo;
    }

    public String getYgbmhand() {
        return ygbmhand;
    }

    public void setYgbmhand(String ygbmhand) {
        this.ygbmhand = ygbmhand;
    }

    public String getYgbmarea() {
        return ygbmarea;
    }

    public void setYgbmarea(String ygbmarea) {
        this.ygbmarea = ygbmarea;
    }

    public String getYgbmsex() {
        return ygbmsex;
    }

    public void setYgbmsex(String ygbmsex) {
        this.ygbmsex = ygbmsex;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getYgbmcon() {
        return ygbmcon;
    }

    public void setYgbmcon(String ygbmcon) {
        this.ygbmcon = ygbmcon;
    }

    public String getYgbmname() {
        return ygbmname;
    }

    public void setYgbmname(String ygbmname) {
        this.ygbmname = ygbmname;
    }

    public List<CerarrayBean> getCerarray() {
        return cerarray;
    }

    public void setCerarray(List<CerarrayBean> cerarray) {
        this.cerarray = cerarray;
    }
}
