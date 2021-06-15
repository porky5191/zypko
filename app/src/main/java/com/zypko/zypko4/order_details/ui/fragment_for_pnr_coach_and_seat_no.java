package com.zypko.zypko4.order_details.ui;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.order_details.urils.Get_All_Train_Utils;
import com.zypko.zypko4.payment.ui.fragment_for_payment;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.Context.VIBRATOR_SERVICE;

public class fragment_for_pnr_coach_and_seat_no extends Fragment {

    View root;
    EditText et_pnr,et_coach,et_seat_no;
    Button btn_conform;
    AutoCompleteTextView et_train_no;

    String[] all_train ;
    String str_pnr,str_train_no,str_coach,str_seat_no;

    ProgressBar progressBar;
    Vibrator vibrator;
    Database myDB;

    String water_bottle,delivery_charge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_pnr_or_user_address,container,false);

            initialise();
        }

        return root;
    }

    private void initialise() {

        myDB = new Database(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            water_bottle    = bundle.getString("WATER_BOTTLE");
            delivery_charge = bundle.getString("DELIVERY_CHARGE");
        }

        Toolbar toolbar = root.findViewById(R.id.toolbar_pnr);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Enter Order Details");
        toolbar.setTitleTextColor(getActivity().getResources().getColor(R.color.colorBlue_tez));

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

        et_pnr                  = root.findViewById(R.id.edit_tex_pnr_number_on_station_or_on_train);
        et_coach                = root.findViewById(R.id.edit_text_coach_on_station_or_on_train);
        et_train_no             = root.findViewById(R.id.edit_text_train_number_on_station_or_on_train);
        et_seat_no              = root.findViewById(R.id.edit_text_seat_number_on_station_or_on_train);
        btn_conform             = root.findViewById(R.id.button_on_station_or_on_train);

        progressBar             = root.findViewById(R.id.progress_bar_pnr);
        vibrator                =  (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

        set_up();
    }

    private void set_up() {

        String url = UrlValues.GET_ALL_TRAIN_DETAILS ;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                Log.e("TRAIN ALL",""+object);

                try{
                    int success = object.getInt(Json_Objects.SUCCESS);

                    if (success == 1) {

                        all_train = new Get_All_Train_Utils().Get_train_details(object);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,all_train);

                        et_train_no.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getActivity(), "Something is wrong !!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_pnr = et_pnr.getText().toString();

                if ( Check_train_no() && Check_seat_no() && Check_coach()) {

                    btn_conform.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    if (Arrays.asList(all_train).contains(et_train_no.getText().toString()))  {
                        go_to_payment();

                    }else {

                        et_train_no.requestFocus();
                        et_train_no.setError("Invalid Train Number/Name");

                        vibrator.vibrate(200);
                        Toast.makeText(getActivity(), "Please choose the Train Number/Name from the list.", Toast.LENGTH_SHORT).show();
                    }

                    btn_conform.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }else {

                        vibrator.vibrate(200);
                }

            }
        });

    }

    private void go_to_payment() {

        Bundle bundle = new Bundle();

        bundle.putString("FRAGMENT",        "TRAIN_DETAILS");
        bundle.putString("PNR",             ""+et_pnr.getText().toString());
        bundle.putString("TRAIN_NO",        ""+et_train_no.getText().toString().substring(0,5));
        bundle.putString("COACH",           ""+et_coach.getText().toString());
        bundle.putString("SEAT_NO",         ""+et_seat_no.getText().toString());
        bundle.putString("WATER_BOTTLE",    ""+water_bottle);
        bundle.putString("DELIVERY_CHARGE", ""+delivery_charge);

        new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_payment(),bundle);
    }

    private boolean Check_pnr(){

        str_pnr = et_pnr.getText().toString();

        if (str_pnr.isEmpty()) {

            et_pnr.setError("PNR is empty");
            et_pnr.requestFocus();
            str_pnr = "N/A";

            return false;
        } else if (str_pnr.length() != 10) {

            et_pnr.setError("PNR is invalid");
            et_pnr.requestFocus();
            return false;
        }

        return true;
    }

    private boolean Check_train_no(){

        str_train_no = et_train_no.getText().toString();

        if (str_train_no.isEmpty()) {

            et_train_no.setError("Train Number is empty");
            et_train_no.requestFocus();

            return false;
        }

        return true;
    }

    private boolean Check_coach(){

        str_coach = et_coach.getText().toString();

        if (str_coach.isEmpty()) {

            et_coach.setError("Coach is empty");
            et_coach.requestFocus();

            return false;
        }

        return true;
    }

    private boolean Check_seat_no(){

        str_seat_no = et_seat_no.getText().toString();

        if (str_seat_no.isEmpty()) {

            et_seat_no.setError("Seat Number is empty");
            et_seat_no.requestFocus();

            return false;
        } else if (str_seat_no.length() != 2) {

            et_seat_no.setError("Seat Number is invalid");
            et_seat_no.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onPause() {
        super.onPause();
        vibrator.cancel();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }
}
