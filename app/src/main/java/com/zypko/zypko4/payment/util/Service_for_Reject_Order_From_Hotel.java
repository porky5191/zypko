package com.zypko.zypko4.payment.util;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;

import org.json.JSONObject;

public class Service_for_Reject_Order_From_Hotel extends Service {

    int order_table_id = 0;

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

        if (intent != null) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                order_table_id = bundle.getInt("ORDER_TABLE_ID");
            }
        }

        String url = UrlValues.REJECT_ORDER_FROM_HOTEL_IF_TIME_EXCEEDED+ "?ORDER_TABLE_ID="+order_table_id;

        Log.e("ORDER_TABLE_ID",""+order_table_id);

        JSONProvider provider = new JSONProvider(this);
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                stopSelf();

                //Log.e("")

            }

            @Override
            public void onError(VolleyError error) {

                stopSelf();
            }
        });


    }

}
