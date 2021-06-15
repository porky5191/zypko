package com.zypko.zypko4.globals;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefEditor implements Prefs{

    Context context;
    SharedPreferences preferences;

    public PrefEditor(Context context){
        this.context = context;
    }

    public void writeData(String key, String value){
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

   public void writeData(String key, long value){
        preferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key,value);
        editor.commit();
   }

    public String getString(String key){
        String str = "";
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        str = preferences.getString(key, "");
        return str;
    }


    public long getLong(String key){
        long val = 0;
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        val = preferences.getLong(key, 0);
        return val;
    }

}
