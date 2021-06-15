package com.zypko.zypko4.hotel_category_clicked.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.zypko.zypko4.clicked_random_food.utils.Adapter_for_clicked_random_food;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category_Utils;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.hotel_category_clicked.util.Adapter_for_clicked_category_show_food_recycler_view;
import com.zypko.zypko4.json_Objects.Json_Food_of_Clicked_Category;
import com.zypko.zypko4.json_Objects.Json_Hotels_of_Searched_Station;
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

public class fragment_for_clicked_category extends Fragment implements Adapter_for_clicked_category_show_food_recycler_view.ClickListener {

    View root;
    RecyclerView recyclerView_clicked_category;
    RecyclerView.Adapter adapter_clicked_category;
    LinearLayout linearLayout_error_message;

    private boolean clicked ;
    ArrayList<Food_of_Clicked_Category> food_ArrayList;
    String food_category_name,hotel_id;

    ShimmerLayout shimmerLayout;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_clicked_category,container,false);
            initialise();
        }

        return root;
    }

    private void initialise() {

        toolbar = root.findViewById(R.id.toolbar_clicked_hotel_category);
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


        linearLayout_error_message      = root.findViewById(R.id.linear_layout_error_message);
        recyclerView_clicked_category   = root.findViewById(R.id.recycler_view_clicked_category);
        shimmerLayout                   = root.findViewById(R.id.shimmer_layout_hotel_clicked);

        shimmerLayout.startShimmerAnimation();
        recyclerView_clicked_category.setVisibility(View.GONE);
        set_up();

    }

    private void set_up() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            food_category_name  = bundle.getString("FOOD_CATEGORY_NAME");
            hotel_id            = bundle.getString("HOTEL_ID");

            toolbar.setTitle(""+food_category_name);
        }


        String url = UrlValues.CLICKED_FOOD_CATEGORY_FROM_HOTEL_DETAILS_PHP_URL;

        Map<String, String> map = new HashMap<>();
        map.put(Json_Hotels_of_Searched_Station.HOTEL_ID,hotel_id);
        map.put(Json_Food_of_Clicked_Category.FOOD_CATEGORY_NAME,food_category_name);

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try{
                    JSONObject object = new JSONObject(string);

                    Log.e("CLICKED_CATEGORY2","<>"+string);

                    food_ArrayList = new Food_of_Clicked_Category_Utils().Get_all_Food_clicked_Category(object);

                    set_recycler_view();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("CLICKED_CATEGORY2","<><>"+e);
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

        if (food_ArrayList != null) {
            adapter_clicked_category = new Adapter_for_clicked_category_show_food_recycler_view(getActivity(), food_ArrayList);

            ((Adapter_for_clicked_category_show_food_recycler_view) adapter_clicked_category).setClickListener(this);
            recyclerView_clicked_category.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView_clicked_category.setAdapter(adapter_clicked_category);

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            recyclerView_clicked_category.setVisibility(View.VISIBLE);

        } else {
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);

            linearLayout_error_message.setVisibility(View.VISIBLE);
            recyclerView_clicked_category.setVisibility(View.GONE);
        }
    }
    @Override
    public void itemClicked(View view){

        if (!clicked) {

            ImageView imageView = view.findViewById(R.id.imageView_of_clicked_category_food);
            TextView textView_food_name = view.findViewById(R.id.textview_name_of_clicked_category_food_name);
            TextView textView_food_id = view.findViewById(R.id.textView_food_id);

            String Name_of_food = textView_food_name.getText().toString();

            Intent intent = new Intent(getActivity(), ItemClickedActivity.class);
            intent.putExtra("FOOD_NAME", "" + Name_of_food);
            intent.putExtra("FOOD_ID", "" + textView_food_id.getText().toString());

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
