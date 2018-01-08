package com.ztd.yyb.bean.beanWeather;

/**
 * Created by  on 2017/5/26.
 */
public class WeatherinfoBean {
    /**
     * weather : 阴转多云
     * weatherbackground : #d7f3ff
     * weatherurl : image/4.png
     * city : null
     * cityid : null
     * temp1 : 27
     * temp2 : 21
     * img1 : null
     * img2 : null
     * ptime : null
     */

    private String weather;
    private String weatherbackground;
    private String weatherurl;
    private Object city;
    private Object cityid;
    private String temp1;
    private String temp2;
    private Object img1;
    private Object img2;
    private Object ptime;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeatherbackground() {
        return weatherbackground;
    }

    public void setWeatherbackground(String weatherbackground) {
        this.weatherbackground = weatherbackground;
    }

    public String getWeatherurl() {
        return weatherurl;
    }

    public void setWeatherurl(String weatherurl) {
        this.weatherurl = weatherurl;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getCityid() {
        return cityid;
    }

    public void setCityid(Object cityid) {
        this.cityid = cityid;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public Object getImg1() {
        return img1;
    }

    public void setImg1(Object img1) {
        this.img1 = img1;
    }

    public Object getImg2() {
        return img2;
    }

    public void setImg2(Object img2) {
        this.img2 = img2;
    }

    public Object getPtime() {
        return ptime;
    }

    public void setPtime(Object ptime) {
        this.ptime = ptime;
    }
}
