package com.ztd.yyb.bean.beanHome;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by  on 2017/3/23.
 */
public class SubBeanX implements IPickerViewData {
    /**
     * addtype :
     * addname : 合肥
     * sub : [{"addtype":"","addname":"瑶海区","sub":null,"apppcd":"101","addcd":"3418"},{"addtype":"","addname":"庐阳区","sub":null,"apppcd":"101","addcd":"3419"},{"addtype":"","addname":"蜀山区","sub":null,"apppcd":"101","addcd":"3420"},{"addtype":"","addname":"包河区","sub":null,"apppcd":"101","addcd":"3421"},{"addtype":"","addname":"巢湖市","sub":null,"apppcd":"101","addcd":"3422"},{"addtype":"","addname":"长丰县","sub":null,"apppcd":"101","addcd":"3423"},{"addtype":"","addname":"肥东县","sub":null,"apppcd":"101","addcd":"3424"},{"addtype":"","addname":"肥西县","sub":null,"apppcd":"101","addcd":"3425"},{"addtype":"","addname":"庐江县","sub":null,"apppcd":"101","addcd":"3426"}]
     * apppcd : 100
     * addcd : 101
     */

    private String addtype;
    private String addname;
    private String apppcd;
    private String addcd;
    private List<SubBean> sub;
    @Override
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

    public List<SubBean> getSub() {
        return sub;
    }

    public void setSub(List<SubBean> sub) {
        this.sub = sub;
    }
}
