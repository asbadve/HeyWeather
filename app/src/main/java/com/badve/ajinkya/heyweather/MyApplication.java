package com.badve.ajinkya.heyweather;

/**
 * Created by Ajinkya on 02-06-2015.
 */
import android.app.Application;
import android.content.Context;




public class MyApplication extends Application {


    public static final String API_KEY_OPEN_WEATHER_MAP = "8be06227a313736007f84b540e2aed5f";
    private static MyApplication sInstance;


    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }


}