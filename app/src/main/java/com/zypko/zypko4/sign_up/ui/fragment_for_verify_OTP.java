package com.zypko.zypko4.sign_up.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.NewPasswordActivity;
import com.zypko.zypko4.activity.ui.PaymentActivity;
import com.zypko.zypko4.activity.ui.UserAccountActivity;
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
import java.util.concurrent.TimeUnit;

public class fragment_for_verify_OTP extends Fragment implements View.OnKeyListener {
    View root;

    EditText  editText_otp;
    TextView textView_resend, textView_otp_send_number,tv_count_down;
    Button btn_complete;
    ProgressBar progressBar;

    ArrayList<User_Details> user_ArrayList;
    String input_phone_number, full_phone_number, verification_id;
    String FRAGMENT,FRAGMENT_1;
    String str_email, str_name, str_password, str_referral, fcm_token;

    CountDownTimer countDownTimer;
    long timeleftInMiliSecond;


    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_verify_otp, container, false);

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            initialise();

        }

        return root;
    }

    private void initialise() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            full_phone_number   = bundle.getString("FULL_PHONE_NO");
            input_phone_number  = bundle.getString("INPUT_PHONE_NO");
            FRAGMENT_1          = bundle.getString("FRAGMENT_1");
            FRAGMENT            = bundle.getString("FRAGMENT");
            str_name            = bundle.getString("USER_NAME");
            str_email           = bundle.getString("USER_EMAIL");
            str_password        = bundle.getString("USER_PASSWORD");
            str_referral        = bundle.getString("USER_REFERRAL_CODE");
            fcm_token           = bundle.getString("USER_FCM_TOKEN");

            Log.e("FULL_PHONE_NO",""+full_phone_number);

        }

        mAuth                       = FirebaseAuth.getInstance();
        editText_otp                = root.findViewById(R.id.edit_text_otp);

        progressBar                 = root.findViewById(R.id.progress_bar_OTP_verify);
        btn_complete                = root.findViewById(R.id.btn_otp_complete);
        textView_resend             = root.findViewById(R.id.text_view_verify_otp_resend);
        textView_otp_send_number    = root.findViewById(R.id.text_view_otp_sent_number);
        tv_count_down               = root.findViewById(R.id.tv_count_down_verify_otp_resend);

        Toolbar toolbar = root.findViewById(R.id.toolbar_otp);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        set_up();
        sendVerificationCode();
    }

    private void set_up() {

        textView_otp_send_number.setText(full_phone_number);

        textView_resend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                timeleftInMiliSecond = 30000;
                textView_resend.setVisibility(View.GONE);
                sendVerificationCode();
                tv_count_down.setVisibility(View.VISIBLE);
                start_Countdown();
                Toast.makeText(getActivity(), "Code Sent", Toast.LENGTH_SHORT).show();
            }
        });



        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_otp.getText().toString().length() < 6){
                    Toast.makeText(getActivity(), "OTP is not correct", Toast.LENGTH_SHORT).show();

                } else {

                    String code = String.valueOf(editText_otp.getText());

                    progressBar.setVisibility(View.VISIBLE);
                    btn_complete.setVisibility(View.GONE);

                    verifyCode(code);

                }
            }
        });
    }

    private void start_Countdown() {

        countDownTimer = new CountDownTimer(timeleftInMiliSecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftInMiliSecond = millisUntilFinished;

                int second = (int) (timeleftInMiliSecond / 1000);

                String time ;

                time = "Try after  00:";
                if (second < 10) {
                    time = time + "0";
                }
                time = time + ""+second;

                tv_count_down.setText(time);
            }

            @Override
            public void onFinish() {

                textView_resend.setVisibility(View.VISIBLE);
                tv_count_down.setVisibility(View.GONE);

            }
        }.start();
    }

    ///////////*************************************************************************************************************///////////

    private void sendVerificationCode() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(full_phone_number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verification_id = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                editText_otp.setText(code);
                verifyCode(code);

            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Log.e("VERIFICATION_FAILED",""+e);
        }

    };

    private void verifyCode(String code) {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, code);
            SignInWithCredential(credential);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Verification code is not sent to your phone number. Please Resend ", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_complete.setVisibility(View.VISIBLE);
        }
    }

    private void SignInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.e("TASK",""+task);
                Log.e("TASK",""+task.isSuccessful());

                if (task.isSuccessful()) {

                    if (FRAGMENT_1.equals("FRAGMENT_FORGOT_PASSWORD")) {

                        Intent intent = new Intent(getActivity(), NewPasswordActivity.class);
                        intent.putExtra("FRAGMENT",FRAGMENT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();

                    } else if (FRAGMENT_1.equals("FRAGMENT_SIGN_UP") && FRAGMENT.equals("USER_ACCOUNT")) {


                        String url = UrlValues.INSERT_USER_DETAILS;

                        Map<String, String> map = new HashMap<>();
                        map.put("USER_NAME",str_name);
                        map.put("USER_PHONE_NO",input_phone_number);
                        map.put("USER_EMAIL",str_email);
                        map.put("USER_PASSWORD",str_password);
                        map.put("USER_REFERRAL_CODE",str_referral);
                        map.put("USER_FCM_TOKEN",fcm_token);

                        Post_to_Server pst = new Post_to_Server(getActivity(),map);
                        pst.getJson(url, new HTTP_Post_Callback() {
                            @Override
                            public void onSuccess(String string) {

                                try {
                                    JSONObject object = new JSONObject(string);

                                    int success = object.getInt(Json_Objects.SUCCESS);

                                    if (success == 1) {

                                        user_ArrayList = new Sign_Up_Utils(getActivity()).Get_details_of_user(object);

                                        set_shared_preferances();

                                        Log.e("FRAGMENT_NAME", FRAGMENT);

                                        Intent intent = new Intent(getActivity(), UserAccountActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().finish();

                                        progressBar.setVisibility(View.GONE);
                                        btn_complete.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
                                    btn_complete.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                btn_complete.setVisibility(View.VISIBLE);
                            }
                        });

                    } else if (FRAGMENT_1.equals("FRAGMENT_SIGN_UP") && FRAGMENT.equals("PAYMENT") ){

                        String url = UrlValues.INSERT_USER_DETAILS;

                        Map<String, String> map = new HashMap<>();
                        map.put("USER_NAME",str_name);
                        map.put("USER_PHONE_NO",input_phone_number);
                        map.put("USER_EMAIL",str_email);
                        map.put("USER_PASSWORD",str_password);
                        map.put("USER_REFERRAL_CODE",str_referral);
                        map.put("USER_FCM_TOKEN",fcm_token);

                        Post_to_Server pst = new Post_to_Server(getActivity(),map);
                        pst.getJson(url, new HTTP_Post_Callback() {
                            @Override
                            public void onSuccess(String string) {

                                try {
                                    JSONObject object = new JSONObject(string);

                                    int success = object.getInt(Json_Objects.SUCCESS);

                                    if (success == 1) {

                                        user_ArrayList = new Sign_Up_Utils(getActivity()).Get_details_of_user(object);

                                        set_shared_preferances();

                                        Log.e("FRAGMENT_NAME", FRAGMENT_1);

                                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                        intent.putExtra("FRAGMENT","VERIFY_OTP");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        getActivity().finish();

                                        progressBar.setVisibility(View.GONE);
                                        btn_complete.setVisibility(View.VISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressBar.setVisibility(View.GONE);
                                    btn_complete.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                btn_complete.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                }  else {

                    // Alert Dialog for wrong code entry in OTP
                    progressBar.setVisibility(View.GONE);
                    btn_complete.setVisibility(View.VISIBLE);

                    if (getActivity() != null) {


                        android.app.AlertDialog.Builder builder;

                        builder = new android.app.AlertDialog.Builder(getActivity());

                        builder.setTitle("Verification Failed");
                        builder.setMessage("Please check the code and type again.");
                        builder.setCancelable(true);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez_dark));
                        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez_dark));


                    }
                }

            }
        });

    }


    private void set_shared_preferances() {

        if (user_ArrayList != null) {

            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_ID, user_ArrayList.get(0).getUser_id());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_NAME, user_ArrayList.get(0).getUser_name());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_EMAIL, user_ArrayList.get(0).getUser_email());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_PHONE_NO, user_ArrayList.get(0).getUser_phone_no());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_PASSWORD, user_ArrayList.get(0).getUser_password());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_IMAGE, user_ArrayList.get(0).getUser_image());
            new PrefEditor(getActivity()).writeData(Json_Shared_Preference.USER_REFERRAL, user_ArrayList.get(0).getUser_referral());

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            if (FRAGMENT.equals("FRAGMENT_FORGOT_PASSWORD")) {

                Intent intent = new Intent(getActivity(), NewPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            } else if (FRAGMENT.equals("FRAGMENT_SIGN_UP")) {

                Intent intent = new Intent(getActivity(), UserAccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

            }
        }
    }


    private void backPressed(){

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        }else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Verification Cancel");
        builder.setMessage("Do you want to cancel verification ?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().onBackPressed();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));


    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Toast.makeText(getActivity(), "Wait for verification", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

}
