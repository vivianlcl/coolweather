package com.example.user.coolweather.util;

import android.text.TextUtils;

import com.example.user.coolweather.model.City;
import com.example.user.coolweather.model.CoolWeatherDB;
import com.example.user.coolweather.model.Country;
import com.example.user.coolweather.model.Province;

/**
 * Created by user on 16-4-29.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if (allProvinces != null & allProvinces.length > 0){
                for (String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceName(array[0]);
                    province.setProvinceName(array[1]);
                    //
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;

    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null & allCities.length > 0) {
                for (String p : allCities) {
                    String[] array = p.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    //
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null & allCities.length > 0) {
                for (String p : allCities) {
                    String[] array = p.split("\\|");
                    Country country = new Country();
                    country.setCountryCode(array[0]);
                    country.setCountryName(array[1]);
                    country.setCityId(cityId);
                    //
                    coolWeatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

}
