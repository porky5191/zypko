package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.item_clicked.ui.fragment_item_clicked;

public class ItemClickedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_clicked);

        Bundle bundle = getIntent().getExtras();

        Fragment fragment = new fragment_item_clicked();
        fragment.setArguments(bundle);

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(fragment);

    }
}
