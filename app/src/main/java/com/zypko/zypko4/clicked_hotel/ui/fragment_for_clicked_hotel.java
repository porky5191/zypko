package com.zypko.zypko4.clicked_hotel.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.VolleyError;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.SearchedStationResultHotelsActivity;
import com.zypko.zypko4.clicked_hotel.util.Adapter_for_clicked_hotel_Recycler_view;
import com.zypko.zypko4.clicked_hotel.util.Clicked_Hotel_utils;
import com.zypko.zypko4.clicked_hotel.util.Hotel;
import com.zypko.zypko4.clicked_hotel.util.ViewPager_Adapter_for_Hotel_Image;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.hotel_category_clicked.ui.fragment_for_clicked_category;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;

import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class fragment_for_clicked_hotel extends Fragment implements Adapter_for_clicked_hotel_Recycler_view.ClickListener,View.OnKeyListener {

    View root;
    RecyclerView recyclerView_for_category;
    RecyclerView.Adapter adapter_for_categoty;
    RecyclerView.LayoutManager layoutManager_for_categoty;
    String name_of_hotel,station_name,hotel_id;

    //ImageView image_of_hotel;
    TextView hotel_name,hotel_address,hotel_category,hotel_rating;

    LinearLayout linearLayout_for_error_message;
    ArrayList<Hotel> hotels_ArrayList;
    ShimmerLayout shimmerLayout;
    ScrollView scrollView;
    LinearLayout linearLayout;

    String FRAGMENT;
    //ViewFlipper viewFlipper;
    ViewPager viewPager;
    String[] imageUrls;
    Timer timer;
    int current_position = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_clicked_hotel,container,false);

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

        linearLayout_for_error_message = root.findViewById(R.id.linear_layout_error_message);
        hotel_name                  = root.findViewById(R.id.textView_hotel_name);
        hotel_address               = root.findViewById(R.id.textView_hotel_address);
        hotel_category              = root.findViewById(R.id.textView_hotel_category);
        //image_of_hotel              = root.findViewById(R.id.imageView_image_of_hotel);
        recyclerView_for_category   = root.findViewById(R.id.recycler_view_clicked_hotel);
        //progressBar                 = root.findViewById(R.id.progress_bar_hotel_details);
        hotel_rating                = root.findViewById(R.id.textView_hotel_rating);

        shimmerLayout               = root.findViewById(R.id.shimmer_layout_hotel_details);
        scrollView                  = root.findViewById(R.id.scroll_view_hotel_details);
        linearLayout                = root.findViewById(R.id.linear_layout_hotel_details);

        //viewFlipper                 = root.findViewById(R.id.viewFlipper_image_of_hotel);
        viewPager                   = root.findViewById(R.id.viewPager_hotel_image);

        shimmerLayout.startShimmerAnimation();
        //scrollView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        set_up();

    }

    private void set_up() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            name_of_hotel   = bundle.getString("HOTEL_NAME");
            hotel_id        = bundle.getString("HOTEL_ID");
            station_name    = bundle.getString("STATION_NAME");
            FRAGMENT        = bundle.getString("FRAGMENT");

        }

        String url = UrlValues.CLICKED_HOTEL_SHOW_RESULT+"?HOTEL_ID="+hotel_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                Log.e("CLICKED_CATEGORY","OBJECT :"+object);
                hotels_ArrayList = new Clicked_Hotel_utils().Get_details_of_clicked_hotel(object);

                set_recycler();

            }

            @Override
            public void onError(VolleyError error) {
                //Toast.makeText(getActivity(), "OnError get", Toast.LENGTH_SHORT).show();
                set_recycler();
            }
        });



    }



    private void set_recycler() {

        if (hotels_ArrayList != null) {

            String url = UrlValues.HOTEL_IMAGE_URL
                    + station_name + "/" + hotels_ArrayList.get(0).getHotel_name()
                    + "/" + hotels_ArrayList.get(0).getHotel_id() + "_";


            imageUrls = new String[]{url + "1.jpg", url + "2.jpg", url + "3.jpg"};

            ViewPager_Adapter_for_Hotel_Image adapter = new ViewPager_Adapter_for_Hotel_Image(getActivity(),imageUrls);
            viewPager.setAdapter(adapter);

            Slide_image_of_Hotel();

            hotel_category.setText  (hotels_ArrayList.get(0).getHotel_category());
            hotel_address.setText   (hotels_ArrayList.get(0).getHotel_address());
            hotel_rating.setText    (hotels_ArrayList.get(0).getHotel_rating());

            adapter_for_categoty = new Adapter_for_clicked_hotel_Recycler_view(getActivity(),hotels_ArrayList);
            ((Adapter_for_clicked_hotel_Recycler_view) adapter_for_categoty).setClickListener(this);
            recyclerView_for_category.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView_for_category.setAdapter(adapter_for_categoty);

            hotel_name.setText(name_of_hotel);
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView_for_category.setVisibility(View.VISIBLE);

        }else {

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout_for_error_message.setVisibility(View.VISIBLE);
            recyclerView_for_category.setVisibility(View.GONE);

        }
    }


    private void Slide_image_of_Hotel(){

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (current_position == imageUrls.length){
                    current_position = 0;
                }
                viewPager.setCurrentItem(current_position++,true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250,2800);
    }

    @Override
    public void itemClicked(View view) {

        TextView textView_food_category_name = view.findViewById(R.id.textView_food_Category_name);

        Bundle bundle = new Bundle();
        bundle.putString("FOOD_CATEGORY_NAME",""+textView_food_category_name.getText().toString());
        bundle.putString("HOTEL_ID",""+hotel_id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Fragment fragment = new fragment_for_clicked_category();
            fragment.setArguments(bundle);
            String backStack = fragment_for_clicked_category.class.getName().toUpperCase();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
            transaction.replace(R.id.f_container, fragment);
            transaction.addToBackStack(backStack);
            transaction.commit();
        }else {
            new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_clicked_category(),bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPresed();
            }
        }
        return true;
    }

    private void onBackPresed() {

        if (FRAGMENT.equals("SEARCHED_HOTEL")) {

            Intent intent = new Intent(getActivity(), SearchedStationResultHotelsActivity.class);
            intent.putExtra("STATION_NAME", "" + station_name);
            startActivity(intent);

            getActivity().finish();
        }else {

            getActivity().onBackPressed();
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


}

