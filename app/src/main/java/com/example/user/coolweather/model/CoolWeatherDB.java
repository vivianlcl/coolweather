package com.example.user.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.user.coolweather.db.CoolWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16-4-29.
 */
public class CoolWeatherDB {
    /**
     *
     * Datebase's name
     */

    public static final String DB_NAME = "cool_weather";


    /**
     * Datebase's version
     */
    public static final int VERION = 1;
    public CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    /**
     * private the
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERION);
        db = dbHelper.getWritableDatabase();
    }
    /**
     *
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB = null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvience(Provience provience){
        if(provience!=null){
            ContentValues values = new ContentValues();
            values.put("provience_name",provience.getProvienceName());
            values.put("provience_code",provience.getProvienceCode());
            db.insert("provience",null,values);
        }
    }

    /**
     * read provience information from database
     * @return
     */
    public List<Provience> proviencesLoader(){
        List<Provience> list = new ArrayList<Provience>();
        Cursor cursor = db.query("Provience",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Provience provience = new Provience();
                provience.setId(cursor.getInt(cursor.getColumnIndex("id")));
                provience.setProvienceName(cursor.getString(cursor.getColumnIndex("provience_name")));
                provience.setProvienceCode(cursor.getString(cursor.getColumnIndex("provience_code")));
                list.add(provience);
            }while(cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return list;
    }

    /**
     * storage the city instance to database
     */
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("provience_id",city.getProvienceId());
            db.insert("City", null, Values);
        }
    }

    /**
     *
     */

    public List<City> LoadCities(int provienceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,"provience_id=?",new String[]{String.valueOf(provienceId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvienceId(provienceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return list;
    }


    public void saveCountry(Country country){
        if (country != null){
            ContentValues values = new ContentValues();
            values.put("country_name",country.getCountryName());
            values.put("country_code",country.getCountryCode());
            values.put("city_id",country.getCityId());
            db.insert("country", null, values);
        }
    }

    /**
     *
     */

    public List<Country> LoadCountries(int cityId){
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("country",null,"city_id=?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCityId(cityId);
                list.add(country);
            }while(cursor.moveToNext());
        }
        if (cursor!=null){
            cursor.close();
        }
        return list;
    }
}
