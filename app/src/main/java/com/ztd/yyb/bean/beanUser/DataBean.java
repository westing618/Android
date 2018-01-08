package com.ztd.yyb.bean.beanUser;

/**
 * Created by  on 2017/3/21.
 */
public class DataBean {
    /**
     * token : EE7260868D47E47B04A77980
     * userid : 11
     * userinfo : {"ygbmid":11,"ygboid":null,"strendtime":null,"arrmid":null,"ygbmlogo":"logo/defaultlogo.png","ygbmsex":null,"ygbmtel":"18650801384","ygbmpin":null,"ygbmname":null,"ygbmstate":"1","ygbmhand":null,"ygbmfront":null,"ygbmcon":null,"ygbmcode":"null","ygbmkl":null,"ygbmbankarea":"厦门","ygbmbankname":"李文龙","ygbmbanktel":"123456789","ygbmcard":"555555555","ygbmbank":"中国工商银行","ygbramount":null,"ygbrid":null,"strstarttime":null,"ygbmaddress":null,"ygbmauditing":"0","ygbmdeviceid":"null","ygbmcreatetime":1489471018000,"ygbmupdatetime":1490079558000}
     * success : true
     */

    private String token;
    private String userid;
    private UserinfoBean userinfo;
    private String success;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
