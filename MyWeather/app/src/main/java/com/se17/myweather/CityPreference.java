package com.se17.myweather;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {
     
    SharedPreferences prefs;
     
    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }
     

    public String getCity(){

        return prefs.getString("city", "Karachi, KHI");
    }
     
    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
     
}