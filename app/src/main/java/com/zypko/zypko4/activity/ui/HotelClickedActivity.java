package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.clicked_hotel.ui.fragment_for_clicked_hotel;
import com.zypko.zypko4.globals.FragmentOpener;

public class HotelClickedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_clicked);

        Bundle bundle = getIntent().getExtras();
        Fragment fragment = new fragment_for_clicked_hotel();
        fragment.setArguments(bundle);

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(fragment);

    }
}
