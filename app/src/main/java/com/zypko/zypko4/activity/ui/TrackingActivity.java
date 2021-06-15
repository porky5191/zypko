package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.food_tracking.ui.fragment_for_food_tracking;
import com.zypko.zypko4.globals.FragmentOpener;

public class TrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Bundle bundle = getIntent().getExtras();
        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_for_food_tracking(),bundle);
    }
}
