package com.ztd.yyb.bean.beanHome;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by  on 2017/3/23.
 */
public class SubBean implements IPickerViewData {
    /**
     * addtype :
     * addname : 瑶海区
     * sub : null
     * apppcd : 101
     * addcd : 3418
     */

    private String addtype;
    private String addname;
    private String sub;
    private String apppcd;
    private String addcd;
    public String getPickerViewText() {
        return this.addname;
    }

    @Override
    public String getPickerCd() {
        return this.addcd;
    }

    public String getAddtype() {
        return addtype;
    }

    public void setAddtype(String addtype) {
        this.addtype = addtype;
    }

    public String getAddname() {
        return addname;
    }

    public void setAddname(String addname) {
        this.addname = addname;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getApppcd() {
        return apppcd;
    }

    public void setApppcd(String apppcd) {
        this.apppcd = apppcd;
    }

    public String getAddcd() {
        return addcd;
    }

    public void setAddcd(String addcd) {
        this.addcd = addcd;
    }
}
