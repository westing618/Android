package com.ztd.yyb.util;

/**
 * 应用更新信息
 * Created by chenjh on 2015/10/24.
 */
public class AppUpdateInfo {
    private int newvercode;
    private String newvername;
    private String url;
    private String updatedlog;
    private int isforce;


    public void setNewvercode(int newvercode) {
        this.newvercode = newvercode;
    }
    public int getNewvercode() {
        return newvercode;
    }


    public void setNewvername(String newvername) {
        this.newvername = newvername;
    }
    public String getNewvername() {
        return newvername;
    }


    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }


    public void setUpdatedlog(String updatedlog) {
        this.updatedlog = updatedlog;
    }
    public String getUpdatedlog() {
        return updatedlog;
    }


    public void setIsforce(int isforce) {
        this.isforce = isforce;
    }
    public int getIsforce() {
        return isforce;
    }
}
