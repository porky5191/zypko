package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.clicked_random_food.ui.fragment_clicked_random_food_result;
import com.zypko.zypko4.globals.FragmentOpener;

public class ClickedRandomFoodActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_random_food);

        Bundle bundle = getIntent().getExtras();
        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_clicked_random_food_result(),bundle);
    }
}
