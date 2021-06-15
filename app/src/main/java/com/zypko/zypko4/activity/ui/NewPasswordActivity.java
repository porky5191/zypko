package com.zypko.zypko4.activity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.json_Objects.Json_User_Details;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.Post_to_Server;
import com.zypko.zypko4.sign_up.ui.fragment_for_verify_OTP;
import com.zypko.zypko4.sign_up.utils.Sign_Up_Utils;
import com.zypko.zypko4.sign_up.utils.User_Details;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    Button btn_submit;
    EditText et_pass_1,et_pass_2;
    ProgressBar progressBar;

    String pass1,pass2;
    ArrayList<User_Details> user_ArrayList;

    Bundle bundle;
    String FRAGMENT;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);


        initialise();
    }

    private void initialise() {

        bundle = getIntent().getExtras();

        if (bundle != null) {
            FRAGMENT = bundle.getString("FRAGMENT");
        }

        btn_submit      = findViewById(R.id.btn_new_password_submit);
        et_pass_1       = findViewById(R.id.et_new_password_password_1);
        et_pass_2       = findViewById(R.id.et_new_password_password_2);
        progressBar     = findViewById(R.id.progress_bar_new_password);

        set_up();
    }

    private void set_up() {

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new CheckInternet(NewPasswordActivity.this).Internet()) {

                    if (Check_password_1() && Check_password_2()) {

                        if (pass1.equals(pass2)) {

                            progressBar.setVisibility(View.VISIBLE);
                            btn_submit.setVisibility(View.GONE);
                            set_password();

                        }else {
                            et_pass_2.setError("Password do not match");
                            et_pass_2.requestFocus();
                        }
                    }

                }else {
                    new AlertDialog_For_NoInternet(NewPasswordActivity.this).AlertDialog();
                }
            }
        });

    }

    private void set_password() {

        String url = UrlValues.SET_USER_NEW_PASSWORD;

        Map<String, String> map = new HashMap<>();
        map.put("USER_ID",""+new PrefEditor(this).getLong(Json_Shared_Preference.USER_ID) );
        map.put("USER_PASSWORD",pass1);

        Post_to_Server pst = new Post_to_Server(this,map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);

                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 1) {

                        user_ArrayList = new Sign_Up_Utils(NewPasswordActivity.this).Get_details_of_user(object);

                        save_shared_pref();

                    }else {
                        Toast.makeText(NewPasswordActivity.this, "Password changed Unsuccessful", Toast.LENGTH_SHORT).show();

                    }

                    progressBar.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(NewPasswordActivity.this, "Password changed catch", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(NewPasswordActivity.this, "Password changed error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        });
    }

    private void save_shared_pref() {

        if (user_ArrayList != null) {

            new PrefEditor(this).writeData(Json_Shared_Preference.USER_ID, user_ArrayList.get(0).getUser_id());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_NAME, user_ArrayList.get(0).getUser_name());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_EMAIL, user_ArrayList.get(0).getUser_email());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_PHONE_NO, user_ArrayList.get(0).getUser_phone_no());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_PASSWORD, user_ArrayList.get(0).getUser_password());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_IMAGE, user_ArrayList.get(0).getUser_image());
            new PrefEditor(this).writeData(Json_Shared_Preference.USER_REFERRAL, user_ArrayList.get(0).getUser_referral());

            Toast.makeText(NewPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            Log.e("FRAGMENT","Ffff:"+FRAGMENT);

            if (FRAGMENT.equals("PAYMENT")) {
                Intent intent = new Intent(this,PaymentActivity.class);
                intent.putExtra("FRAGMENT","NEW_PASSWORD");
                startActivity(intent);

            } else {
                startActivity(new Intent(NewPasswordActivity.this,UserAccountActivity.class));

            }


            finish();

        }else{

            Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean Check_password_1() {

        pass1 = et_pass_1.getText().toString();

        if (pass1.isEmpty()) {

            et_pass_1.setError("Password not entered");
            et_pass_1.requestFocus();

            return false;

        } else if (pass1.length() < 6) {

            et_pass_1.setError("Password is too Small");
            et_pass_1.requestFocus();

            return false;
        }

        return true;
    }

    private boolean Check_password_2() {

        pass2 = et_pass_2.getText().toString();

        if (pass2.isEmpty()) {

            et_pass_2.setError("Password not entered");
            et_pass_2.requestFocus();

            return false;

        } else if (pass2.length() < 6) {

            et_pass_2.setError("Password is too Small");
            et_pass_2.requestFocus();

            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(NewPasswordActivity.this,RandomHotelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
