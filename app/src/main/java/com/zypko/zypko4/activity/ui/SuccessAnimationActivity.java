package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.food_tracking.ui.fragment_for_success_animation;
import com.zypko.zypko4.globals.FragmentOpener;

public class SuccessAnimationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_animation);

        Bundle bundle = getIntent().getExtras();

        new FragmentOpener(this).setManager(getSupportFragmentManager()).open_Replace_Backstack(new fragment_for_success_animation(),bundle);


    }
}
