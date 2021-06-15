package com.zypko.zypko4.food_tracking.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.food_tracking.util.Bottom_Sheet;
import com.zypko.zypko4.food_tracking.util.Bottom_Sheet_Utils;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class fragment_for_delivery_boy_details_bottom_sheet extends BottomSheetDialogFragment {

    View root;
    ImageView img_delivery_boy,img_call;
    TextView tv_delivery_boy_name,tv_delivery_boy_rating,tv_train_no,tv_train_name,tv_coach,tv_seat_no,tv_delivery_boy_phone_no;
    LinearLayout linear_layout_on_station,linear_layout_on_train;

    ArrayList<Bottom_Sheet> db_ArrayList;
    int order_table_id;
    ShimmerLayout shimmerLayout;
    RelativeLayout relativeLayout;
    int request_call = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.layout_for_delivery_boy_details,container,false);

            initialise();
        }

        return root;
    }

    private void initialise() {

        img_delivery_boy            = root.findViewById(R.id.imageView_bottom_sheet_delivery_boy);
        img_call                    = root.findViewById(R.id.imageView_bottom_sheet_delivery_boy_call);

        tv_delivery_boy_name        = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_name);
        tv_delivery_boy_phone_no    = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_phone_no);
        tv_delivery_boy_rating      = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_rating);
        tv_train_name               = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_train_name);
        tv_train_no                 = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_train_no);
        tv_coach                    = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_coach);
        tv_seat_no                  = root.findViewById(R.id.textView_bottom_sheet_delivery_boy_seat_no);
        linear_layout_on_station    = root.findViewById(R.id.linear_layout_on_station);
        linear_layout_on_train      = root.findViewById(R.id.linear_layout_on_train);


        shimmerLayout               = root.findViewById(R.id.shimmer_layout_delivery_boy_details);
        relativeLayout              = root.findViewById(R.id.relative_layout_delivery_boy_details);

        set_up();
    }

    private void set_up() {

        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmerAnimation();
        relativeLayout.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        if (bundle != null) {
            order_table_id = bundle.getInt("ORDER_TABLE_ID");
        }

        String url = UrlValues.BOTTOM_SHEET_DELIVERY_BOY_AND_ADDRESS+"?ORDER_TABLE_ID="+order_table_id;

        JSONProvider provider = new JSONProvider(getActivity());

        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                db_ArrayList = new Bottom_Sheet_Utils(getActivity()).Get_details_of_delivery_boy_and_address(object);

                set_bottom_sheet();

            }

            @Override
            public void onError(VolleyError error) {

                //Toast.makeText(getActivity(), "on error", Toast.LENGTH_SHORT).show();
            }
        });


        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                make_call();
            }
        });

    }

    private void set_bottom_sheet() {

        if (db_ArrayList != null) {

            Picasso.get().load(UrlValues.SERVER+db_ArrayList.get(0).getDelivery_boy_image()).fit().centerCrop().into(img_delivery_boy);

            tv_delivery_boy_name        .setText(db_ArrayList.get(0).getDelivery_boy_name());
            tv_delivery_boy_phone_no    .setText(db_ArrayList.get(0).getDelivery_boy_phone_no());
            tv_delivery_boy_rating      .setText(db_ArrayList.get(0).getDelivery_boy_rating());

            if (db_ArrayList.get(0).getTrain_number().equals("00000")) {

                linear_layout_on_station.setVisibility(View.VISIBLE);
                linear_layout_on_train.setVisibility(View.GONE);

            }else {
                tv_train_name               .setText(db_ArrayList.get(0).getTrain_name());
                tv_train_no                 .setText(db_ArrayList.get(0).getTrain_number());
                tv_coach                    .setText(db_ArrayList.get(0).getCoach());
                tv_seat_no                  .setText(db_ArrayList.get(0).getSeat_no());
            }

            relativeLayout.setVisibility(View.VISIBLE);
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);


        }else {
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

    }


    private void make_call() {
        String number = ""+tv_delivery_boy_phone_no.getText().toString();

        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE },request_call);
        }else {
            String dial = "tel:"+number;
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == request_call){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                make_call();

            }else {
                Toast.makeText(getActivity(), "You can't access this feature", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }
}
