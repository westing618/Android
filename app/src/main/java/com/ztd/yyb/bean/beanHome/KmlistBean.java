package com.ztd.yyb.bean.beanHome;

import java.util.List;

/**
 * Created by  on 2017/3/23.
 */
public class KmlistBean {
    /**
     * subject : [{"subjectprice":"130","subjectname":"语文","subjectcd":"1"},{"subjectprice":"130","subjectname":"英语","subjectcd":"1"}]
     * classcd : 1
     * classname : 小学
     */

    private String classcd;
    private String classname;
    private List<SubjectBean> subject;

    public String getClasscd() {
        return classcd;
    }

    public void setClasscd(String classcd) {
        this.classcd = classcd;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<SubjectBean> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectBean> subject) {
        this.subject = subject;
    }
}
