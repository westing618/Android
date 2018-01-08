package com.ztd.yyb.bean.beanSuess;

/**
 * Created by  on 2017/3/24.
 */

public class ChangBean {
    private String name;
    private String id;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ChangBean{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
