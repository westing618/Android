package com.ztd.yyb.bean.beanEvalu;

import java.util.List;

/**
 * Created by  on 2017/3/28.
 */
public class DataBean {
    /**
     * evaluatearray : [{"ygbdtitle":"","ygbdid":1,"ygbname":"用工贝用户","ygblogo":"defaultlogo.png","ygbeid":1,"ygbestar":5,"star":"","ygboid":"","ygbmid":1,"ygbeevaluate":"","ygbeevaluatemc":"态度还可以,不太准时","ygbecreatetime":"2017-3-10 06:05:06"}]
     * success : 1
     */

    private String success;
    private List<EvaluatearrayBean> evaluatearray;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<EvaluatearrayBean> getEvaluatearray() {
        return evaluatearray;
    }

    public void setEvaluatearray(List<EvaluatearrayBean> evaluatearray) {
        this.evaluatearray = evaluatearray;
    }
}
