package com.zypko.zypko4.item_clicked.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.ItemClickedActivity;
import com.zypko.zypko4.activity.ui.TrackingActivity;
import com.zypko.zypko4.cart.ui.fragment_for_Cart;
import com.zypko.zypko4.clicked_hotel.util.Food_Sqlite;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.hotel_category_clicked.util.Food_All_Details_utils;
import com.zypko.zypko4.item_clicked.util.Adapter_for_item_clicked_hotel_items;
import com.zypko.zypko4.item_clicked.util.Hotel_All_Food_Utils;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class fragment_item_clicked extends Fragment implements Adapter_for_item_clicked_hotel_items.ClickListener {

    View root;
    String food_name, food_id;

    ImageView imageView_of_food, imageView_veg;

    TextView textView_total_price, textView_food_price, textView_items_count, textView_view_cart, textView_food_category, textView_description, textView_hotel_name, textView_food_name, textView_rating;
    ImageView add_item, minus_item;
    TextView textView_track_items_of_individual_food;
    LinearLayout linearLayout_for_go_to_cart;
    Button btn_add_item;

    int track_individual_food_items = 0, food_made_amount = 0;

    ArrayList<Food_of_Clicked_Category> food_ArrayList,hotel_food_ArrayList;
    ArrayList<Food_Sqlite> food_from_sqlite_ArrayList, food_from_sqlite_ArrayList_for_all;

    Database myDB;
    ShimmerLayout shimmerLayout;
    LinearLayout linearLayout_itemClicked;
    RelativeLayout relativeLayout_above_image;
    TextView tv_above_image;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    // discount
    TextView tv_discount,tv_old_price,tv_under_recycler_view;
    RelativeLayout rel_layout_discount;
    LinearLayout linearLayout_old_price;
    Toolbar toolbar;

    boolean hotel_pressed = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_clicked_item, container, false);

            initialise();

        }

        return root;
    }

    private void initialise() {

        myDB = new Database(getActivity());

        imageView_of_food           = root.findViewById(R.id.imageView_of_item_clicked_food);
        imageView_veg               = root.findViewById(R.id.imageView_clicked_item_veg_or_non_veg);

        textView_food_name          = root.findViewById(R.id.textView_clicked_item_food_name);
        textView_hotel_name         = root.findViewById(R.id.textView_clicked_item_food_Hotel);
        textView_food_category      = root.findViewById(R.id.textView_clicked_item_food_category);
        textView_rating             = root.findViewById(R.id.textView_clicked_item_food_rating);
        textView_description        = root.findViewById(R.id.textView_clicked_item_food_description);
        textView_food_price         = root.findViewById(R.id.textView_clicked_item_price);
        tv_above_image              = root.findViewById(R.id.tv_text_above_clicked_item);
        relativeLayout_above_image  = root.findViewById(R.id.relative_layout_text_above_clicked_item);

        add_item                    = root.findViewById(R.id.imageView_add_item);
        minus_item                  = root.findViewById(R.id.imageView_minus_item);
        btn_add_item                = root.findViewById(R.id.btn_add_items);
        textView_track_items_of_individual_food     = root.findViewById(R.id.textView_track_items);

        textView_total_price        = root.findViewById(R.id.text_view_item_clicked_total_price);
        textView_items_count        = root.findViewById(R.id.text_view_item_clicked_items_count);
        textView_view_cart          = root.findViewById(R.id.text_view_item_clicked_view_cart);

        linearLayout_for_go_to_cart = root.findViewById(R.id.Linear_layout_for_go_to_cart);

        // recycler View for hotel items
        recyclerView                = root.findViewById(R.id.recycler_view_item_clicked);
        layoutManager               = new LinearLayoutManager(getActivity());
        recyclerView                .setLayoutManager(new GridLayoutManager(getActivity(),2));

        tv_under_recycler_view      = root.findViewById(R.id.tv_under_recycler_view);
        //for loading animation
        shimmerLayout               = root.findViewById(R.id.shimmer_layout_item_clicked);
        linearLayout_itemClicked = root.findViewById(R.id.linear_layout_item_clicked);

        // discount
        tv_discount                 = root.findViewById(R.id.tv_discount_percentage_item_clicked);
        rel_layout_discount         = root.findViewById(R.id.rel_layout_discount_item_clicked);
        linearLayout_old_price      = root.findViewById(R.id.linear_layout_old_price_item_click);
        tv_old_price                = root.findViewById(R.id.text_view_item_clicked_old_price);
        ////********************************************************************************************************************************************//


        add_item.setFocusable(true);
        minus_item.setFocusable(true);
        textView_view_cart.setFocusable(true);

        ////********************************************************************************************************************************************//

        shimmerLayout.startShimmerAnimation();
        linearLayout_itemClicked.setVisibility(View.GONE);

        set_up();

    }

    private void set_up() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            food_name = bundle.getString("FOOD_NAME");
            food_id = bundle.getString("FOOD_ID");
        }

        toolbar = root.findViewById(R.id.toolbar_item_clicked);
        toolbar.setTitle(" ");
        toolbar.setVisibility(View.GONE);

        final Drawable img_up = getResources().getDrawable(R.drawable.ic_arrow_up);
        final Drawable img_down = getResources().getDrawable(R.drawable.ic_arrow_below);

        textView_hotel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hotel_pressed) {
                    recyclerView.setVisibility(View.GONE);
                    tv_under_recycler_view.setVisibility(View.VISIBLE);
                    textView_hotel_name.setCompoundDrawablesWithIntrinsicBounds(null,null,img_down,null);
                    hotel_pressed = false;
                }else {
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_under_recycler_view.setVisibility(View.GONE);
                    textView_hotel_name.setCompoundDrawablesWithIntrinsicBounds(null,null,img_up,null);
                    hotel_pressed = true;
                }
            }
        });

        String url = UrlValues.CLICKED_FOOD_GET_ALL_DETAILS + "?FOOD_ID=" + food_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                food_ArrayList = new Food_All_Details_utils().Get_all_Food_Details(object);

                //have to check hotel status
                if (food_ArrayList != null) {

                    if (!food_ArrayList.get(0).getHotel_status().equals("o")) {

                        add_item.setVisibility(View.GONE);
                        minus_item.setVisibility(View.GONE);
                        textView_track_items_of_individual_food.setVisibility(View.GONE);
                        imageView_of_food.setAlpha(100);
                        relativeLayout_above_image.setVisibility(View.VISIBLE);
                        btn_add_item.setVisibility(View.GONE);

                    }
                }

                set_view();
            }

            @Override
            public void onError(VolleyError error) {
                set_view();
            }
        });

        textView_Clicks();

    }


    //***************************************************************************************************************////
    // functions

    private void textView_Clicks(){

        textView_view_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hotel_id = 0;

                if (food_ArrayList != null) {
                    hotel_id = food_ArrayList.get(0).getHotel_id();
                }

                Bundle bundle = new Bundle();
                bundle.putInt("HOTEL_ID",hotel_id);

                new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_Cart(),bundle);

            }
        });

        textView_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.show_Sqlite();
            }
        });

        ///*********************************************************************************************///
        // Calculations

        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_item_overall_function();

            }
        });

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_item_overall_function();
            }
        });

        minus_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (track_individual_food_items > 1) {
                    track_individual_food_items--;
                    textView_track_items_of_individual_food.setText("" + track_individual_food_items);

//                    food_made_amount = food_ArrayList.get(0).getFood_price() + food_ArrayList.get(0).getFood_taxes()
//                            + food_ArrayList.get(0).getFood_packing_charge() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
                    food_made_amount = food_ArrayList.get(0).getFood_price() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);

                    int individual_total_price = food_made_amount * track_individual_food_items;
                    int total_packaging_charge  = food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
                    int total_food_taxes        = food_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

                    myDB.check_selected_item(Integer.parseInt(food_id), food_name, food_ArrayList.get(0).getVeg(), food_ArrayList.get(0).getFood_price() ,food_made_amount, individual_total_price, track_individual_food_items, food_ArrayList.get(0).getFood_discount(), total_food_taxes, total_packaging_charge, food_ArrayList.get(0).getHotel_name(), food_ArrayList.get(0).getHotel_id());

                    int[] total_items_and_total_amount = myDB.get_total_items_and_total_amount();
                    textView_total_price.setText("" + total_items_and_total_amount[1]);
                    if (total_items_and_total_amount[0] == 1) {

                        textView_items_count.setText("" + (total_items_and_total_amount[0]) + " item");
                    } else {

                        textView_items_count.setText("" + (total_items_and_total_amount[0]) + " items");
                    }


                } else {
                    minus_item.setVisibility(View.GONE);
                    textView_track_items_of_individual_food.setVisibility(View.GONE);
                    add_item.setVisibility(View.GONE);
                    btn_add_item.setVisibility(View.VISIBLE);

                    track_individual_food_items--;
                    textView_track_items_of_individual_food.setText("" + track_individual_food_items);

//                    food_made_amount = food_ArrayList.get(0).getFood_price() + food_ArrayList.get(0).getFood_taxes()
//                            + food_ArrayList.get(0).getFood_packing_charge() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
                    food_made_amount = food_ArrayList.get(0).getFood_price() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);

                    int individual_total_price = food_made_amount * track_individual_food_items ;
                    int total_packaging_charge  = food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
                    int total_food_taxes        = food_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

                    myDB.check_selected_item(Integer.parseInt(food_id), food_name,food_ArrayList.get(0).getVeg(), food_ArrayList.get(0).getFood_price(),food_made_amount, individual_total_price, track_individual_food_items, food_ArrayList.get(0).getFood_discount(), total_food_taxes, total_packaging_charge, food_ArrayList.get(0).getHotel_name(), food_ArrayList.get(0).getHotel_id());

                    int[] total_items_and_total_amount = myDB.get_total_items_and_total_amount();

                    textView_total_price.setText("" + total_items_and_total_amount[1]);
                    textView_items_count.setText("" + (total_items_and_total_amount[0]) + " items");

                    if (total_items_and_total_amount[0] == 0) {

                        linearLayout_for_go_to_cart.setVisibility(View.GONE);
                    }

                    myDB.Delete_sqlite_one_selected_row(Integer.parseInt(food_id));
                }
            }
        });
    }

    private void add_item_overall_function() {

        if (track_individual_food_items == 0) {

            if (myDB.get_hotel_id_of_cart_items() == food_ArrayList.get(0).getHotel_id() || myDB.get_hotel_id_of_cart_items() == 0) {

                add_item_function();

            }else {

                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
                }else {
                    builder = new AlertDialog.Builder(getActivity());
                }

                builder.setTitle("Do you want to proceed ?");
                builder.setMessage("Not the same hotel. If you agree your cart item will be lost.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB.Clear_sqlite();
                        add_item_function();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setIcon(R.drawable.ic_alert_yellow);

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez_dark));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez_dark));

            }

        } else {
            track_individual_food_items++;
            textView_track_items_of_individual_food.setText("" + track_individual_food_items);

//            food_made_amount = food_ArrayList.get(0).getFood_price() + food_ArrayList.get(0).getFood_taxes()
//                    + food_ArrayList.get(0).getFood_packing_charge() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
            food_made_amount = food_ArrayList.get(0).getFood_price() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);

            int food_total_price = food_made_amount + Integer.parseInt(textView_total_price.getText().toString());
            textView_total_price.setText("" + food_total_price);

            int individual_total_price  = food_made_amount * track_individual_food_items;
            int total_packaging_charge  = food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
            int total_food_taxes        = food_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

            Log.e("PRICE","price packaging"+food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items);
            Log.e("PRICE","price tax"+food_ArrayList.get(0).getFood_taxes() * track_individual_food_items);

            myDB.check_selected_item(Integer.parseInt(food_id), food_name, food_ArrayList.get(0).getVeg(), food_ArrayList.get(0).getFood_price(), food_made_amount, individual_total_price, track_individual_food_items, food_ArrayList.get(0).getFood_discount(), total_food_taxes, total_packaging_charge, food_ArrayList.get(0).getHotel_name(), food_ArrayList.get(0).getHotel_id());

            int[] total_items_and_total_amount = myDB.get_total_items_and_total_amount();


            textView_items_count.setText("" + (total_items_and_total_amount[0]) + " items");
            textView_total_price.setText("" + total_items_and_total_amount[1]);


        }


    }

    private void add_item_function(){


        Log.e("STATION ID",""+new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID));

        btn_add_item.setVisibility(View.GONE);
        add_item.setVisibility(View.VISIBLE);
        minus_item.setVisibility(View.VISIBLE);
        textView_track_items_of_individual_food.setVisibility(View.VISIBLE);
        linearLayout_for_go_to_cart.setVisibility(View.VISIBLE);
        track_individual_food_items++;
        textView_track_items_of_individual_food.setText("" + track_individual_food_items);

//        food_made_amount = food_ArrayList.get(0).getFood_price() + food_ArrayList.get(0).getFood_taxes()
//                + food_ArrayList.get(0).getFood_packing_charge() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
        food_made_amount = food_ArrayList.get(0).getFood_price() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);

        int individual_total_price = food_made_amount * track_individual_food_items;
        int total_packaging_charge  = food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
        int total_food_taxes        = food_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

        Log.e("PRICE","price packaging"+food_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items);
        Log.e("PRICE","price tax"+food_ArrayList.get(0).getFood_taxes() * track_individual_food_items);

        myDB.check_selected_item(Integer.parseInt(food_id), food_name,food_ArrayList.get(0).getVeg(), food_ArrayList.get(0).getFood_price() ,food_made_amount , individual_total_price, track_individual_food_items, food_ArrayList.get(0).getFood_discount(), total_food_taxes, total_packaging_charge, food_ArrayList.get(0).getHotel_name(), food_ArrayList.get(0).getHotel_id());

        int[] total_items_and_total_amount = myDB.get_total_items_and_total_amount();
        textView_items_count.setText("" + (total_items_and_total_amount[0]) + " items");
        textView_total_price.setText("" + total_items_and_total_amount[1]);
    }

    private void set_view() {

        if (food_ArrayList != null) {

            String url = UrlValues.HOTEL_CATEGORY_IMAGE_URL+food_ArrayList.get(0).getStation_name()+"/"+food_ArrayList.get(0).getHotel_name()
                    +"/category/"+food_ArrayList.get(0).getFood_id()+".jpg";


            Picasso.get().load(url).fit().centerCrop().into(imageView_of_food);

            Log.e("URL",url);

            // food price including food packing charge and food taxes excluding added discount
            //int price = food_ArrayList.get(0).getFood_price()+food_ArrayList.get(0).getFood_taxes()+food_ArrayList.get(0).getFood_packing_charge() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
            int price = food_ArrayList.get(0).getFood_price() - ((food_ArrayList.get(0).getFood_price() * food_ArrayList.get(0).getFood_discount())/100);
            //int old_price = food_ArrayList.get(0).getFood_price()+food_ArrayList.get(0).getFood_taxes()+food_ArrayList.get(0).getFood_packing_charge() ;
            int old_price = food_ArrayList.get(0).getFood_price() ;

            textView_food_name.setText(food_ArrayList.get(0).getFood_name());
            textView_hotel_name.setText("Foods of "+food_ArrayList.get(0).getHotel_name()+"  (Add more items)");
            textView_food_category.setText(food_ArrayList.get(0).getFood_category_name());
            textView_rating.setText(food_ArrayList.get(0).getFood_rating());
            textView_description.setText(food_ArrayList.get(0).getFood_details());
            textView_food_price.setText("" + price);

            if (food_ArrayList.get(0).getVeg().equals("y")) {

                imageView_veg.setImageResource(R.drawable.veg);
            } else {
                imageView_veg.setImageResource(R.drawable.non_veg);
            }

            if (food_ArrayList.get(0).getFood_discount() != 0) {

                tv_old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_discount.setText(food_ArrayList.get(0).getFood_discount()+"%");
                tv_old_price.setText(""+old_price);

            }else {
                rel_layout_discount.setVisibility(View.GONE);
                linearLayout_old_price.setVisibility(View.GONE);
            }

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            linearLayout_itemClicked.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);

            hotel_all_foods();

        } else {
            Toast.makeText(getActivity(), "Not Found !!!", Toast.LENGTH_SHORT).show();
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            linearLayout_itemClicked.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }

    }

    private void hotel_all_foods() {

        // recycler view hor hotel items
        String category_url = UrlValues.GET_ALL_FOOD_OF_HOTEL;

        Map<String, String> map = new HashMap<>();
        map.put(Json_Hotels_of_Searched_Station.HOTEL_ID,""+food_ArrayList.get(0).getHotel_id());
        map.put("FOOD_NAME",food_name);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(category_url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);

                    hotel_food_ArrayList = new Hotel_All_Food_Utils().Get_all_Hotel_Foods(object);
                    set_recycler_view();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                set_recycler_view();
            }
        });

    }

    private void set_recycler_view(){

        if (hotel_food_ArrayList != null) {

            adapter= new Adapter_for_item_clicked_hotel_items(hotel_food_ArrayList,food_ArrayList.get(0).getStation_name(),food_ArrayList.get(0).getHotel_name());
            ((Adapter_for_item_clicked_hotel_items) adapter).setClickListener(this);
            recyclerView.setAdapter(adapter);

            if (hotel_pressed) {
                recyclerView.setVisibility(View.VISIBLE);
                tv_under_recycler_view.setVisibility(View.GONE);
            }

        }else {
            recyclerView.setVisibility(View.GONE);
            tv_under_recycler_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);

    }

    @Override
    public void onResume() {
        super.onResume();


//        //have to check hotel status
//        if (food_ArrayList != null) {
//
//            if (!food_ArrayList.get(0).getHotel_status().equals("o")) {
//
//                add_item.setVisibility(View.GONE);
//                minus_item.setVisibility(View.GONE);
//                textView_track_items_of_individual_food.setVisibility(View.GONE);
//                imageView_of_food.setAlpha(100);
//                relativeLayout_above_image.setVisibility(View.VISIBLE);
//                btn_add_item.setVisibility(View.GONE);
//
//            }
//        }

        hotel_pressed = false;

        int[] array_items_and_price = myDB.get_total_items_and_total_amount();

        if (array_items_and_price[0] == 0) {

            linearLayout_for_go_to_cart.setVisibility(View.GONE);
        }

        food_from_sqlite_ArrayList = myDB.get_selected_item(Integer.parseInt(food_id));

        if (food_from_sqlite_ArrayList.size() != 0) {

            if (food_from_sqlite_ArrayList.get(0).gettotal_items() != 0) {

                track_individual_food_items = food_from_sqlite_ArrayList.get(0).gettotal_items();

                textView_track_items_of_individual_food.setText("" + track_individual_food_items);

                minus_item.setVisibility(View.VISIBLE);
                textView_track_items_of_individual_food.setVisibility(View.VISIBLE);
                add_item.setVisibility(View.VISIBLE);
                btn_add_item.setVisibility(View.GONE);
            }

        }else {
            minus_item.setVisibility(View.GONE);
            textView_track_items_of_individual_food.setVisibility(View.GONE);
            add_item.setVisibility(View.GONE);
            btn_add_item.setVisibility(View.VISIBLE);

            track_individual_food_items = 0;
        }

        food_from_sqlite_ArrayList_for_all = myDB.get_all_selected_items();

        if (food_from_sqlite_ArrayList_for_all.size() != 0) {

            if (food_from_sqlite_ArrayList_for_all.get(0).gettotal_items() != 0) {
                linearLayout_for_go_to_cart.setVisibility(View.VISIBLE);

                textView_total_price.setText("" + food_from_sqlite_ArrayList_for_all.get(0).getFood_total_price());
                textView_items_count.setText("" + food_from_sqlite_ArrayList_for_all.get(0).gettotal_items() + " items");
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {

        Intent intent = new Intent(getActivity(),ItemClickedActivity.class);
        intent.putExtra("FOOD_NAME" ,""+hotel_food_ArrayList.get(position).getFood_name());
        intent.putExtra("FOOD_ID"   ,""+hotel_food_ArrayList.get(position).getFood_id());
        startActivity(intent);

        getActivity().finish();
    }


}
