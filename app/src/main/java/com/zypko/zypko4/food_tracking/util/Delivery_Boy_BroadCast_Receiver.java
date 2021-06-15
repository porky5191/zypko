package com.zypko.zypko4.food_tracking.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;

public class Delivery_Boy_BroadCast_Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        rating_of_delivery_boy(context);
    }

    private void rating_of_delivery_boy(final Context context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.layout_for_rating,null);

        TextView name           = view.findViewById(R.id.tv_name_delivery_boy_rating);
        TextView phone_no       = view.findViewById(R.id.tv_phone_no_delivery_boy_rating);
        final EditText comment        = view.findViewById(R.id.et_comment_delivery_boy_rating);
        ImageView del_boy_img   = view.findViewById(R.id.imageView_delivery_boy_rating);
        final Button btn_submit       = view.findViewById(R.id.btn_submit_delivery_boy_rating);
        final RatingBar ratingBar     = view.findViewById(R.id.rating_delivery_boy_rating);

        name.setText(new PrefEditor(context).getString(Json_Shared_Preference.DELIVERY_BOY_NAME));
        phone_no.setText(new PrefEditor(context).getString(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO));
        Picasso.get().load(UrlValues.SERVER+new PrefEditor(context).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE)).centerCrop().fit().into(del_boy_img);

        //Log.e("IMAGE__",""+UrlValues.SERVER+new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE));

        builder.setCancelable(true);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, ""+ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                //dialog.hide();
            }
        });



    }

}
