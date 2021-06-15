package com.zypko.zypko4.cart.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.HotelClickedActivity;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.cart.util.Adapter_for_selected_food_items;
import com.zypko.zypko4.cart.util.Hotel_Details_in_Cart_Utils;
import com.zypko.zypko4.cart.util.Seter_Geter;
import com.zypko.zypko4.clicked_hotel.ui.fragment_for_clicked_hotel;
import com.zypko.zypko4.clicked_hotel.util.Food_Sqlite;
import com.zypko.zypko4.clicked_hotel.util.Hotel;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.order_details.ui.fragment_for_pnr_coach_and_seat_no;
import com.zypko.zypko4.payment.ui.fragment_for_payment;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;

import org.json.JSONObject;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class fragment_for_Cart extends Fragment implements Adapter_for_selected_food_items.ClickListener{
    
    View root;
    ArrayList<Seter_Geter> arrayList;
    Database myDB;

    RecyclerView recyclerView_for_selected_items;
    RecyclerView.Adapter adapter_for_selected_items;
    RecyclerView.LayoutManager layoutManager_for_selected_items;

    TextView textView_proceed_to_pay,textView_subtotal,textView_delivery_charge,textView_water_bottle,textView_total_to_pay,textView_total_amount_below,tv_packaging_charge,tv_food_taxes;
    int hotel_id;
    CheckBox check_box_for_water_bottle;
    ArrayList<Hotel> hotel_ArrayList;
    ArrayList<Food_Sqlite> cart_food_details_ArrayList;

    TextView textView_hotel_name,textView_hotel_address;
    ImageView imageView_hotel_image;
    ShimmerLayout shimmerLayout;
    LinearLayout linearLayout;

    String water_bottle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_cart,container,false);
            
            initialise();
        }
        
        return root;
    }

    private void initialise() {

        myDB = new Database(getActivity());
        Bundle bundle = getArguments();

        if (bundle != null) {
            hotel_id = bundle.getInt("HOTEL_ID");
        }

        shimmerLayout               = root.findViewById(R.id.shimmer_layout_cart);
        linearLayout                = root.findViewById(R.id.Linear_layout_cart);

        textView_proceed_to_pay     = root.findViewById(R.id.text_view_proceed_to_pay);
        textView_hotel_name         = root.findViewById(R.id.textView_cart_hotel_name);
        textView_hotel_address      = root.findViewById(R.id.textView_cart_hotel_address);
        imageView_hotel_image       = root.findViewById(R.id.imageView_cart_hotel_photo);

        textView_delivery_charge    = root.findViewById(R.id.textView_cart_delivery_charge);
        textView_subtotal           = root.findViewById(R.id.textView_cart_subtotal);
        textView_water_bottle       = root.findViewById(R.id.textView_cart_water_bottle_price);
        textView_total_to_pay       = root.findViewById(R.id.textView_cart_total_amount);
        textView_total_amount_below = root.findViewById(R.id.textView_cart_total_food_price_to_pay);
        check_box_for_water_bottle  = root.findViewById(R.id.check_box_cart_water_bottle);
        tv_packaging_charge         = root.findViewById(R.id.textView_cart_packaging_charge);
        tv_food_taxes               = root.findViewById(R.id.textView_cart_food_taxes);

        recyclerView_for_selected_items     = root.findViewById(R.id.recycler_view_for_selected_items);
        cart_food_details_ArrayList         = myDB.get_food_details_in_cart();

        adapter_for_selected_items          = new Adapter_for_selected_food_items(getActivity(), cart_food_details_ArrayList);
        ((Adapter_for_selected_food_items)  adapter_for_selected_items).setClickListener(this);
        layoutManager_for_selected_items    = new LinearLayoutManager(getActivity());
        recyclerView_for_selected_items     .setLayoutManager(layoutManager_for_selected_items);
        recyclerView_for_selected_items     .setAdapter(adapter_for_selected_items);

        shimmerLayout.startShimmerAnimation();
        set_up();

    }

    private void set_up() {

        String url = UrlValues.GET_HOTEL_DETAILS + "?HOTEL_ID=" + hotel_id;

        Log.e("URL",url);

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                hotel_ArrayList = new Hotel_Details_in_Cart_Utils().Get_Hotel_Details(object);

                textView_hotel_name.setClickable(true);
                textView_hotel_address.setClickable(true);
                imageView_hotel_image.setClickable(true);

                set_view();
            }

            @Override
            public void onError(VolleyError error) {
                //Toast.makeText(getActivity(), "OnError get", Toast.LENGTH_SHORT).show();
                set_view();
            }
        });


        ////********************************************************************************************************************************************//

        // hotel clicked
        textView_hotel_name.setClickable(false);
        textView_hotel_address.setClickable(false);
        imageView_hotel_image.setClickable(false);
        textView_hotel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hotel_clicked();

            }
        });

        textView_hotel_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotel_clicked();
            }
        });

        imageView_hotel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotel_clicked();
            }
        });

        ////********************************************************************************************************************************************//

        textView_proceed_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new FragmentOpener(getActivity()).setManager(getActivity().getSupportFragmentManager()).open_Replace_Backstack(new fragment_for_food_tracking());

                water_bottle = "n";

                if (check_box_for_water_bottle.isChecked()) {

                    water_bottle = "y";
                }

                final Bundle bundle = new Bundle();
                bundle.putString("WATER_BOTTLE",    water_bottle);
                bundle.putString("DELIVERY_CHARGE", ""+textView_delivery_charge.getText().toString());


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getLayoutInflater().inflate(R.layout.dialog_box_for_asking_on_station_or_on_train,null);
                builder.setCancelable(true);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button button = view.findViewById(R.id.dialog_box_asking_station_train_button);
                final RadioGroup radioGroup = view.findViewById(R.id.Radio_group_asking_station_or_train);
                final RadioButton on_train = view.findViewById(R.id.radio_button_On_train);
                final RadioButton on_station = view.findViewById(R.id.radio_button_on_station);




                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_On_train) {

                            new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_pnr_coach_and_seat_no(),bundle);
                            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.ON_STATION,"n");

                        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_on_station) {

                            Bundle bundle1 = new Bundle();
                            bundle1.putString("FRAGMENT",        "CART");
                            bundle1.putString("PNR",             "N/A");
                            bundle1.putString("TRAIN_NO",        "0");
                            bundle1.putString("COACH",           "N/A");
                            bundle1.putString("SEAT_NO",         "00");
                            bundle1.putString("WATER_BOTTLE",    ""+water_bottle);
                            bundle1.putString("DELIVERY_CHARGE", ""+textView_delivery_charge.getText().toString());

                            new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_payment(),bundle1);
                            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.ON_STATION,"y");
                        }

                        dialog.hide();
                    }
                });

            }
        });

        check_box_for_water_bottle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    textView_water_bottle.setText("15");
                }else {
                    textView_water_bottle.setText("0");
                }

                onResume();
            }
        });

    }

    private void hotel_clicked() {

        if (hotel_ArrayList != null) {

            String hotel_name = hotel_ArrayList.get(0).getHotel_name();
            int hotel_id = myDB.get_hotel_id_of_cart_items();

            Intent intent = new Intent(getActivity(),HotelClickedActivity.class);
            intent.putExtra("HOTEL_NAME"    ,hotel_name);
            intent.putExtra("HOTEL_ID"      ,""+hotel_id);
            intent.putExtra("FRAGMENT"      ,"CART");
            intent.putExtra("STATION_NAME"  ,""+new PrefEditor(getActivity()).getString(Json_Shared_Preference.STATION_NAME));
            startActivity(intent);

        }
    }

    private void set_view() {

        if (hotel_ArrayList != null) {

            textView_hotel_name.setText(hotel_ArrayList.get(0).getHotel_name());
            textView_hotel_address.setText(hotel_ArrayList.get(0).getHotel_address());

            String url = UrlValues.HOTEL_IMAGE_URL
                    + hotel_ArrayList.get(0).getHotel_station() + "/" + hotel_ArrayList.get(0).getHotel_name()
                    + "/" +hotel_ArrayList.get(0).getHotel_id() + ".jpg";

            Picasso.get().load(url).fit().centerCrop().into(imageView_hotel_image);

            //linearLayout.setVisibility(View.VISIBLE);
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);

        }
    }


    @Override
    public void itemClicked(View view, int position) {

       TextView total_price_of_individual_food = view.findViewById(R.id.textView_total_individual_food_amount);

        total_price_of_individual_food.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int[] total_price = myDB.get_total_items_and_total_amount();
                int[] packaging_and_food_taxes = myDB.get_total_packaging_charge_and_total_food_taxes();
                int total_amount = total_price[1] + Integer.parseInt(textView_water_bottle.getText().toString()) + packaging_and_food_taxes[0] + packaging_and_food_taxes[1];

                textView_subtotal.setText(""+total_price[1]);

                if (total_amount >= 100 || total_amount ==0) {
                    textView_delivery_charge.setText("0");
                }else {
                    textView_delivery_charge.setText("15");
                }

                int amount = total_price[1] + Integer.parseInt(textView_delivery_charge.getText().toString()) + Integer.parseInt(textView_water_bottle.getText().toString()) + packaging_and_food_taxes[0] + packaging_and_food_taxes[1];
                textView_total_to_pay.setText(""+amount);
                textView_total_amount_below.setText(""+amount);

                tv_packaging_charge.setText(""+packaging_and_food_taxes[0]);
                tv_food_taxes.setText(""+packaging_and_food_taxes[1]);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }

    @Override
    public void onResume() {
        super.onResume();

        cart_food_details_ArrayList = myDB.get_food_details_in_cart();
        if (cart_food_details_ArrayList.size() == 0) {
            startActivity(new Intent(getActivity(),RandomHotelActivity.class));
            getActivity().finish();
        }

        if (check_box_for_water_bottle.isChecked()){
            textView_water_bottle.setText("15");
        }else {
            textView_water_bottle.setText("0");
        }

        int[] total_price = myDB.get_total_items_and_total_amount();

        textView_subtotal.setText(""+total_price[1]);

        int[] packaging_and_food_taxes = myDB.get_total_packaging_charge_and_total_food_taxes();
        int total_amount = total_price[1] + Integer.parseInt(textView_water_bottle.getText().toString()) + packaging_and_food_taxes[0] + packaging_and_food_taxes[1];

        if (total_amount >= 100 || total_amount == 0) {
            textView_delivery_charge.setText("0");
        }else {
            textView_delivery_charge.setText("15");
        }

        int amount = total_price[1] + Integer.parseInt(textView_delivery_charge.getText().toString()) + Integer.parseInt(textView_water_bottle.getText().toString()) + packaging_and_food_taxes[0] + packaging_and_food_taxes[1];
        textView_total_to_pay.setText(""+amount);
        textView_total_amount_below.setText(""+amount);


        tv_packaging_charge.setText(""+packaging_and_food_taxes[0]);
        tv_food_taxes.setText(""+packaging_and_food_taxes[1]);


    }
}
