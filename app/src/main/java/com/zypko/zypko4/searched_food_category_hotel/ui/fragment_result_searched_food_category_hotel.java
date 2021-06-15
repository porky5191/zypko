package com.zypko.zypko4.searched_food_category_hotel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.ItemClickedActivity;
import com.zypko.zypko4.activity.ui.SearchedStationResultHotelsActivity;
import com.zypko.zypko4.searched_food_category_hotel.util.Adapter_for_result_searched_food_category_hotel;
import com.zypko.zypko4.server.NetRequest;

public class fragment_result_searched_food_category_hotel extends Fragment implements View.OnKeyListener,Adapter_for_result_searched_food_category_hotel.ClickListener {

    View root;
    RecyclerView recyclerView_for_hotels;
    RecyclerView.Adapter Adapter_for_hotels;
    RecyclerView.LayoutManager layoutManager_for_searched_hotel;

    private boolean clicked;

    String[] data = {"1","2","3","4","5","6","7","8"};
    String station_name,item_name;
    int hotel_id;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_result_searched_food_category_hotel,container,false);

            /////////////////////////////////////////////////////////
            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            /////////////////////////////////////////////////////////

            initialise();
        }

        return root;
    }

    private void initialise() {

        toolbar = root.findViewById(R.id.toolbar_search_food_category);
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
                getActivity().onBackPressed();
            }
        });

        Bundle bundle = getArguments();

        if (bundle != null) {

            station_name        = bundle.getString("STATION_NAME");
            item_name           = bundle.getString("ITEM_NAME");
            hotel_id            = bundle.getInt("HOTEL_ID");

            toolbar.setTitle(""+item_name);
        }


        recyclerView_for_hotels = root.findViewById(R.id.recycler_view_of_searched_food_category_hotel);
        layoutManager_for_searched_hotel = new LinearLayoutManager(getActivity());
        recyclerView_for_hotels.setLayoutManager(layoutManager_for_searched_hotel);
        Adapter_for_hotels = new Adapter_for_result_searched_food_category_hotel(data);
        ((Adapter_for_result_searched_food_category_hotel) Adapter_for_hotels).setClickListener(this);
        recyclerView_for_hotels.setAdapter(Adapter_for_hotels);

    }

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

        Intent intent = new Intent(getActivity(),SearchedStationResultHotelsActivity.class);
        intent.putExtra("STATION_NAME",""+station_name);
        startActivity(intent);

        getActivity().finish();

    }

    @Override
    public void itemClicked(View view) {

        if (!clicked) {


            ImageView imageView = view.findViewById(R.id.imageview_of_searched_item);
            TextView textView = view.findViewById(R.id.textview_name_of_searched_item);

            String Name_of_food = textView.getText().toString();

            Intent intent = new Intent(getActivity(), ItemClickedActivity.class);
            intent.putExtra("FOOD_NAME", "" + Name_of_food);
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
