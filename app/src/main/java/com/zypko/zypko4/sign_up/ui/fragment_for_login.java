package com.zypko.zypko4.sign_up.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.PaymentActivity;
import com.zypko.zypko4.activity.ui.UserAccountActivity;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.Post_to_Server;
import com.zypko.zypko4.sign_up.utils.Sign_Up_Utils;
import com.zypko.zypko4.sign_up.utils.User_Details;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.VIBRATOR_SERVICE;

public class fragment_for_login extends Fragment {

    View root;
    TextView tv_signUp, tv_forgot_password;
    Button btn_login;
    EditText et_phone, et_password;
    ProgressBar progressBar;

    String phone_number, password;
    ArrayList<User_Details> user_ArrayList;
    Vibrator vibrator;
    int passwordNotVisible = 1;

    String FRAGMENT;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_check_login_or_signup, container, false);

            initialise();
        }
        return root;
    }


    private void initialise() {

        tv_forgot_password  = root.findViewById(R.id.textview_login_forgot_password);
        tv_signUp = root.findViewById(R.id.textview_login_signup);
        btn_login           = root.findViewById(R.id.button_login_login);
        et_phone            = root.findViewById(R.id.edit_text_login_phone_no);
        et_password         = root.findViewById(R.id.edit_text_login_password);
        progressBar         = root.findViewById(R.id.progress_bar_login);

        //textInputLayout_phon_no = root.findViewById(R.id.phone_no_text_Input_Layout);
        vibrator            = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

        set_view();

    }

    private void set_view() {

        final Bundle bundle = getArguments();

        if (bundle != null) {
            FRAGMENT = bundle.getString("FRAGMENT");
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new CheckInternet(getActivity()).Internet()) {
                    if (check_Number() && check_password()) {

                        check_in_server();
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


        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_password.getRight() - et_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (passwordNotVisible == 0) {
                            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            et_password.setSelection(et_password.length());
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_password, 0);
                            passwordNotVisible = 1;

                        }else {
                            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            et_password.setSelection(et_password.length());
                            et_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.show_password, 0);
                            passwordNotVisible = 0;
                        }

                        return true;
                    }
                }
                return false;
            }
        });


        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_signup(),bundle);
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_forgot_password(),bundle);
            }
        });

    }

    private boolean check_password() {

        password = et_password.getText().toString();

        if (password.isEmpty()) {

            et_password.setError("Not entered Password");
            et_password.requestFocus();

            return false;
        }

        return true;
    }

    private boolean check_Number() {

        phone_number = et_phone.getText().toString();

        if (phone_number.isEmpty()) {

            et_phone.setError("Not entered Phone number");
            et_phone.requestFocus();

            return false;

        } else if (phone_number.length() != 10) {

            et_phone.setError("Wrong Number");
            et_phone.requestFocus();

            return false;
        }

        return true;
    }

    private void check_in_server() {

        btn_login.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tv_signUp.setClickable(false);

        String url = UrlValues.CHECK_USER_DETAILS;//+"?PHONE_NO="+phone_number+"&PASSWORD"+password+"&USER_FCM_TOKEN="+new PrefEditor(getActivity()).getString(getString(R.string.FCM_TOKEN));

        Map<String, String> map = new HashMap<>();
        map.put("PHONE_NO",phone_number);
        map.put("PASSWORD",password);
        map.put("USER_FCM_TOKEN",new PrefEditor(getActivity()).getString(getString(R.string.FCM_TOKEN)));

        Post_to_Server pst = new Post_to_Server(getActivity(),map);
        pst.getJson(url, new HTTP_Post_Callback() {
            @Override
            public void onSuccess(String string) {

                try {
                    JSONObject object = new JSONObject(string);

                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 1) {

                        user_ArrayList = new Sign_Up_Utils(getActivity()).Get_details_of_user(object);

                        user_exists();

                    } else {
                        user_does_not_exists();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    progressBar.setVisibility(View.GONE);
                    btn_login.setVisibility(View.VISIBLE);
                    tv_signUp.setClickable(true);
                }

            }

            @Override
            public void onError(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                tv_signUp.setClickable(true);
            }
        });


//        JSONProvider provider = new JSONProvider(getActivity());
//        provider.getJson(url, new HTTP_Get() {
//            @Override
//            public void onSuccess(JSONObject object) {
//
//
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//
//
//            }
//        });


    }

    private void user_exists() {

        progressBar.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);
        tv_signUp.setClickable(true);


        if (user_ArrayList != null) {

            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_ID, user_ArrayList.get(0).getUser_id());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_NAME, user_ArrayList.get(0).getUser_name());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_EMAIL, user_ArrayList.get(0).getUser_email());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_PHONE_NO, user_ArrayList.get(0).getUser_phone_no());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_PASSWORD, user_ArrayList.get(0).getUser_password());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_IMAGE, user_ArrayList.get(0).getUser_image());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_REFERRAL, user_ArrayList.get(0).getUser_referral());


            if (FRAGMENT.equals("PAYMENT")) {
                Intent intent = new Intent(getActivity(),PaymentActivity.class);
                intent.putExtra("FRAGMENT","LOGIN");
                getActivity().startActivity(intent);

            } else if (FRAGMENT.equals("USER_ACCOUNT")) {
                getActivity().startActivity(new Intent(getActivity(), UserAccountActivity.class));

            }
            getActivity().finish();
        }
    }

    private void user_does_not_exists() {

        progressBar.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);
        tv_signUp.setClickable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Error");
        builder.setMessage("Phone number or Password is incorrect..!! Please check and try again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

}
