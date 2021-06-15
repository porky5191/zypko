package com.zypko.zypko4.globals;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckInternet {

    private Context context;
    public CheckInternet(Context context){
        this.context = context;
    }

    public boolean Internet(){

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null;

    }
}
