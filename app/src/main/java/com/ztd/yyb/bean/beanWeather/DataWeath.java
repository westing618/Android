package com.ztd.yyb.bean.beanWeather;

/**
 * Created by  on 2017/5/26.
 */

public class DataWeath {


    /**
     * weatherinfo : {"weather":"阴转多云","weatherbackground":"#d7f3ff","weatherurl":"image/4.png","city":null,"cityid":null,"temp1":"27","temp2":"21","img1":null,"img2":null,"ptime":null}
     * success : 1
     */

    private WeatherinfoBean weatherinfo;
    private String success;

    public WeatherinfoBean getWeatherinfo() {
        return weatherinfo;
    }

    public void setWeatherinfo(WeatherinfoBean weatherinfo) {
        this.weatherinfo = weatherinfo;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
