package com.zypko.zypko4.globals;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.zypko.zypko4.R;

public class AlertDialog_For_NoInternet extends ContextWrapper {

    Context context;

    public AlertDialog_For_NoInternet(Context base) {
        super(base);
        this.context = base;
    }

    public void AlertDialog(){

        final android.app.AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            alert = new android.app.AlertDialog.Builder(context,R.style.AlertDialogTheme);
        }else {
            alert = new android.app.AlertDialog.Builder(context);
        }


        alert.setCancelable(false);
        alert.setTitle("Error..");
        alert.setMessage("No Internet Connection");
        //alert.setCancelable(false);

        alert.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).onBackPressed();
            }
        });

        android.app.AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));

    }

}
