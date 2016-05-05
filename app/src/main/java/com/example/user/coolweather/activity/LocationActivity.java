package com.example.user.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.coolweather.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 16-5-5.
 */
public class LocationActivity extends Activity{
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;
    public static final int SHOW_LOCATOIN =0;
    private String address = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);
        positionTextView = (TextView)findViewById(R.id.position_text_view);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //get all location providers that can use
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            //There is no location provider can use,then Pop-up prompts
            Toast.makeText(this,"No location provider to use",Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null){
            //show the current device information
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //remove the locationListener,when turn off app
            locationManager.removeUpdates(locationListener);
        }
    }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //update the current device's location information
                showLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

    private void showLocation(final Location location){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Assembly reverse geocoding interface address
                    StringBuilder uri = new StringBuilder();

                    uri.append("http://api.map.baidu.com/geocoder?location=");
                    uri.append(location.getLatitude()).append(",");
                    uri.append(location.getLongitude());
                    uri.append("&output=json&key="+"b6D0rlWO1iQuVhW89EgUq01G");
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(uri.toString());
                    //
//                    httpGet.addHeader("Accept-Language","zh-CN");
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode()==200){
                        Log.d("LCL","I am 200");
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject addressComponent =jsonObject.getJSONObject("result").getJSONObject("addressComponent");
                        address = addressComponent.getString("district");
                        Message message = new Message();
                        message.what = SHOW_LOCATOIN;
                        message.obj = address;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_LOCATOIN:
                    String currentPosition = (String)msg.obj;
                    positionTextView.setText(currentPosition);
            }
        }
    };

//        private void showLocation(Location location){
//        String currentPosition = "latitude is:"+location.getLatitude()+"\n"+"longitude is:"+location.getLongitude();
//        positionTextView.setText(currentPosition);
//    }


}
