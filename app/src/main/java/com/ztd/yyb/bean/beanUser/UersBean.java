package com.ztd.yyb.bean.beanUser;

/**
 * Created by  on 2017/3/21.
 */

public class UersBean {

    /**
     * data : {"token":"EE7260868D47E47B04A77980","userid":"11","userinfo":{"ygbmid":11,"ygboid":null,"strendtime":null,"arrmid":null,"ygbmlogo":"logo/defaultlogo.png","ygbmsex":null,"ygbmtel":"18650801384","ygbmpin":null,"ygbmname":null,"ygbmstate":"1","ygbmhand":null,"ygbmfront":null,"ygbmcon":null,"ygbmcode":"null","ygbmkl":null,"ygbmbankarea":"厦门","ygbmbankname":"李文龙","ygbmbanktel":"123456789","ygbmcard":"555555555","ygbmbank":"中国工商银行","ygbramount":null,"ygbrid":null,"strstarttime":null,"ygbmaddress":null,"ygbmauditing":"0","ygbmdeviceid":"null","ygbmcreatetime":1489471018000,"ygbmupdatetime":1490079558000},"success":"true"}
     * code : 0
     * msg : 校验成功
     */

    private DataBean data;
    private String code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
