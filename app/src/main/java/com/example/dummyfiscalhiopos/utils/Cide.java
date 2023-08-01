package com.example.dummyfiscalhiopos.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class Cide {

    // Variavles
    private Context context;
    private Activity activity;

    // Constructor
    public Cide(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private SharedPreferences.Editor cachePut() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.edit();
    }

    public void cacheGuardaString(String key, String dato) {
        cachePut().putString(key, dato).commit();
    }

    public String cacheLeerString(String key) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return myPreferences.getString(key, "");
    }


    // GET
    public Context getContext() {
        return context;
    }


}
