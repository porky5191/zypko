package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.show_searched_station_result_hotels.ui.fragment_Searched_station_result_hotels;

public class SearchedStationResultHotelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_station_result_holets);

        Bundle bundle = getIntent().getExtras();

        Fragment fragment = new fragment_Searched_station_result_hotels();
        fragment.setArguments(bundle);

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(fragment);
    }
}
