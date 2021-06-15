package com.zypko.zypko4.payment.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BroadCast_Receiver_For_Alarm_Manager extends BroadcastReceiver {

    Context context;
    String train_no;
    int order_table_id;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            train_no = bundle.getString("TRAIN_NUMBER");
            order_table_id = bundle.getInt("ORDER_TABLE_ID");

//            Log.e("BROAD_CAST_RECEIVER", "a <>" + train_no);
//            Log.e("BROAD_CAST_RECEIVER", "a <>" + order_table_id);

            send_request();
        }

    }

    private void send_request() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = new Date();

        String url = "https://api.railwayapi.com/v2/live/train/" + train_no + "/station/GHY/date/" + dateFormat.format(date) + "/apikey/" + context.getString(R.string.my_api_key) + "/";

        JSONProvider provider = new JSONProvider(context);
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                try {
                    int response_code = object.getInt("response_code");
                    Log.d("BROAD_CAST_RECEIVER", "" + response_code);

                    if (response_code == 200) {

                        JSONObject obj = object.getJSONObject("status");

                        boolean has_departed = obj.getBoolean("has_departed");
                        String act_arr = obj.getString("actarr");

                        if (!has_departed) {

                            String url2 = UrlValues.UPDATE_ALARM_MANAGER;

                            Map<String, String> map = new HashMap<>();
                            map.put("ORDER_TABLE_ID", "" + order_table_id);
                            map.put("TRAIN_ARRIVAL_TIME", "" + act_arr);

                            Post_to_Server pst = new Post_to_Server(context, map);
                            pst.getJson(url2, new HTTP_Post_Callback() {
                                @Override
                                public void onSuccess(String string) {

                                    try {
                                        JSONObject object1 = new JSONObject(string);
                                        int success = object1.getInt(Json_Objects.SUCCESS);

                                        if (success == 0) {
                                            send_request();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });

                        } else {
                            Intent intent = new Intent(context, BroadCast_Receiver_For_Alarm_Manager.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, order_table_id, intent, 0);
                            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }
}
