package com.zypko.zypko4.show_searched_station_result_hotels.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.ClickedRandomFoodActivity;
import com.zypko.zypko4.activity.ui.HotelClickedActivity;
import com.zypko.zypko4.activity.ui.ItemClickedActivity;
import com.zypko.zypko4.activity.ui.SearchedFoodCategoryHotelActivity;
import com.zypko.zypko4.activity.ui.SearchedStationResultHotelsActivity;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Available_Station;
import com.zypko.zypko4.json_Objects.Json_History;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.random_hotel.util.History;
import com.zypko.zypko4.random_hotel.util.Past_searh_Recycler_view_Adapter;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Adapter_for_searching_food_category_Hotel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_searched_food_categoty_hotel extends Fragment implements Adapter_for_searching_food_category_Hotel.ClickListener, View.OnKeyListener {

    View root;
    EditText editText_food_category_hotel_search;
    RadioButton radioButton_category, radioButton_hotel;
    RadioGroup radioGroup;

    RecyclerView recyclerView_for_past_search;
    RecyclerView.Adapter past_search_Adapter;
    RecyclerView.LayoutManager layoutManager_for_past_search;

    Database myDB;
    LinearLayout linearLayout_no_history;
    String[] history;
    int[] history_item_id,hotel_id_array,food_category_id;
    String[] string_array;
    String station_name;

    boolean is_searched = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_searched_food_categoty_hotel, container, false);

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            station_name = bundle.getString("STATION_NAME");
        }

        recyclerView_for_past_search    = root.findViewById(R.id.recycler_view_for_past_search);
        //radioButton_food                = root.findViewById(R.id.radio_button_food);
        radioButton_category            = root.findViewById(R.id.radio_button_food_category);
        radioButton_hotel               = root.findViewById(R.id.radio_button_hotel);

        radioGroup                      = root.findViewById(R.id.Radio_group_searching_food_category_hotel);
        linearLayout_no_history         = root.findViewById(R.id.linear_layout_no_history);
        myDB = new Database(getActivity());

        ViewMenu();
    }

    private void ViewMenu() {

        history_recycler_view(Json_History.CATEGORY_NAME);

        radioButton_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_recycler_view(Json_History.HOTEL_NAME);
            }
        });

        radioButton_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history_recycler_view(Json_History.CATEGORY_NAME);
            }
        });


        search_works();
    }

    private void search_works() {

        editText_food_category_hotel_search = root.findViewById(R.id.edittext_for_search_food_categoty_hotel);

        editText_food_category_hotel_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //back_pressed = 1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_food_category) {
                    filter_category(s.toString());

                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_hotel) {
                    filter_hotel(s.toString());
                }

            }
        });
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void filter_category(String text) {

        String url = UrlValues.SEARCH_CATEGORY_SUGGESTION;// + "?FOOD_CATEGORY_NAME=" + text;

        Map<String,String> map = new HashMap<>();
        map.put("FOOD_CATEGORY_NAME",text);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);

                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success > 0) {

                        recyclerView_for_past_search.setAdapter(null);
                        past_search_Adapter.notifyDataSetChanged();

                        JSONArray array = object.getJSONArray(Json_Objects.CONTENT);

                        string_array        = new String[array.length()];
                        food_category_id    = new int[array.length()];

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String station_name = obj.getString("FOOD_CATEGORY_NAME");
                            int catrgory_id     = obj.getInt("FOOD_CATEGORY_ID");

                            string_array[i] = station_name;
                            food_category_id[i] = catrgory_id;
                            is_searched = true;
                        }
                        past_on_recycler_view();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void filter_hotel(String text) {

        String url = UrlValues.SEARCH_HOTEL_SUGGESTION;// + "?HOTEL_NAME=" + text + "&=" + station_name;

        Map<String,String> map = new HashMap<>();
        map.put("HOTEL_NAME"    ,text);
        map.put("STATION_NAME"  ,station_name);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);

                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success > 0) {

                        recyclerView_for_past_search.setAdapter(null);
                        past_search_Adapter.notifyDataSetChanged();

                        JSONArray array = object.getJSONArray(Json_Objects.CONTENT);

                        string_array = new String[array.length()];
                        hotel_id_array = new int[array.length()];

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String station_name = obj.getString("HOTEL_NAME");
                            int hotel_id = obj.getInt("HOTEL_ID");

                            string_array[i] = station_name;
                            hotel_id_array[i] = hotel_id;
                            is_searched = true;
                        }
                        past_on_recycler_view();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void history_recycler_view(String NAME){

        ArrayList<History> arrayList = myDB.get_history(NAME);
        history         = new String[arrayList.size()];
        history_item_id = new int[arrayList.size()];

        if (arrayList.size() == 0) {
            linearLayout_no_history.setVisibility(View.VISIBLE);
        } else {
            linearLayout_no_history.setVisibility(View.GONE);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            history[i]          = arrayList.get(i).getSearched_name();
            history_item_id[i]  = arrayList.get(i).getItem_id();
        }

        past_search_Adapter = new Adapter_for_searching_food_category_Hotel(history, 0);
        ((Adapter_for_searching_food_category_Hotel) past_search_Adapter).setClickListener(this);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView_for_past_search.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_in_recycler_view));
        recyclerView_for_past_search.addItemDecoration(divider);


        layoutManager_for_past_search = new LinearLayoutManager(getActivity());
        recyclerView_for_past_search.setLayoutManager(layoutManager_for_past_search);
        recyclerView_for_past_search.setAdapter(past_search_Adapter);

    }

    private void past_on_recycler_view() {

        past_search_Adapter = new Adapter_for_searching_food_category_Hotel(string_array, 1);
        ((Adapter_for_searching_food_category_Hotel) past_search_Adapter).setClickListener(this);


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView_for_past_search.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_in_recycler_view));
        recyclerView_for_past_search.addItemDecoration(divider);

        layoutManager_for_past_search = new LinearLayoutManager(getActivity());
        recyclerView_for_past_search.setLayoutManager(layoutManager_for_past_search);

        recyclerView_for_past_search.setAdapter(past_search_Adapter);
        linearLayout_no_history.setVisibility(View.GONE);
    }

    @Override
    public void itemClicked(View view, int position) {

        TextView textView = view.findViewById(R.id.textview_for_past_search);
        //String category_or_hotel = null;

        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_food_category) {

            //
             Log.e("is_searched",""+is_searched);

            if (food_category_id != null && is_searched) {
                myDB.insert_searched_item(textView.getText().toString(), Json_History.CATEGORY_NAME,food_category_id[position]);

            }

            Intent intent = new Intent(getActivity(), ClickedRandomFoodActivity.class);

            //intent.putExtra("HOTEL_ID"              ,c_id );
            intent.putExtra("FOOD_CATEGORY_NAME"    , "" + textView.getText().toString());
            intent.putExtra("STATION_NAME"          , "" + station_name);
            intent.putExtra("FRAGMENT"          , "SEARCHED_CATEGORY" );
            getActivity().startActivity(intent);


            getActivity().finish();

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_button_hotel) {

            Log.e("is_searched",""+is_searched);

            int h_id = 0;
            if (history_item_id != null && !is_searched) {
                h_id = history_item_id[position];
            }

            if (hotel_id_array != null && is_searched) {
                myDB.insert_searched_item(textView.getText().toString(), Json_History.HOTEL_NAME,hotel_id_array[position]);

                h_id = hotel_id_array[position];
            }

            Intent intent = new Intent(getActivity(), HotelClickedActivity.class);

            intent.putExtra("FRAGMENT"              ,"SEARCHED_HOTEL");
            intent.putExtra("HOTEL_ID"              ,""+h_id);
            intent.putExtra("HOTEL_NAME"            , "" + textView.getText().toString());
            intent.putExtra("STATION_NAME"          , "" + station_name);
            getActivity().startActivity(intent);

            Log.e("lalalalalala","a"+h_id);
            Log.e("lalalalalala",station_name);

            getActivity().finish();
        }


        ///**********************************************************************************************///

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

        Intent intent = new Intent(getActivity(), SearchedStationResultHotelsActivity.class);
        intent.putExtra("STATION_NAME", "" + station_name);
        startActivity(intent);

        getActivity().finish();

    }


    @Override
    public void onResume() {
        super.onResume();
        is_searched = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);

    }
}
