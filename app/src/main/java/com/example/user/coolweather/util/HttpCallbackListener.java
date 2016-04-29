package com.example.user.coolweather.util;

/**
 * Created by user on 16-4-29.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
