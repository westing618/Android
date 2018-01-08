package com.ztd.yyb.bean.beanMyCerti;

/**
 * Created by  on 2017/4/5.
 */

public class DataCertifi {


    /**
     * result : true
     * data : {"ygbmaddress":"国防大学","ygbmfront":"2none.png","ygbmlogo":"23350675FFFFFF9662EBBAF010D9B3B5logo.jpg","ygbmhand":"2none.png","cerarray":[{"ygbmid":11,"ygbmcid":null,"ygbmcstate":"3","ygbmccreatetime":"","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"2","id":"26"},{"ygbmid":11,"ygbmcid":null,"ygbmcstate":"3","ygbmccreatetime":"","ygbmcauditor":"","ygbmcauditingtime":"","ygbmctype":"4","id":"27"}],"ygbmarea":"安徽合肥长丰县","ygbmsex":"0","success":"1","ygbmcon":"2none.png","ygbmname":"开发者"}
     * msg : 获取成功
     */

    private String result;
    private DataBean data;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
