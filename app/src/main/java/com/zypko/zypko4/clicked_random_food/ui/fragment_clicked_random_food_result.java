package com.zypko.zypko4.clicked_random_food.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.ItemClickedActivity;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.activity.ui.SearchedStationResultHotelsActivity;
import com.zypko.zypko4.clicked_random_food.utils.Adapter_for_clicked_random_food;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category_Utils;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Available_Station;
import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_History;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.random_hotel.ui.fragment_for_asking_to_enter_station;
import com.zypko.zypko4.random_hotel.util.Random_Food_Category_utils;
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

public class fragment_clicked_random_food_result extends Fragment implements View.OnKeyListener,Adapter_for_clicked_random_food.ClickListener{

    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private boolean clicked;

    String station_name,food_category_name;
    String FRAGMENT;

    ArrayList<Food_of_Clicked_Category> food_ArrayList = new ArrayList<>();
    LinearLayout linearLayout_for_error_message;
    ShimmerLayout shimmerLayout;

    Database myDB;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_clicked_random_food_result,container,false);

            //////////////////////////////////////////////

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            //////////////////////////////////////////


            initialise();
        }

        return root;
    }

    private void initialise() {

        myDB        = new Database(getActivity());

        toolbar = root.findViewById(R.id.toolbar_clicked_random_food);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPresed();
            }
        });


        linearLayout_for_error_message  = root.findViewById(R.id.linear_layout_error_message);
        shimmerLayout                   = root.findViewById(R.id.shimmer_layout_food_category_clicked);

        recyclerView = root.findViewById(R.id.recycler_view_clicked_random_food_result);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

       ////***********************************************************************************************************////

        shimmerLayout.startShimmerAnimation();
        recyclerView.setVisibility(View.GONE);

        set_up();

    }

    private void set_up() {

        Bundle bundle = getArguments();

        if (bundle != null) {

            station_name        = bundle.getString("STATION_NAME");
            food_category_name  = bundle.getString("FOOD_CATEGORY_NAME");
            FRAGMENT            = bundle.getString("FRAGMENT");

            toolbar.setTitle(""+food_category_name);
            myDB.insert_searched_item(station_name,Json_History.STATION_NAME, (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID));

//            Log.e("FRAGMENT","s"+FRAGMENT);
//            if (FRAGMENT.equals("CLICKED_FOOD_CATEGORY")) {
//                new FragmentOpener(getActivity()).setManager(getFragmentManager()).remove_fragment(new fragment_for_asking_to_enter_station());
//            }


        }

        String url = UrlValues.CLICKED_FOOD_CATEGORY_AND_STATION_SHOW_RESULT_PHP_URL ;

        Map<String ,String> map = new HashMap<>();
        map.put(Json_Food_of_Clicked_Category.STATION_NAME          ,station_name);
        map.put(Json_Food_of_Clicked_Category.FOOD_CATEGORY_NAME    ,food_category_name);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);
                    food_ArrayList = new Food_of_Clicked_Category_Utils().Get_all_Food_clicked_Category(object);

                    set_recycler_view();

                } catch (JSONException e) {
                    e.printStackTrace();
                    set_recycler_view();
                }
            }

            @Override
            public void onError(VolleyError error) {
                set_recycler_view();
            }
        });
    }

    private void set_recycler_view() {

        if (food_ArrayList != null && getActivity() != null) {

            if (food_ArrayList.size() != 0) {
                new PrefEditor(getActivity()).writeData(Json_Shared_Preference.STATION_ID,food_ArrayList.get(0).getStation_id());

                adapter= new Adapter_for_clicked_random_food(food_ArrayList);
                ((Adapter_for_clicked_random_food) adapter).setClickListener(this);
                recyclerView.setAdapter(adapter);

                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

            }else {
                shimmerLayout.stopShimmerAnimation();
                shimmerLayout.setVisibility(View.GONE);

                linearLayout_for_error_message.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        }else {
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);

            linearLayout_for_error_message.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }


    }

////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPresed();
            }

        }

        return true;
    }

    private void onBackPresed() {

        if (FRAGMENT.equals("SEARCHED_CATEGORY")){
            Intent intent = new Intent(getActivity(),SearchedStationResultHotelsActivity.class);
            intent.putExtra("STATION_NAME",""+station_name);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(),RandomHotelActivity.class);
            startActivity(intent);

            //getActivity().onBackPressed();
            getActivity().finish();
        }


        //startActivity(new Intent(getActivity(),RandomHotelActivity.class));
        //getActivity().onBackPressed();
        //getActivity().finish();


    }

    @Override
    public void itemClicked(View view) {


        if (!clicked) {


            ImageView imageView         = view.findViewById(R.id.imageView_of_food_category);
            TextView imageView_id       = view.findViewById(R.id.textView_id);
            TextView textView           = view.findViewById(R.id.textView_name_of_food_category);
            String Name_of_food         = textView.getText().toString();


            Intent intent = new Intent(getActivity(), ItemClickedActivity.class);
            intent.putExtra("FOOD_NAME",    "" + Name_of_food);
            intent.putExtra("FOOD_ID",      "" + imageView_id.getText().toString());

            //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, ViewCompat.getTransitionName(imageView));
            //startActivity(intent, optionsCompat.toBundle());
            startActivity(intent);
            clicked = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clicked = false;
    }


    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }

}
