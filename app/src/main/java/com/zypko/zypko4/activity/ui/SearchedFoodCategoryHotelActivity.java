package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.searched_food_category_hotel.ui.fragment_result_searched_food_category_hotel;

public class SearchedFoodCategoryHotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_food_category_hotel);

        Bundle bundle = getIntent().getExtras();

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_result_searched_food_category_hotel(),bundle);
    }
}

