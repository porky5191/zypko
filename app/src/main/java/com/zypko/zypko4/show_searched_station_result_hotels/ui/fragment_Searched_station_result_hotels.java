package com.zypko.zypko4.show_searched_station_result_hotels.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.HotelClickedActivity;
import com.zypko.zypko4.activity.ui.LoginActivity;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.cart.ui.fragment_for_Cart;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Available_Station;
import com.zypko.zypko4.json_Objects.Json_History;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Adapter_for_Searched_hotels;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Hotels_of_Searched_Station;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Hotels_of_Searched_Station_utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.supercharge.shimmerlayout.ShimmerLayout;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class fragment_Searched_station_result_hotels extends Fragment implements View.OnKeyListener,Adapter_for_Searched_hotels.ClickListener {

    View root;
    RecyclerView recyclerView_for_hotels;
    RecyclerView.Adapter Adapter_for_hotels;
    RecyclerView.LayoutManager layoutManager_for_searched_hotel;
    TextView floating_text_button;

    private boolean clicked ;

    String station_name;
    ArrayList<Hotels_of_Searched_Station> hotels_ArrayList;
    LinearLayout linearLayout_for_error_message;

    ShimmerLayout shimmerLayout;
    Database myDB;

    FloatingActionButton fab_account, fab_cart, fab_call;
    FloatingActionMenu floatingActionMenu;
    int request_call = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_searched_station_result_hotels,container,false);

            //////////////////////////////////////////

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

        myDB = new Database(getActivity());

        // Floating Action Button

        fab_account = root.findViewById(R.id.fab_account);
        fab_cart = root.findViewById(R.id.fab_cart);
        fab_call = root.findViewById(R.id.fab_call);
        floatingActionMenu = root.findViewById(R.id.floating_action_menu);

        linearLayout_for_error_message  = root.findViewById(R.id.linear_layout_error_message);
        shimmerLayout                   = root.findViewById(R.id.shimmer_layout_hotels);
        floating_text_button            = root.findViewById(R.id.floating_text_button_for_foods);

        shimmerLayout.startShimmerAnimation();


        recyclerView_for_hotels = root.findViewById(R.id.recycler_view_of_searched_hotels);
        layoutManager_for_searched_hotel = new LinearLayoutManager(getActivity());
        recyclerView_for_hotels.setLayoutManager(layoutManager_for_searched_hotel);


        set_up();
    }

    private void set_up() {

        final Bundle bundle = getArguments();

        if (bundle != null) {

            station_name = bundle.getString("STATION_NAME");
            myDB.insert_searched_item(station_name,Json_History.STATION_NAME, (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.STATION_ID));

        }


        fab_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("FRAGMENT","USER_ACCOUNT");
                startActivity(intent);
                floatingActionMenu.close(true);

            }
        });


        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hotel_id = myDB.get_hotel_id_of_cart_items();

                if (hotel_id != 0) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("HOTEL_ID", hotel_id);

                    new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_Cart(), bundle);
                    floatingActionMenu.close(true);

                } else {

                    Toast.makeText(getActivity(), "You don't have any items in cart", Toast.LENGTH_SHORT).show();

                }

            }
        });

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_call();
                floatingActionMenu.close(true);

            }
        });


        String url = UrlValues.HOTELS_OF_SEARCHED_STATION_PHP_URL;//+"?"+Json_Hotels_of_Searched_Station.STATION_NAME+"="+station_name;

        Map<String, String> map = new HashMap<>();
        map.put(Json_Hotels_of_Searched_Station.STATION_NAME,station_name);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {


                try {
                    JSONObject object = new JSONObject(string);
                    Log.d("HOTEL__",object.toString());
                    hotels_ArrayList = new Hotels_of_Searched_Station_utils().Get_all_hotels_of_searched_station(object);

                    set_recycler();
                } catch (JSONException e) {
                    e.printStackTrace();
                    set_recycler();
                }

            }

            @Override
            public void onError(VolleyError error) {
                set_recycler();
            }
        });


//        JSONProvider provider = new JSONProvider(getActivity());
//        provider.getJson(url, new HTTP_Get() {
//            @Override
//            public void onSuccess(JSONObject object) {
//
//
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//            }
//        });

        floating_text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = new Bundle();
                bundle1.putString("STATION_NAME",""+station_name);

                new FragmentOpener(getActivity()).setManager(getFragmentManager())
                        .open_Replace_Backstack(new fragment_searched_food_categoty_hotel(),bundle1);
            }
        });
    }


    private void set_recycler() {

        if (hotels_ArrayList != null) {

            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.STATION_ID,hotels_ArrayList.get(0).getStation_id());

            Adapter_for_hotels = new Adapter_for_Searched_hotels(getActivity(),hotels_ArrayList);
            ((Adapter_for_Searched_hotels) Adapter_for_hotels).setClickListener(this);
            recyclerView_for_hotels.setAdapter(Adapter_for_hotels);

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);

        }else {
            linearLayout_for_error_message.setVisibility(View.VISIBLE);
            recyclerView_for_hotels.setVisibility(View.GONE);
            floating_text_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "We are currently unavailable at this station", Toast.LENGTH_SHORT).show();
                }
            });

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);

        }


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

        startActivity(new Intent(getActivity(),RandomHotelActivity.class));
        //getActivity().onBackPressed();
        getActivity().finish();

    }


    @Override
    public void itemClicked(View view) {

        if (!clicked) {

            ImageView imageView = view.findViewById(R.id.imageview_of_one_searched_hotel);
            TextView textView = view.findViewById(R.id.textview_name_of_searched_hotel);
            TextView textView_hotel_id = view.findViewById(R.id.textView_hotel_id);

            String Name_of_food = textView.getText().toString();

            Intent intent = new Intent(getActivity(), HotelClickedActivity.class);
            intent.putExtra("HOTEL_NAME", "" + Name_of_food);
            intent.putExtra("HOTEL_ID", "" + textView_hotel_id.getText().toString());
            intent.putExtra("STATION_NAME", "" + station_name);
            intent.putExtra("FRAGMENT", "FROM_HOTEL" );
            //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, ViewCompat.getTransitionName(imageView));
            //startActivity(intent, optionsCompat.toBundle());

            startActivity(intent);
            clicked = true;
        }
    }



    private void make_call() {
        String number = "6002015766";

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, request_call);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == request_call) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                make_call();

            } else {
                Toast.makeText(getActivity(), "You can't access this feature", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        clicked = false;
        floatingActionMenu.close(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);

    }
}
