package com.zypko.zypko4.server;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface HTTP_Get{
    void onSuccess(JSONObject object);
    void onError(VolleyError error);
}
