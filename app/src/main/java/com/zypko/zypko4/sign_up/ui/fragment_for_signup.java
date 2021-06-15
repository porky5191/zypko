package com.zypko.zypko4.sign_up.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.sign_up.utils.User_Details;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;

public class fragment_for_signup extends Fragment {

    View root;
    EditText et_phone_number, et_email, et_name, et_password, et_referral;
    //CheckBox chk_referral;
    Button button_complete;
    String str_email, str_name, str_password, str_referral;
    ProgressBar progressBar;
    ArrayList<User_Details> user_ArrayList;

    String input_phone_number;
    Vibrator vibrator;
    String FRAGMENT;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_sign_up, container, false);

            initialise();
        }
        return root;
    }

    private void initialise() {

        Toolbar toolbar = root.findViewById(R.id.toolbar_sign_up);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        et_phone_number = root.findViewById(R.id.edit_text_sign_up_phone_number);
        et_email = root.findViewById(R.id.edit_text_sign_up_email);
        et_name = root.findViewById(R.id.edit_text_sign_up_Name);
        et_password = root.findViewById(R.id.edit_text_sign_up_password);
        et_referral = root.findViewById(R.id.edit_text_sign_up_referral);


        //chk_referral = root.findViewById(R.id.check_box_sign_up);
        button_complete = root.findViewById(R.id.sign_up_button_complete);
        button_complete.setFocusable(true);

        progressBar = root.findViewById(R.id.progress_bar_signup);
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

        set_up();
    }

    private void set_up() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            FRAGMENT = bundle.getString("FRAGMENT");
        }


        ///************************************** for further use  **********************************////
//        chk_referral.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (chk_referral.isChecked()) {
//                    et_referral.setVisibility(View.VISIBLE);
//                } else {
//                    et_referral.setVisibility(View.GONE);
//                }
//            }
//        });


        button_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new CheckInternet(getActivity()).Internet()) {
                    if (checkEmail() && checkName() && checkPassword() && checkNumber()) {

                        button_complete.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        send_user_data_to_server();

                    } else {

                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(200);
                        }
                    }
                } else {
                    new AlertDialog_For_NoInternet(getActivity()).AlertDialog();
                }
            }
        });
    }

    private void send_user_data_to_server() {

        String url = UrlValues.CHECK_USER_PHONE_NO+"?PHONE_NO="+ input_phone_number;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                try {
                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 0) {

                        progressBar.setVisibility(View.GONE);
                        button_complete.setVisibility(View.VISIBLE);

                        String token = new PrefEditor(getActivity()).getString(getString(R.string.FCM_TOKEN));

                        Bundle bundle = new Bundle();

                        bundle.putString("FULL_PHONE_NO"        , "+91" + input_phone_number);
                        bundle.putString("USER_NAME"            , "" + str_name);
                        bundle.putString("INPUT_PHONE_NO"       , "" + input_phone_number);
                        bundle.putString("USER_EMAIL"           , "" + str_email);
                        bundle.putString("USER_PASSWORD"        , "" + str_password);
                        bundle.putString("USER_REFERRAL_CODE"   , "" + str_referral);
                        bundle.putString("USER_FCM_TOKEN"       , "" + token);
                        bundle.putString("FRAGMENT_1"           , "FRAGMENT_SIGN_UP");
                        bundle.putString("FRAGMENT"             , FRAGMENT);

                        new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_verify_OTP(), bundle);

                    } else if (success == 1) {
                        Toast.makeText(getActivity(), "Number Already exist", Toast.LENGTH_SHORT).show();

                        et_phone_number.setError("Number Already exist");
                        et_phone_number.requestFocus();

                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(200);
                        }

                        progressBar.setVisibility(View.GONE);
                        button_complete.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity(), "Sign-Up Unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        button_complete.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    progressBar.setVisibility(View.GONE);
                    button_complete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                button_complete.setVisibility(View.VISIBLE);
            }
        });

    }

    private boolean checkReferral() {

        str_referral = et_referral.getText().toString().trim();

//        if (chk_referral.isChecked()) {
//
//            if (str_referral.isEmpty()) {
//
//                et_referral.setError("No Referral Code Entered");
//                et_referral.requestFocus();
//
//                return false;
//
//            } else if (str_referral.length() != 8) {
//
//                et_referral.setError("Invalid Referral Code");
//                et_referral.requestFocus();
//
//                return false;
//            }
//        }
        return true;
    }

    private boolean checkPassword() {

        str_password = et_password.getText().toString().trim();

        if (str_password.isEmpty()) {

            et_password.setError("No Password Entered");
            et_password.requestFocus();

            return false;

        } else if (str_password.length() < 6) {

            et_password.setError("Minimum 6 character");
            et_password.requestFocus();

            return false;
        }
        return true;

    }

    private boolean checkName() {

        str_name = et_name.getText().toString().trim();

        if (str_name.isEmpty()) {

            et_name.setError("No Name Entered");
            et_name.requestFocus();
            return false;

        }
        return true;

    }

    private boolean checkEmail() {

        str_email = et_email.getText().toString().trim();

        if (str_email.isEmpty()) {
            str_email = "Email";
        }
        return true;
    }

    private boolean checkNumber() {

        input_phone_number = et_phone_number.getText().toString().trim();

        if (input_phone_number.isEmpty()) {

            et_phone_number.setError("No Number Entered");
            et_phone_number.requestFocus();
            return false;

        } else if (input_phone_number.length() != 10) {

            et_phone_number.setError("Not a Valid Number");
            et_phone_number.requestFocus();
            return false;

        }

        return true;
    }
}
