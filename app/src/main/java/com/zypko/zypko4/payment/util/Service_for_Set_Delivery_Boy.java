package com.zypko.zypko4.payment.util;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;

import org.json.JSONObject;


public class Service_for_Set_Delivery_Boy extends Service {

    int order_table_id = 0;
    int station_id = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        set_server(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void set_server(Intent intent) {

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            order_table_id  = bundle.getInt("ORDER_TABLE_ID");
            station_id      = bundle.getInt("STATION_ID");
        }

        String url = UrlValues.SET_DELIVERY_BOY_ID_AND_HOLD_10_MINUTS+ "?ORDER_TABLE_ID="+order_table_id+"&STATION_ID="+station_id;

        Log.e("STATION_ID",""+station_id);
        Log.e("ORDER_TABLE_ID",""+order_table_id);

        JSONProvider provider = new JSONProvider(this);
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                stopSelf();

            }

            @Override
            public void onError(VolleyError error) {

                stopSelf();
            }
        });


    }

}
