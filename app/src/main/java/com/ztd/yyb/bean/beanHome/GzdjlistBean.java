package com.ztd.yyb.bean.beanHome;

/**
 * Created by  on 2017/3/23.
 */
public class GzdjlistBean {
    /**
     * unitpriceid :
     * labourname : 泥水
     * unitprice :
     * unitcd :
     * price : [{"unitpriceid":"1","labourname":"泥水","unitprice":"300","unitcd":"true","price":null,"labourcd":"1"}]
     * labourcd : 1
     */

    private String ygblcid;
    private String ygblcname;
    private String ygblcunit;
    private String ygblccreatetime;
    private String ygblcoperator;
    private String ygblcprice;

    public String getYgblcid() {
        return ygblcid;
    }

    public void setYgblcid(String ygblcid) {
        this.ygblcid = ygblcid;
    }

    public String getYgblcname() {
        return ygblcname;
    }

    public void setYgblcname(String ygblcname) {
        this.ygblcname = ygblcname;
    }

    public String getYgblcunit() {
        return ygblcunit;
    }

    public void setYgblcunit(String ygblcunit) {
        this.ygblcunit = ygblcunit;
    }

    public String getYgblcprice() {
        return ygblcprice;
    }

    public void setYgblcprice(String ygblcprice) {
        this.ygblcprice = ygblcprice;
    }

    public String getYgblcoperator() {
        return ygblcoperator;
    }

    public void setYgblcoperator(String ygblcoperator) {
        this.ygblcoperator = ygblcoperator;
    }

    public String getYgblccreatetime() {
        return ygblccreatetime;
    }

    public void setYgblccreatetime(String ygblccreatetime) {
        this.ygblccreatetime = ygblccreatetime;
    }
}
