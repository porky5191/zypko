package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.user_account.ui.fragment_for_user_account_details;

public class UserAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_for_user_account_details());

    }



}
