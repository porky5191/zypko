package com.zypko.zypko4.user_account.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.Prefs;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.Post_to_Server;

import java.util.HashMap;
import java.util.Map;

public class Service_for_Rate_Delivery_Boy extends Service {

    String comment;
    int rating;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        set_up(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void set_up(Intent intent) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            comment = bundle.getString("COMMENT");
            rating  = bundle.getInt("RATING");
        }


        Toast.makeText(this, "rating "+rating+" comment "+comment, Toast.LENGTH_SHORT).show();

        String url = UrlValues.RATE_DELIVERY_BOY;

        Map<String , String> map = new HashMap<>();
        map.put("COMMENT",""+comment);
        map.put("RATING",""+rating);
        map.put("DELIVERY_BOY_ID",""+new PrefEditor(this).getString(Json_Shared_Preference.DELIVERY_BOY_ID));

        Post_to_Server pst = new Post_to_Server(this,map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                Log.e("OBJECT.....SERVICE","string :"+string);
                stopSelf();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("OBJECT.....SERVICE e","error : "+error);

                stopSelf();
            }
        });
    }
}