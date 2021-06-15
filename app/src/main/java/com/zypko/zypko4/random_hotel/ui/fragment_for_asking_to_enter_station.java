package com.zypko.zypko4.random_hotel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.ClickedRandomFoodActivity;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.Prefs;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_History;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.random_hotel.util.History;
import com.zypko.zypko4.random_hotel.util.Past_searh_Recycler_view_Adapter;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_for_asking_to_enter_station extends Fragment implements Past_searh_Recycler_view_Adapter.ClickListener {

    View root;
    EditText editText_hotel_search;

    RecyclerView recyclerView_for_past_search;
    RecyclerView.Adapter past_search_Adapter;
    String food_name;

    RecyclerView.LayoutManager layoutManager_for_past_search;

    // int history_or_search = 0;
    Database myDB;

    int history_or_search = 0;
    LinearLayout linearLayout_no_history;


    String[] station ;

    String[] stations;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_search_station, container, false);

            initialise();
        }
        return root;
    }

    private void initialise() {

        linearLayout_no_history   = root.findViewById(R.id.linear_layout_no_history);
        editText_hotel_search = root.findViewById(R.id.edittext_search_station);

        myDB = new Database(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            food_name = bundle.getString("FOOD_CATEGORY_NAME");
        }

        ViewMenu();
    }


    private void ViewMenu() {


        ArrayList<History> arrayList = myDB.get_history(Json_History.STATION_NAME);
        station = new String[arrayList.size()];

        if (arrayList.size() == 0){
            linearLayout_no_history.setVisibility(View.VISIBLE);
        }else {
            linearLayout_no_history.setVisibility(View.GONE);
        }

        for (int i=0;i<arrayList.size();i++){
            station[i] = arrayList.get(i).getSearched_name();
        }


        recyclerView_for_past_search = root.findViewById(R.id.recycler_view_for_past_search);

        past_search_Adapter = new Past_searh_Recycler_view_Adapter(getActivity(), station, 0);
        ((Past_searh_Recycler_view_Adapter) past_search_Adapter).setClickListener(this);


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView_for_past_search.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_in_recycler_view));
        recyclerView_for_past_search.addItemDecoration(divider);


        layoutManager_for_past_search = new LinearLayoutManager(getActivity());
        recyclerView_for_past_search.setLayoutManager(layoutManager_for_past_search);
        recyclerView_for_past_search.setAdapter(past_search_Adapter);

        //past_search_Adapter.

        ////////////////////////////////////////////////////////


        search_works();


    }


    private void search_works() {



        editText_hotel_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //back_pressed = 1;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_station_name(s.toString());
            }
        });
    }

    private void filter_station_name(String text) {

        String url = UrlValues.SEARCH_STATION_SUGGESTION;// + "?STATION_NAME=" + text;

        Map<String, String> map = new HashMap<>();
        map.put("STATION_NAME",text);

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

                        stations = new String[array.length()];

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            String station_name = obj.getString(Json_Shared_Preference.STATION_NAME);

                            stations[i] = station_name;
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
//
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
//
//            }
//        });
    }

    private void past_on_recycler_view() {

        past_search_Adapter = new Past_searh_Recycler_view_Adapter(getActivity(), stations, 1);
        ((Past_searh_Recycler_view_Adapter) past_search_Adapter).setClickListener(this);


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView_for_past_search.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_in_recycler_view));
        recyclerView_for_past_search.addItemDecoration(divider);

        layoutManager_for_past_search = new LinearLayoutManager(getActivity());
        recyclerView_for_past_search.setLayoutManager(layoutManager_for_past_search);

        recyclerView_for_past_search.setAdapter(past_search_Adapter);

        linearLayout_no_history.setVisibility(View.GONE);
    }

    @Override
    public void itemClicked(View view) {

        TextView textView = view.findViewById(R.id.textview_for_past_search);

        new PrefEditor(getActivity()).writeData(Prefs.LOCATION, textView.getText().toString());

        Intent intent = new Intent(getActivity(), ClickedRandomFoodActivity.class);

        intent.putExtra("STATION_NAME"      , "" + textView.getText().toString());
        intent.putExtra("FOOD_CATEGORY_NAME", "" + food_name);
        intent.putExtra("FRAGMENT"          , "CLICKED_FOOD_CATEGORY" );
        //intent.putExtra("HISTORY_OR_SEARCH","");

        getActivity().startActivity(intent);

        getActivity().finish();


    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }


}
