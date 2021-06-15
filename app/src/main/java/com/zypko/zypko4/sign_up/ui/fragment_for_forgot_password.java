package com.zypko.zypko4.sign_up.ui;

import android.os.Bundle;
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
import com.zypko.zypko4.sign_up.utils.Sign_Up_Utils;
import com.zypko.zypko4.sign_up.utils.User_Details;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fragment_for_forgot_password extends Fragment {

    View root;
    Toolbar toolbar;
    EditText et_phone_number;
    Button btn_submit;

    String phone_number;
    ProgressBar progressBar;
    ArrayList<User_Details> user_ArrayList;
    String FRAGMENT;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_forgot_password, container, false);

            initialise();
        }

        return root;
    }

    private void initialise() {

        Bundle bundle = getArguments();

        if (bundle != null) {
            FRAGMENT = bundle.getString("FRAGMENT");
        }

        toolbar         = root.findViewById(R.id.toolbar_forgot_password);
        et_phone_number = root.findViewById(R.id.et_forgot_password_phone_no);
        btn_submit      = root.findViewById(R.id.btn_forgot_password_submit);
        progressBar     = root.findViewById(R.id.progress_bar_fot_got_password);

        set_up();

    }

    private void set_up() {

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


        btn_submit.setFocusable(true);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new CheckInternet(getActivity()).Internet()) {

                    if (Check_phone_number()) {

                        check_in_server();
                    }
                }else {

                    new AlertDialog_For_NoInternet(getActivity()).AlertDialog();

                }

            }
        });

    }

    private boolean Check_phone_number() {

        phone_number = et_phone_number.getText().toString();

        if (phone_number.isEmpty()) {

            et_phone_number.setError("Not entered Phone number");
            et_phone_number.requestFocus();

            return false;

        } else if (phone_number.length() != 10) {

            et_phone_number.setError("Wrong Number");
            et_phone_number.requestFocus();

            return false;
        }

        return true;
    }

    private void check_in_server() {

        btn_submit.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        btn_submit.setClickable(false);

        String url = UrlValues.CHECK_USER_PHONE_NO_FOR_FORGOT_PASSWORD + "?PHONE_NO=" + phone_number;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                try {
                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 1) {

                        user_ArrayList = new Sign_Up_Utils(getActivity()).Get_only_user_id(object);
                        user_exists();

                    } else {
                        user_does_not_exists();
                    }
                    progressBar.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                    btn_submit.setClickable(true);

                } catch (JSONException e) {
                    e.printStackTrace();

                    progressBar.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                    btn_submit.setClickable(true);
                }
            }

            @Override
            public void onError(VolleyError error) {

                progressBar.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setClickable(true);
            }
        });


    }

    private void user_does_not_exists() {

        et_phone_number.setError("Phone Number is invalid");
        et_phone_number.requestFocus();

    }

    private void user_exists() {


        if (user_ArrayList != null) {

            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_ID      , user_ArrayList.get(0).getUser_id());

            Bundle bundle = new Bundle();
            bundle.putString("FRAGMENT_1","FRAGMENT_FORGOT_PASSWORD");
            bundle.putString("FRAGMENT",FRAGMENT);
            bundle.putString("FULL_PHONE_NO", "+91" + et_phone_number.getText().toString());

            new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_verify_OTP(), bundle);

        }else{

            Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_SHORT).show();
        }

    }

}
