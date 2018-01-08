package com.ztd.yyb.net;

/**
 * Created by chenjh on 2015/11/2.
 */
public class ResponseBody {
    private String rtnCode;
    private String prmOut;
    private String err;
    private String version;
    private int act;

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getPrmOut() {
        return prmOut;
    }

    public void setPrmOut(String prmOut) {
        this.prmOut = prmOut;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }
}
