package com.zypko.zypko4.user_account.utils;

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

public class Service_for_remove_past_order extends Service {

    int order_table_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        remove_past_order(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void remove_past_order(Intent intent) {

        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                order_table_id = bundle.getInt("ORDER_TABLE_ID");
            }
        }

        String url = UrlValues.PAST_ORDER_REMOVE+ "?ORDER_TABLE_ID="+order_table_id;

        Log.e("SERVICE",""+url);
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
