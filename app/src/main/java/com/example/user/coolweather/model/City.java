package com.example.user.coolweather.model;

/**
 * Created by user on 16-4-29.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provienceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvienceId() {
        return provienceId;
    }

    public void setProvienceId(int provienceId) {
        this.provienceId = provienceId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}