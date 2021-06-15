package com.zypko.zypko4.payment.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.xw.repo.BubbleSeekBar;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.LoginActivity;
import com.zypko.zypko4.activity.ui.SuccessAnimationActivity;
import com.zypko.zypko4.clicked_hotel.util.Food_Sqlite;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.json_Objects.Paytm_Constants;
import com.zypko.zypko4.payment.util.BroadCast_Receiver_For_Alarm_Manager;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class fragment_for_payment extends Fragment {

    View root;
    BubbleSeekBar seekBar;

    TextView tv_online_payment, tv_COD, tv_total_amount, tv_payable_amount, tv_rest_payable_amount, tv_payment_percent_paid;
    Button btn_proceed_to_pay;
    ProgressBar progressBar;

    LinearLayout linearLayout_online_payment, linearLayout_COD, linearLayout_payable_amount, linearLayout_rest_payable_amount;

    int total_discount, total_amount, station_id, delivery_charge;
    int order_table_id, final_amount = 0;
    String pnr, train_no, coach, seat_no, water_bottle;

    Database myDB;
    ArrayList<Food_Sqlite> arrayList;

    boolean available_pnr = true;
    boolean access = false;
    Bundle bundle;
    int request_call = 1;

    ////////************************************///
    String CHECKSUM;
    String FRAGMENT;

    String act_arr=null,act_dep,sch_arr,act_arr_date,sch_dep,late_min;

    boolean running_thread = true;
    int distance = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_payment, container, false);

            initialise();

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        return root;
    }

    private void initialise() {

        myDB = new Database(getActivity());

        Toolbar toolbar = root.findViewById(R.id.toolbar_payment);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Payment options");
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.colorBlue_tez));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        seekBar                 = root.findViewById(R.id.payment_seek_bar);
        tv_online_payment       = root.findViewById(R.id.tv_payment_online_payment);
        tv_COD                  = root.findViewById(R.id.tv_payment_COD);
        tv_payable_amount       = root.findViewById(R.id.tv_payment_payable_amount);
        tv_rest_payable_amount  = root.findViewById(R.id.tv_payment_rest_amount);
        tv_total_amount         = root.findViewById(R.id.tv_payment_total_amount);
        btn_proceed_to_pay      = root.findViewById(R.id.btn_payment_proceed_to_pay);
        progressBar             = root.findViewById(R.id.payment_progress_bar);
        tv_payment_percent_paid = root.findViewById(R.id.tv_payment_percent_paid);

        linearLayout_online_payment         = root.findViewById(R.id.linearLayout_online_payment);
        linearLayout_COD                    = root.findViewById(R.id.linearLayout_COD);
        linearLayout_payable_amount         = root.findViewById(R.id.linear_layout_payable_amount);
        linearLayout_rest_payable_amount    = root.findViewById(R.id.linear_layout_rest_payable_amount);


        //***************************************** Bundle **************************************///

        station_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID);

        bundle = getArguments();

        if (bundle != null) {

            FRAGMENT        = bundle.getString("FRAGMENT");

            if (FRAGMENT.equals("LOGIN") || FRAGMENT.equals("VERIFY_OTP") || FRAGMENT.equals("NEW_PASSWORD")) {

                pnr             = new PrefEditor(getActivity()).getString(Json_Shared_Preference.PNR);
                train_no        = new PrefEditor(getActivity()).getString(Json_Shared_Preference.TRAIN_NO);
                coach           = new PrefEditor(getActivity()).getString(Json_Shared_Preference.COACH);
                seat_no         = new PrefEditor(getActivity()).getString(Json_Shared_Preference.SEAT_NO);
                water_bottle    = new PrefEditor(getActivity()).getString(Json_Shared_Preference.WATER_BOTTLE);
                delivery_charge = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.DELIVERY_CHARGE);


            }else {
                pnr             = bundle.getString("PNR");
                train_no        = bundle.getString("TRAIN_NO");
                coach           = bundle.getString("COACH");
                seat_no         = bundle.getString("SEAT_NO");
                water_bottle    = bundle.getString("WATER_BOTTLE");
                delivery_charge = Integer.parseInt(bundle.getString("DELIVERY_CHARGE"));
            }


            if (pnr.length() != 10) {

                available_pnr = false;
            }
        }

        set_up();
    }

    private void set_up() {

        tv_COD.setAlpha((float) 0.5);

        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

                int amount = (total_amount * progress) / 100;
                tv_payable_amount.setText(String.valueOf(amount));
                tv_rest_payable_amount.setText(String.valueOf(total_amount - amount));
                tv_payment_percent_paid.setText(String.valueOf(progress));

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });


        // Button pressed
        btn_proceed_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID) != 0) && (!new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_NAME).isEmpty())) {

                    proceed_button_clicked();
                } else {
                    log_in_alert_dialog();
                }


            }
        });


        linearLayout_COD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (available_pnr) {

                    tv_COD.setAlpha(1);
                    tv_online_payment.setAlpha((float) 0.5);
                    seekBar.setAlpha((float) 0.5);

                    linearLayout_payable_amount.setVisibility(View.GONE);
                    linearLayout_rest_payable_amount.setVisibility(View.GONE);


            }
        });

        linearLayout_online_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_COD.setAlpha((float) 0.5);
                tv_online_payment.setAlpha(1);
                seekBar.setAlpha(1);

                linearLayout_payable_amount.setVisibility(View.VISIBLE);
                linearLayout_rest_payable_amount.setVisibility(View.VISIBLE);

            }
        });

        get_values();

    }

    private void log_in_alert_dialog() {

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        } else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Not Logged in !!");
        builder.setMessage("You are not Logged in. Please Log-in/Sign-up to proceed.");
        builder.setCancelable(true);

        builder.setPositiveButton("Log-in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.PNR,pnr);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.TRAIN_NO,train_no);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.COACH,coach);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.SEAT_NO,seat_no);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.WATER_BOTTLE,water_bottle);
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.DELIVERY_CHARGE,delivery_charge);


                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("FRAGMENT","PAYMENT");
                startActivity(intent);
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));

    }

    private void proceed_button_clicked() {

        btn_proceed_to_pay.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if (train_no.equals("0")) {
            access = true;
        } else {
            check_train_status();

        }

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (access) {
                    if (running_thread) {
                        //dialog_for_order_cant_place("Message","We are not fully functional yet. You can have ZYPKO delivering your food on your seat from april.");
                        set_order_details();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    btn_proceed_to_pay.setVisibility(View.VISIBLE);
                }
            }
        };

        handler.postDelayed(runnable, 5000);
    }


    private void get_values() {

        arrayList = myDB.get_total_discount_and_total_amount();
        int[] discount_per = new int[arrayList.size()];
        int[] packaging_and_food_taxes = myDB.get_total_packaging_charge_and_total_food_taxes();

        for (int i = 0; i < arrayList.size(); i++) {

            discount_per[i] = arrayList.get(i).getFood_discount();
        }

        for (int i = 0; i < arrayList.size(); i++) {

            total_discount = total_discount + (discount_per[i] / 100) * arrayList.get(i).getFood_total_price();
            total_amount = total_amount + arrayList.get(i).getFood_total_price();
        }

        if (water_bottle.equals("y")) {
            total_amount = total_amount + 15;
        }

        total_amount = total_amount + delivery_charge + packaging_and_food_taxes[0] + packaging_and_food_taxes[1];


        tv_total_amount.setText(String.valueOf(total_amount));
        tv_payable_amount.setText(String.valueOf(total_amount));
        tv_rest_payable_amount.setText("0");


    }

    private void set_order_details() {

        int[] food_ids = myDB.get_food_id_of_cart_items();
        Gson gson = new Gson();
        String array_food_id = gson.toJson(food_ids);

        int[] food_total_items = new int[arrayList.size()];

        for (int i = 0; i < arrayList.size(); i++) {

            food_total_items[i] = arrayList.get(i).gettotal_items();
        }

        String food_count = gson.toJson(food_total_items);

        int hotel_id = myDB.get_hotel_id_of_cart_items();
        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);


        String payment = null;
        // for determining cod or online
        if (tv_COD.getAlpha() == 1) {

            final_amount = Integer.parseInt(tv_total_amount.getText().toString());
            payment = "c";

        } else if (tv_online_payment.getAlpha() == 1) {

            if (seekBar.getProgress() == 100) {
                final_amount = Integer.parseInt(tv_total_amount.getText().toString());
                payment = "o";
            } else {
                final_amount = Integer.parseInt(tv_total_amount.getText().toString());
                payment = "p";
            }


        }

        String url = UrlValues.GET_CART_DETAILS;

        Map<String, String> map = new HashMap();

        map.put("ARRAY_FOOD_ID"     , array_food_id);
        map.put("USER_ID"           , "" + user_id);
        map.put("HOTEL_ID"          , "" + hotel_id);
        map.put("PNR"               , "" + pnr);
        map.put("TRAIN_NO"          , "" + train_no);
        map.put("COACH"             , "" + coach);
        map.put("SEAT_NO"           , "" + seat_no);
        map.put("TOTAL_DISCOUNT"    , "" + total_discount);
        map.put("TOTAL_AMOUNT"      , "" + final_amount);
        map.put("ON_STATION"        , "" + new PrefEditor(getActivity()).getString(Json_Shared_Preference.ON_STATION));
        map.put("FOOD_COUNT"        , food_count);
        map.put("STATION_ID"        , "" + new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID));
        map.put("WATER_BOTTLE"      , "" + water_bottle);
        map.put("PAYMENT"           , "" + payment);
        map.put("PERCENT_PAID"      , "" + seekBar.getProgress());
        map.put("TRAIN_ARRIVAL_TIME", "" + act_arr);
        map.put("DISTANCE"          , "" + distance);

//        Log.e("OBJECT_STRING", ""+act_arr);
//        Log.e("OBJECT_STRING", ""+distance);


        Post_to_Server pst = new Post_to_Server(getActivity(), map);
        pst.getJson(url, new Post_Call_Order());

    }

    class Post_Call_Order implements HTTP_Post_Callback {

        @Override
        public void onSuccess(String string) {
            Log.e("OBJECT_STRING", string);

            try {
                JSONObject object = new JSONObject(string);

                int success = object.getInt(Json_Objects.SUCCESS);

                if (success == 1) {

                    order_table_id = object.getInt(Json_Objects.CONTENT);
                    new PrefEditor(getActivity()).writeData(Json_Shared_Preference.ORDER_TABLE_ID, order_table_id);

                    if (tv_COD.getAlpha() == 1) {
                        go_to_tracking_page();
                    }else {
                        Generate_checksum();

                    }
                    // for test purpose

                }else if (success == 0){

                    dialog_for_order_cant_place("Something is Wrong !!","Order Can't be placed right now");

                    progressBar.setVisibility(View.GONE);
                    btn_proceed_to_pay.setVisibility(View.VISIBLE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                btn_proceed_to_pay.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(VolleyError error) {
            progressBar.setVisibility(View.GONE);
            btn_proceed_to_pay.setVisibility(View.VISIBLE);

        }
    }


    private void check_train_status() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = new Date();

        String url = "https://api.railwayapi.com/v2/live/train/" + train_no + "/station/GHY/date/" + dateFormat.format(date) + "/apikey/" + getString(R.string.my_api_key) + "/";

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    int response_code = object.getInt("response_code");
                    Log.d("EVERY_THING", "" + response_code);

                    if (response_code == 200) {

                        JSONObject obj = object.getJSONObject("status");
                        String position = object.getString("position");

                        boolean has_departed = obj.getBoolean("has_departed");
                        act_dep      = obj.getString("actdep");
                        sch_arr      = obj.getString("scharr");
                        act_arr_date = obj.getString("actarr_date");
                        sch_dep      = obj.getString("schdep");
                        act_arr      = obj.getString("actarr");
                        late_min     = obj.getString("latemin");

                        if (act_arr.equals("Source") || late_min == null) {

                            dialog_for_order_cant_place("Sorry !!", "Your train has not started yet. Please order through On Station.");

                        }else {

                            if (!has_departed) {

                                Log.d("EVERY_THING", "" + position);
                                String[] dis_str = position.split("\\.");

                                String dis = dis_str[1].substring(0, dis_str[1].length() - 3);
                                distance = Integer.parseInt(dis.trim());

                                Log.d("EVERY_THING", "d>" + distance);

                                if (distance >= 55) {
                                    access = true;
                                } else {
                                    access = false;
                                    dialog_for_order_cant_place("Order Can't Placed !!", "You have to order before 1 hour.");
                                }
                            } else {
                                dialog_for_order_cant_place("Sorry !!", "Your train has departed");
                            }
                        }
                    }else{
                        Toast.makeText(getActivity(), "Something is wrong .!.!.!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.d("EVERY_THING", "" + e);
                    e.printStackTrace();
                    can_not_take_order("Error !!", "There is some technical issue going on. You can place order by calling this number : 6002015766");
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.d("EVERY_THING", "" + error);
                can_not_take_order("Error !!", "There is some technical issue going on. You can place order by calling this number : 6002015766");
            }
        });
    }

    private void dialog_for_order_cant_place(String title, String message) {

        btn_proceed_to_pay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        } else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("" + title);
        builder.setMessage("" + message);
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));

    }

    private void can_not_take_order(String title, String message) {

        btn_proceed_to_pay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        } else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Copy the number to call");
        builder.setMessage("Call +916002015766 this number for Order Your food");
        builder.setCancelable(true);

        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Number", "+916002015766");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity(), "Number copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));

    }


    ///**************************************** PAYTM Gateway *****************************************************////

    private void Generate_checksum() {

        String url = "https://zypkofoods.com/ZYPKO/payment_gateway/paytm/generateChecksum.php";

        String phone_no = new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_PHONE_NO);
        String email = new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_EMAIL);

        int paid_amount = ((final_amount * seekBar.getProgress()) / 100);

        Map<String, String> map = new HashMap<>();

        map.put("MID"           , Paytm_Constants.MID);
        map.put("ORDER_ID"      , "ORDERID" + order_table_id);
        map.put("CUST_ID"       , "CUSTID" + order_table_id);
        map.put("MOBILE_NO"     , phone_no);
        map.put("EMAIL"         , email);
        map.put("CHANNEL_ID"    , Paytm_Constants.CHANNEL_ID);
        map.put("TXN_AMOUNT"    , "" + paid_amount);
        map.put("WEBSITE"       , Paytm_Constants.WEBSITE);
        map.put("CALLBACK_URL"  , Paytm_Constants.CALLBACK_URL);
        map.put("INDUSTRY_TYPE_ID", Paytm_Constants.INDUSTRY_TYPE_ID);

//        Log.e("PAYMENT",""+Paytm_Constants.MID);
//        Log.e("PAYMENT","ORDERID" + order_table_id);
//        Log.e("PAYMENT","CUSTID" + order_table_id);
//        Log.e("PAYMENT",""+phone_no);
//        Log.e("PAYMENT",""+email);
//        Log.e("PAYMENT",""+Paytm_Constants.INDUSTRY_TYPE_ID);
//        Log.e("PAYMENT",""+Paytm_Constants.CHANNEL_ID);
//        Log.e("PAYMENT",""+Paytm_Constants.WEBSITE);
//        Log.e("PAYMENT",""+Paytm_Constants.CALLBACK_URL);

        Post_to_Server pst = new Post_to_Server(getActivity(), map);
        pst.getJson(url, new Post_call());

    }


    private class Post_call implements HTTP_Post_Callback {
        @Override
        public void onSuccess(String string) {
            Log.e("CHECKSUM", "<<string>>" + string);

            try {
                JSONObject object = new JSONObject(string);

                CHECKSUM = object.getString("CHECKSUMHASH");
                Log.e("CHECKSUM", "" + CHECKSUM);

                if (CHECKSUM.trim().length() != 0) {
                    initializePaytmPayment();
                }

                progressBar.setVisibility(View.GONE);
                btn_proceed_to_pay.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("CHECKSUM", "<>" + e);

                progressBar.setVisibility(View.GONE);
                btn_proceed_to_pay.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(VolleyError error) {
            Log.e("CHECKSUM", "<<error>>" + error);

            progressBar.setVisibility(View.GONE);
            btn_proceed_to_pay.setVisibility(View.VISIBLE);
        }
    }


    private void initializePaytmPayment() {

        String phone_no = new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_PHONE_NO);
        String email = new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_EMAIL);

        //getting paytm service
        //PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        PaytmPGService Service = PaytmPGService.getProductionService();

        int paid_amount = ((final_amount * seekBar.getProgress()) / 100);

        //creating a hashmap and adding all the values required
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID"          , Paytm_Constants.MID);
        paramMap.put("ORDER_ID"     , "ORDERID" + order_table_id);
        paramMap.put("CUST_ID"      , "CUSTID" + order_table_id);
        paramMap.put("MOBILE_NO"    , phone_no );
        paramMap.put("EMAIL"        , email );
        paramMap.put("CHANNEL_ID"   , Paytm_Constants.CHANNEL_ID);
        paramMap.put("TXN_AMOUNT"   , "" + paid_amount);
        paramMap.put("WEBSITE"      , Paytm_Constants.WEBSITE);
        paramMap.put("CALLBACK_URL" , Paytm_Constants.CALLBACK_URL);
        paramMap.put("INDUSTRY_TYPE_ID", Paytm_Constants.INDUSTRY_TYPE_ID);
        paramMap.put("CHECKSUMHASH" , CHECKSUM);

//        Log.e("PAYMENT",""+Paytm_Constants.MID);
//        Log.e("PAYMENT","ORDERID" + order_table_id);
//        Log.e("PAYMENT","CUSTID" + order_table_id);
//        Log.e("PAYMENT",""+phone_no);
//        Log.e("PAYMENT",""+email);
//        Log.e("PAYMENT",""+Paytm_Constants.INDUSTRY_TYPE_ID);
//        Log.e("PAYMENT",""+Paytm_Constants.CHANNEL_ID);
//        Log.e("PAYMENT",""+Paytm_Constants.WEBSITE);
//        Log.e("PAYMENT",""+Paytm_Constants.CALLBACK_URL);
//        Log.e("PAYMENT",""+CHECKSUM);


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle bundle) {

                String response = bundle.getString("RESPCODE");
                String status   = bundle.getString("STATUS");

                Log.e("PAYMENT", "response >>" + response);
                Log.e("PAYMENT", "status : " + bundle.getString("STATUS"));


                if (response.equals("01") && status.equals("TXN_SUCCESS")) {
                    Transaction_successful();
                } else {
                    Unsuccessful_Transaction();
                }

                Log.e("PAYMENT", "" + bundle.toString());
            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }

            @Override
            public void someUIErrorOccurred(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }

            @Override
            public void onBackPressedCancelTransaction() {
                Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Toast.makeText(getActivity(), s + bundle.toString(), Toast.LENGTH_LONG).show();
                Unsuccessful_Transaction();

            }
        });

    }

    private void Transaction_successful() {

        int hotel_id = myDB.get_hotel_id_of_cart_items();

        String url = UrlValues.PAYMENT_SUCCESSFUL + "?ORDER_TABLE_ID=" + order_table_id + "&HOTEL_ID="+hotel_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                Toast.makeText(getActivity(), "Payment Successful", Toast.LENGTH_SHORT).show();
                go_to_tracking_page();


            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    private void go_to_tracking_page() {

        //int station_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID);

        //****************************************** call service to hit server for checking delivery boy ******************************************************************////
//        Intent intent = new Intent(getActivity(), Service_for_Set_Delivery_Boy.class);
//        intent.putExtra("ORDER_TABLE_ID", order_table_id);
//        intent.putExtra("STATION_ID", station_id);
//        Log.e("STATION_ID", "" + station_id);
//        getActivity().startService(intent);


        ///************************* call service for canceling order from hotel after 30 minutes if order is not accepted by hotel ***************************************////
//        Intent intent2 = new Intent(getActivity(), Service_for_Reject_Order_From_Hotel.class);
//        intent2.putExtra("ORDER_TABLE_ID", order_table_id);
//        getActivity().startService(intent2);

        //////******************************** food tracking page ************************************************************************************************////////////////////////
//        Intent intent3 = new Intent(getActivity(), TrackingActivity.class);
//        intent3.putExtra("ORDER_TABLE_ID", order_table_id);
//        intent3.putExtra("FRAGMENT", "PAYMENT_ACTIVITY");
//        startActivity(intent3);

//        Bundle bundle = new Bundle();
//        bundle.putInt("ORDER_TABLE_ID", order_table_id);
//        bundle.putString("FRAGMENT", "PAYMENT_ACTIVITY");

//        new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_success_animation(),bundle);

        Intent intent = new Intent(getActivity(),SuccessAnimationActivity.class);
        intent.putExtra("ORDER_TABLE_ID",order_table_id);
        intent.putExtra("FRAGMENT", "PAYMENT_ACTIVITY");
        startActivity(intent);

        if (distance != 0) {
            alarm_manager();
        }

        // Delete Cart items
        myDB.Clear_sqlite();
        getActivity().finish();

    }


    private void Unsuccessful_Transaction() {


        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        } else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Transaction Failed !!!");
        builder.setMessage("Do you want to try again ?");
        builder.setCancelable(false);

        builder.setPositiveButton("Retry Payment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressBar.setVisibility(View.VISIBLE);
                btn_proceed_to_pay.setVisibility(View.GONE);

                initializePaytmPayment();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressBar.setVisibility(View.GONE);
                btn_proceed_to_pay.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));


    }


    //***********************************  Alarm manager for update train arrival time ****************////
    private void alarm_manager(){

        long CURRENT_TIME = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(),BroadCast_Receiver_For_Alarm_Manager.class);
        intent.putExtra("TRAIN_NUMBER",""+train_no);
        intent.putExtra("ORDER_TABLE_ID",order_table_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),order_table_id,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,CURRENT_TIME+AlarmManager.INTERVAL_HOUR,AlarmManager.INTERVAL_HOUR,pendingIntent);

        Log.e("BROAD_CAST_RECEIVER","b <>"+train_no);
        Log.e("BROAD_CAST_RECEIVER","b <>"+order_table_id);

    }



    @Override
    public void onResume() {
        running_thread = true;
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onPause() {
        running_thread = false;
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

}
