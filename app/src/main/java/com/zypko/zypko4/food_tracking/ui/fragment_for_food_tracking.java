package com.zypko.zypko4.food_tracking.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.VolleyError;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.food_tracking.util.Bottom_Sheet_Utils;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class fragment_for_food_tracking extends Fragment implements View.OnKeyListener,SwipeRefreshLayout.OnRefreshListener {

    View root;
    LinearLayout linearLayout;
    ImageView imageView_qr_code;

    TextView textView_call_delivery_boy, tv_order_table_id;

    ImageView img_accepted, img_cocking, img_packing, img_picked_up, img_delivered;

    String FRAGMENT = "PAYMENT_ACTIVITY";
    int order_table_id;
    String[] food_tracking_details;

    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView_order_accepted,lottieAnimationView_order_cocked,lottieAnimationView_order_packing,lottieAnimationView_order_picked_up,lottieAnimationView_order_delivered;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_food_tracking, container, false);
            ///*************************************************////

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            ///*************************************************////

            initialise();
        }

        return root;
    }

    private void initialise() {

        Bundle bundle = getArguments();

        if (bundle != null) {

            order_table_id = bundle.getInt("ORDER_TABLE_ID");
            FRAGMENT = bundle.getString("FRAGMENT");

        }

        linearLayout = root.findViewById(R.id.bottom_Sheet);
        textView_call_delivery_boy = root.findViewById(R.id.textview_call_delivery_boy);
        imageView_qr_code = root.findViewById(R.id.image_view_user_qr_code);
        tv_order_table_id = root.findViewById(R.id.tv_order_table_id);

        img_accepted = root.findViewById(R.id.imageview_order_accepted);
        img_cocking = root.findViewById(R.id.imageview_cocking);
        img_packing = root.findViewById(R.id.imageview_packing);
        img_picked_up = root.findViewById(R.id.imageview_picked_up);
        img_delivered = root.findViewById(R.id.imageview_delivered);

        swipeRefreshLayout      = root.findViewById(R.id.SwipeRefreshLayout_tracking_page);

        lottieAnimationView_order_accepted  = root.findViewById(R.id.lottie_animation_tracking_order_accepted);
        lottieAnimationView_order_cocked    = root.findViewById(R.id.lottie_animation_tracking_order_cocked);
        lottieAnimationView_order_packing   = root.findViewById(R.id.lottie_animation_tracking_packing);
        lottieAnimationView_order_picked_up = root.findViewById(R.id.lottie_animation_tracking_order_picked_up);
        lottieAnimationView_order_delivered = root.findViewById(R.id.lottie_animation_tracking_delivered);

        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorBlue_tez_dark));
        swipeRefreshLayout.setOnRefreshListener(this);
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        tv_order_table_id.setText("" + order_table_id);

        textView_call_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment_for_delivery_boy_details_bottom_sheet fragment_bottom_sheet = new fragment_for_delivery_boy_details_bottom_sheet();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("ORDER_TABLE_ID", order_table_id);
                fragment_bottom_sheet.setArguments(bundle1);

                fragment_bottom_sheet.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        Toolbar toolbar = root.findViewById(R.id.toolbar_food_tracking);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Track your Food");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        set_up();

    }

    private void set_up() {

        get_food_tracking_from_server(order_table_id);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix;

        try {
            bitMatrix = multiFormatWriter.encode(String.valueOf(new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID)), BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView_qr_code.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void get_food_tracking_from_server(int order_table_id) {

        String url = UrlValues.GET_FOOD_TRACKING_DETAILS + "?ORDER_TABLE_ID=" + order_table_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                Log.e("OBJECT a", "" + object);

                try {
                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 1) {
                        food_tracking_details = new Bottom_Sheet_Utils(getActivity()).Get_food_tracking_details(object);
                        set_right_images();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("ERROR_TRACKING", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void set_right_images() {


        if (food_tracking_details.length != 0) {

            if (food_tracking_details[0].equals("y")) {
                lottieAnimationView_order_accepted.cancelAnimation();
                lottieAnimationView_order_accepted.setVisibility(View.GONE);
                img_accepted.setVisibility(View.VISIBLE);
            }
            if (food_tracking_details[1].equals("y")) {
                lottieAnimationView_order_cocked.cancelAnimation();
                lottieAnimationView_order_cocked.setVisibility(View.GONE);
                img_cocking.setVisibility(View.VISIBLE);
            }
            if (food_tracking_details[2].equals("y")) {
                lottieAnimationView_order_packing.cancelAnimation();
                lottieAnimationView_order_packing.setVisibility(View.GONE);
                img_packing.setVisibility(View.VISIBLE);
            }
            if (food_tracking_details[3].equals("y")) {
                lottieAnimationView_order_picked_up.cancelAnimation();
                lottieAnimationView_order_picked_up.setVisibility(View.GONE);
                img_picked_up.setVisibility(View.VISIBLE);
                textView_call_delivery_boy.setVisibility(View.VISIBLE);
            }
            if (food_tracking_details[4].equals("y")) {
                lottieAnimationView_order_delivered.cancelAnimation();
                lottieAnimationView_order_delivered.setVisibility(View.GONE);
                img_delivered.setVisibility(View.VISIBLE);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.DELIVERED,"y");
            }
        }

    }

    private void rating_of_delivery_boy() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.layout_for_rating, null);

        TextView name = view.findViewById(R.id.tv_name_delivery_boy_rating);
        TextView phone_no = view.findViewById(R.id.tv_phone_no_delivery_boy_rating);
        final EditText comment = view.findViewById(R.id.et_comment_delivery_boy_rating);
        ImageView del_boy_img = view.findViewById(R.id.imageView_delivery_boy_rating);
        final Button btn_submit = view.findViewById(R.id.btn_submit_delivery_boy_rating);
        final RatingBar ratingBar = view.findViewById(R.id.rating_delivery_boy_rating);

        name.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_NAME));
        phone_no.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO));
        Picasso.get().load(UrlValues.SERVER + new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE)).centerCrop().fit().into(del_boy_img);

        //Log.e("IMAGE__",""+UrlValues.SERVER+new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE));

        builder.setCancelable(true);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getActivity(), "" + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                //dialog.hide();
            }
        });


    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
            }
        }
        return true;
    }


    private void onBackPressed() {

        if (FRAGMENT.equals("USER_ACTIVITY")) {

            getActivity().onBackPressed();
            getActivity().finish();
            Log.e("FINISH","FINISH1");
        } else {
            Intent intent = new Intent(getActivity(), RandomHotelActivity.class);
            startActivity(intent);
            getActivity().finish();
            Log.e("FINISH","FINISH");
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }

    @Override
    public void onDestroyView() {
        if (getView() != null) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            parent.removeAllViews();
        }
        super.onDestroyView();
    }


    @Override
    public void onRefresh() {

        set_up();
    }
}
