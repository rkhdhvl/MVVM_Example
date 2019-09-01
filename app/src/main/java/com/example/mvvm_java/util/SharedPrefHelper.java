package com.example.mvvm_java.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPrefHelper {
    private static final String PREF_TIME = "Pref_Time";
    private static SharedPrefHelper instance;
    private SharedPreferences sharedPreferences;

    private SharedPrefHelper (Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPrefHelper getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new SharedPrefHelper(context);
        }
        return instance;
    }

    public void saveUpdateTime(long time)
    {
       sharedPreferences.edit().putLong(PREF_TIME,time).apply();
    }

    public long getUpdateTime()
    {
        return sharedPreferences.getLong(PREF_TIME,0);
    }

}
