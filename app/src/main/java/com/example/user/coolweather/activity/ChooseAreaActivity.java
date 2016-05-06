package com.example.user.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.coolweather.R;
import com.example.user.coolweather.model.City;
import com.example.user.coolweather.model.CoolWeatherDB;
import com.example.user.coolweather.model.County;
import com.example.user.coolweather.model.Province;
import com.example.user.coolweather.util.HttpCallbackListener;
import com.example.user.coolweather.util.HttpUtil;
import com.example.user.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends Activity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_County = 1;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> datalist = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;


    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);

        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if (currentLevel == LEVEL_PROVINCE){
                   selectedProvince = provinceList.get(index);
                   queryCities();
               }else if (currentLevel == LEVEL_CITY){
                   selectedCity = cityList.get(index);
                   queryCounties();
               }
            }
        });
        queryProvinces();
    }


    private void queryProvinces(){
        provinceList = coolWeatherDB.provincesLoader();
        Log.d("LCL","provinceList.size()"+provinceList.size());
        if (provinceList.size() > 0){
            datalist.clear();
           int  i =0;
            for (Province province:provinceList){
                Log.d("LCL","province:"+provinceList.get(i).getProvinceName());
                datalist.add(provinceList.get(i++).getProvinceName());
                Log.d("LCL", "datalist:" + datalist.get(i-1));
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }
        else {
            queryFromServer(null,"province");
        }
    }


    private void queryCities(){
        cityList = coolWeatherDB.citiesLoader(selectedProvince.getId());
        if (cityList.size() > 0){
            datalist.clear();
            for (City city:cityList){
                datalist.add(city.getCityName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties(){
        countyList = coolWeatherDB.countiesLoader(selectedCity.getId());
        if (countyList.size() > 0){
            datalist.clear();
            for (County county:countyList){
                datalist.add(county.getCountyName());
            }

            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_County;
        }else {
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    public void queryFromServer(final String code,final String type){
        String address;
        Log.d("LCL","code:"+code+",type:"+type);

        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }

        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("LCL","onFinish...."+"response:"+response);
                boolean result = false;
                Log.d("LCL","type:"+type);
                if ("province".equals(type)) {
                    Log.d("LCL","\"province\".equals(type)");
                    result = Utility.handleProvincesResponse(coolWeatherDB, response);
                }else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(coolWeatherDB,response,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
                }
                Log.d("LCL","result:"+result);
                if (result){
                    //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                //
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"Load Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Display progressDialog
     */

    private void showProgressDialog(){
        if (progressDialog ==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();
    }

    /**
     * Close progressDialog
     */
    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    /**
     * Catch the back pressed
     */
    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_County){
            queryCities();
        }else if (currentLevel ==LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
}
