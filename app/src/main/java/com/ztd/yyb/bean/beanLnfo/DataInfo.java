package com.ztd.yyb.bean.beanLnfo;

/**
 * Created by  on 2017/3/28.
 */

public class DataInfo {


    /**
     * result : true
     * data : {"picture":[{"ygbmid":127,"ygbmcid":102,"ygbmcipath":"8F4B3199null45D682E8C6586F3D.jpg","ygbmciid":65,"ygbmcicreatetime":"2017-4-21 14:55:21"}],"orderinfo":{"order":"0","star":"0"},"userinfo":{"order":"","ygbramount":"0.00","ygbrid":115,"ygbarea":null,"ygbtype":"周结算","ygbmtel":"15863652365","ygbmcreatetime":1492756953000,"ygbmauditing":"0","ygboid":null,"ygbmstate":"1","ygbmid":127,"ygbmkl":null,"star":"","ygbmlogo":"","ygbmfront":"8F4B2F2Fnull018F13D7FC2B0EA0appendfixz.jpg","ygbmcon":"8F4B2F2Fnull585F890D412D66A2appendfixf.jpg","ygbmhand":"8F4B2F2Fnull681EE1CE2027BFECappendfixh.jpg","ygbmcard":"","ygbmsex":"0","ygbmaddress":"法拉利咯","ygbmname":"魏老师","ygbmarea":"湖南长沙芙蓉区","ygbmpin":"635884422333355866","ygbmbank":"","ygbmbankarea":"","ygbmbankname":"","ygbmbanktel":"","ygbravailable":null,"ygbmjob":"老师","ygbmjobage":"1","ygisbindcard":"0","ygissetpw":"0","strstarttime":null,"strendtime":null,"arrmid":null,"ygbmdeviceid":"null","ygbmupdatetime":1492757720000,"ygbmcode":"9024","type":""},"success":"1"}
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
