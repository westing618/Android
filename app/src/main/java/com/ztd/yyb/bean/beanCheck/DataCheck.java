package com.ztd.yyb.bean.beanCheck;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class DataCheck {

    private  String id;
    private String name;

    private String chname;

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }
    public DataCheck(String name, String id,String chname) {
        this.name = name;
        this.id = id;
        this.chname=chname;
    }
    private List<DataCheckChild>  checkchild;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataCheckChild> getCheckchild() {
        return checkchild;
    }

    public void setCheckchild(List<DataCheckChild> checkchild) {
        this.checkchild = checkchild;
    }



    public static class DataCheckChild{

        private  String childid;
        private String childname;

        public DataCheckChild(String childname, String childid) {
            this.childid = childid;
            this.childname = childname;
        }

        public String getChildid() {
            return childid;
        }

        public void setChildid(String childid) {
            this.childid = childid;
        }

        public String getChildname() {
            return childname;
        }

        public void setChildname(String childname) {
            this.childname = childname;
        }
    }
}
