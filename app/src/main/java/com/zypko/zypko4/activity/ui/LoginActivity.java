package com.zypko.zypko4.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.json_Objects.Json_User_Details;
import com.zypko.zypko4.sign_up.ui.fragment_for_login;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Bundle bundle = getIntent().getExtras();

        if ((new PrefEditor(this).getLong(Json_Shared_Preference.USER_ID) != 0) && ( !new PrefEditor(this).getString(Json_Shared_Preference.USER_NAME).isEmpty()) )  {

            startActivity(new Intent(LoginActivity.this,UserAccountActivity.class));
            finish();

        }else {
            new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_for_login(),bundle);
        }


    }
}
