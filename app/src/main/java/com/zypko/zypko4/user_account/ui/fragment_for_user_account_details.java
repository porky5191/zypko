package com.zypko.zypko4.user_account.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.Prefs;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;
import com.zypko.zypko4.user_account.utils.Adapter_for_past_orders;
import com.zypko.zypko4.user_account.utils.Adapter_for_recent_order;
import com.zypko.zypko4.user_account.utils.Past_Order;
import com.zypko.zypko4.user_account.utils.Past_Orders_Utils;
import com.zypko.zypko4.user_account.utils.Recent_Order;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.zypko.zypko4.user_account.ui.fragment_for_user_image_display.IMG_REQUEST;

public class fragment_for_user_account_details extends Fragment implements View.OnKeyListener,Adapter_for_recent_order.ClickListener {

    View root;
    RecyclerView recyclerView_past_orders,recyclerView_recent_orders;
    RecyclerView.Adapter adapter,adapter_recent;
    RecyclerView.LayoutManager layoutManager,layoutManager_recent;

    TextView tv_help,tv_past_orders,tv_call,tv_facebook,tv_instagram,tv_whatsapp,tv_gmail, tv_recent_order;
    TextView tv_user_name,tv_user_phone,tv_user_email,tv_logout;
    ImageView imageView_user_image,imageView_secondary_image;
    View view1,view2,view4,view5,view6,view7;
    boolean isPressed_help = false;
    boolean isPressed_past_order = false;
    boolean isPressed_recent_order = false;
    private Bitmap bitmap;

    ArrayList<Past_Order> past_orders_ArrayList;
    ArrayList<Recent_Order> recent_orders_ArrayList;
    int request_call = 1,request_sms = 2;

    ProgressBar progressBar_recent,progressBar_past;
    TextView tv_no_past_order,tv_no_recent_order;
    int recent_request=0,past_request=0;

    ProgressDialog progressDialog;

    ///////********************************************************************************************/////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_user_account_details,container,false);

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            initialise();
        }
        return root;
    }


    private void initialise() {

        tv_help                 = root.findViewById(R.id.textView_user_account_help);
        tv_past_orders          = root.findViewById(R.id.textView_past_orders);
        tv_recent_order         = root.findViewById(R.id.textView_recent_orders);
        tv_call                 = root.findViewById(R.id.textView_user_account_call);
        //tv_message              = root.findViewById(R.id.textView_user_account_message);
        tv_facebook             = root.findViewById(R.id.textView_user_account_facebook);
        tv_instagram            = root.findViewById(R.id.textView_user_account_instagram);
        tv_whatsapp             = root.findViewById(R.id.textView_user_account_whatsapp);
        tv_gmail                = root.findViewById(R.id.textView_user_account_gmail);
        imageView_secondary_image = root.findViewById(R.id.image_view_secondary_user_image);

        imageView_user_image    = root.findViewById(R.id.imageView_user_image);

        view1                   = root.findViewById(R.id.view_1);
        view2                   = root.findViewById(R.id.view_2);
        //view3                   = root.findViewById(R.id.view_3);
        view4                   = root.findViewById(R.id.view_4);
        view5                   = root.findViewById(R.id.view_5);
        view6                   = root.findViewById(R.id.view_6);

        tv_user_name            = root.findViewById(R.id.textView_user_account_user_name);
        tv_user_phone           = root.findViewById(R.id.textView_user_account_user_phone_no);
        tv_user_email           = root.findViewById(R.id.textView_user_account_user_email);
        tv_logout               = root.findViewById(R.id.textView_logout);

        progressBar_past        = root.findViewById(R.id.progress_bar_past_orders);
        progressBar_recent      = root.findViewById(R.id.progress_bar_recent_orders);
        tv_no_recent_order      = root.findViewById(R.id.tv_no_recent_order);
        tv_no_past_order        = root.findViewById(R.id.tv_no_past_order);

        recyclerView_past_orders    = root.findViewById(R.id.recycler_view_past_orders);
        recyclerView_recent_orders  = root.findViewById(R.id.recycler_view_recent_orders);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView_past_orders.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_in_recycler_view));
        recyclerView_past_orders.addItemDecoration(divider);
        recyclerView_recent_orders.addItemDecoration(divider);

        set_up();
    }

    private void set_up() {

        Toolbar toolbar = root.findViewById(R.id.toolbar_user_account);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable img_up = getResources().getDrawable(R.drawable.ic_arrow_up);
                Drawable img_down = getResources().getDrawable(R.drawable.ic_arrow_below);

                if (!isPressed_help) {

                    tv_help.setCompoundDrawablesWithIntrinsicBounds(null,null,img_up,null);

                    tv_call     .setVisibility(View.VISIBLE);
                    //tv_message  .setVisibility(View.VISIBLE);
                    tv_facebook .setVisibility(View.VISIBLE);
                    tv_whatsapp .setVisibility(View.VISIBLE);
                    tv_instagram.setVisibility(View.VISIBLE);
                    tv_gmail    .setVisibility(View.VISIBLE);

                    view1       .setVisibility(View.VISIBLE);
                    view2       .setVisibility(View.VISIBLE);
                    //view3       .setVisibility(View.VISIBLE);
                    view4       .setVisibility(View.VISIBLE);
                    view5       .setVisibility(View.VISIBLE);
                    view6       .setVisibility(View.VISIBLE);

                    isPressed_help = true;

                }else {
                    tv_help.setCompoundDrawablesWithIntrinsicBounds(null,null,img_down,null);

                    tv_call     .setVisibility(View.GONE);
                    //tv_message  .setVisibility(View.GONE);
                    tv_facebook .setVisibility(View.GONE);
                    tv_whatsapp .setVisibility(View.GONE);
                    tv_instagram.setVisibility(View.GONE);
                    tv_gmail    .setVisibility(View.GONE);

                    view1       .setVisibility(View.GONE);
                    view2       .setVisibility(View.GONE);
                    //view3       .setVisibility(View.GONE);
                    view4       .setVisibility(View.GONE);
                    view5       .setVisibility(View.GONE);
                    view6       .setVisibility(View.GONE);

                    isPressed_help = false;
                }
            }
        });



        tv_past_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img_up = getResources().getDrawable(R.drawable.ic_arrow_up);
                Drawable img_down = getResources().getDrawable(R.drawable.ic_arrow_below);

                if (!isPressed_past_order) {

                    tv_past_orders.setCompoundDrawablesWithIntrinsicBounds(null,null,img_up,null);

                    if (past_orders_ArrayList == null && past_request == 0) {
                        progressBar_past.setVisibility(View.VISIBLE);
                    }else if (past_orders_ArrayList == null && past_request == 1){
                        progressBar_past.setVisibility(View.GONE);
                        tv_no_past_order.setVisibility(View.VISIBLE);
                    }
                    recyclerView_past_orders.setVisibility(View.VISIBLE);

                    isPressed_past_order = true;

                }else {

                    tv_past_orders.setCompoundDrawablesWithIntrinsicBounds(null,null,img_down,null);

                    progressBar_past.setVisibility(View.GONE);
                    tv_no_past_order.setVisibility(View.GONE);
                    recyclerView_past_orders.setVisibility(View.GONE);

                    isPressed_past_order = false;
                }

            }
        });



        tv_recent_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable img_up = getResources().getDrawable(R.drawable.ic_arrow_up);
                Drawable img_down = getResources().getDrawable(R.drawable.ic_arrow_below);

                if (!isPressed_recent_order) {

                    tv_recent_order.setCompoundDrawablesWithIntrinsicBounds(null,null,img_up,null);

                    if (recent_orders_ArrayList == null && recent_request == 0) {
                        progressBar_recent.setVisibility(View.VISIBLE);
                    }else if (recent_orders_ArrayList == null && recent_request == 1){
                        progressBar_recent.setVisibility(View.GONE);
                        tv_no_recent_order.setVisibility(View.VISIBLE);
                    }
                    recyclerView_recent_orders.setVisibility(View.VISIBLE);

                    isPressed_recent_order = true;

                }else {

                    tv_recent_order.setCompoundDrawablesWithIntrinsicBounds(null,null,img_down,null);

                    progressBar_recent.setVisibility(View.GONE);
                    tv_no_recent_order.setVisibility(View.GONE);
                    recyclerView_recent_orders.setVisibility(View.GONE);

                    isPressed_recent_order = false;
                }

            }
        });



        imageView_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_user_image_display());
            }
        });


        // Log out
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    builder = new android.app.AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
                }else {
                    builder = new android.app.AlertDialog.Builder(getActivity());
                }

                builder.setTitle("Logout");
                builder.setMessage("Do you want to logout ?");
                builder.setCancelable(true);

                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Prefs.PREF_FILE_NAME,Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();

                        imageView_user_image.setImageResource(R.drawable.edit_user);
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.edit_user);

                        Save_Image_to_Internal_Storage();

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(),RandomHotelActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        });


        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                make_call();
            }
        });



//        tv_message.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
//
//                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},request_sms);
//                }
//                else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms","8486749811",null));
//                    intent.putExtra("sms_body","Please contact immediately");
//                    startActivity(intent);
//                }
//
//            }
//        });

        tv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("fb://page/365070290971107"));
                    startActivity(intent);
                }catch (Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/zypkofoods")));
                }

            }
        });

        tv_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Intent.ACTION_VIEW,Uri.parse("http://instagram.com/_u/zypko_"));

                i.setPackage("com.instagram.android");

                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/zypko_")));
                }
            }
        });


        tv_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contact = "+91 8753957392"; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = getActivity().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        tv_gmail.setFocusable(true);
        tv_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"zypkofoods@gmail.com"});
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an email client"));
            }
        });

        imageView_secondary_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                secondary_image_clicked();

            }
        });


        //Set Name email and phone number

        tv_user_name    .setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_NAME));
        tv_user_phone   .setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_PHONE_NO));
        tv_user_email   .setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.USER_EMAIL));

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);


        //Set_Image_on_ImageView();
        Picasso.get()
                .load("file:/storage/emulated/0/Android/obb/com.zypko.zypko4/zypko/"+user_id+".jpg")
                .placeholder(R.drawable.edit_user)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().centerCrop()
                .into(imageView_user_image);


        //rating_of_delivery_boy();
        get_recent_order_from_server();
        get_past_order_from_server();

//
//        if (new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERED).equals("y")) {
//            rating_of_delivery_boy();
//        }
//
//        rating_of_delivery_boy();
    }

    private void secondary_image_clicked() {

        new MaterialDialog.Builder(getActivity())
                .title("Set your Image")
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, IMG_REQUEST);
                                break;

                            case 1:
                                imageView_user_image.setImageResource(R.drawable.edit_user);
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.edit_user);

                                Save_Image_to_Internal_Storage();
                                break;
                        }
                    }
                }).show();

    }


    private void rating_of_delivery_boy() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.layout_for_rating,null);

        TextView name           = view.findViewById(R.id.tv_name_delivery_boy_rating);
        TextView phone_no       = view.findViewById(R.id.tv_phone_no_delivery_boy_rating);
        final EditText comment        = view.findViewById(R.id.et_comment_delivery_boy_rating);
        ImageView del_boy_img   = view.findViewById(R.id.imageView_delivery_boy_rating);
        final Button btn_submit       = view.findViewById(R.id.btn_submit_delivery_boy_rating);
        final RatingBar ratingBar     = view.findViewById(R.id.rating_delivery_boy_rating);

        name.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_NAME));
        phone_no.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO));
        Picasso.get().load(UrlValues.SERVER+new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE)).centerCrop().fit().into(del_boy_img);

        builder.setCancelable(true);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rate_delivery_boy(ratingBar.getRating());
                //dialog.hide();
            }
        });

    }

    private void rate_delivery_boy(float rate) {


        int rating = (int) rate;


        if (rating != 5) {

            if (rating < rate) {
                rating = rating + 1;
            }
        }

    }

    private void make_call() {

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        }else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Copy the number to call");
        builder.setMessage("Call +918753957392 this number for Technical help");
        builder.setCancelable(true);

        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Number", "+918753957392");
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getActivity(), "Number copied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));



//
//        String number = "8753957392";
//
//        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CALL_PHONE },request_call);
//        }else {
//            String dial = "tel:"+number;
//            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(dial)));
//        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == request_call){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                make_call();
//
//            }else {
//                Toast.makeText(getActivity(), "You can't access this feature", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void get_recent_order_from_server(){

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);

        String url = UrlValues.GET_RECENT_ORDER_DETAILS+"?USER_ID="+user_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                recent_orders_ArrayList = new Past_Orders_Utils().Get_Recent_Orders_details(object);
                recent_request = 1;
                set_recycler_view();

            }

            @Override
            public void onError(VolleyError error) {

                recent_request = 1;
                set_recycler_view();
            }
        });

    }

    private void get_past_order_from_server() {

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);

        String url = UrlValues.GET_PAST_ORDER_DETAILS+"?USER_ID="+user_id;

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(url, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                past_orders_ArrayList = new Past_Orders_Utils().Get_Past_Orders_details(object);
                past_request = 1;
                set_recycler_view();

            }

            @Override
            public void onError(VolleyError error) {

                past_request = 1;
                set_recycler_view();

            }
        });
    }

    private void set_recycler_view() {

        if (past_orders_ArrayList != null) {

            adapter = new Adapter_for_past_orders(getActivity(),past_orders_ArrayList);
            layoutManager = new LinearLayoutManager(getActivity());

            recyclerView_past_orders.setLayoutManager(layoutManager);
            recyclerView_past_orders.setAdapter(adapter);
            progressBar_past.setVisibility(View.GONE);

        }else {

            if (past_request == 1 && isPressed_past_order) {
                progressBar_past.setVisibility(View.GONE);
                tv_no_past_order.setVisibility(View.VISIBLE);
            }
        }

        if (recent_orders_ArrayList != null){

            adapter_recent = new Adapter_for_recent_order(getActivity(),recent_orders_ArrayList);
            ((Adapter_for_recent_order) adapter_recent).setClickListener(this);

            layoutManager_recent = new LinearLayoutManager(getActivity());

            recyclerView_recent_orders.setLayoutManager(layoutManager_recent);
            recyclerView_recent_orders.setAdapter(adapter_recent);
            progressBar_recent.setVisibility(View.GONE);

        }else {

            if (recent_request == 1 && isPressed_recent_order) {
                progressBar_recent.setVisibility(View.GONE);
                tv_no_recent_order.setVisibility(View.VISIBLE);
            }

        }

    }

    private void Save_Image_to_Internal_Storage() {

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);


        File dir = new File(getActivity().getObbDir(),"zypko");
        dir.mkdir();

        File file = new File(dir, user_id+".jpg");

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        isPressed_help = false;
        isPressed_past_order = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);

        Picasso.get()
                .load("file:/storage/emulated/0/Android/obb/com.zypko.zypko4/zypko/"+user_id+".jpg")
                .placeholder(R.drawable.edit_user)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().centerCrop()
                .into(imageView_user_image);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                getActivity().onBackPressed();
                getActivity().finish();
            }

        }

        return true;
    }

    @Override
    public void itemClicked(View view, int position) {

        Button btn_track_order              = view.findViewById(R.id.btn_recent_order_track_your_order);
        final TextView tv_order_table_id    = view.findViewById(R.id.tv_recent_order_table_id);

    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }




    ///************************************************************************************************************************////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (new CheckInternet(getActivity()).Internet()) {
            if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

                try {
                    // Rotate the bitmap to original orientation
                    Uri selectedImage = data.getData();
                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cur = getActivity().managedQuery(selectedImage, orientationColumn, null, null, null);
                    int orientation = -1;
                    if (cur != null && cur.moveToFirst()) {
                        orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    }
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    switch(orientation) {
                        case 90:
                            bitmap = rotateImage(bitmap, 90);
                            break;
                        case 180:
                            bitmap = rotateImage(bitmap, 180);
                            break;
                        case 270:
                            bitmap = rotateImage(bitmap, 270);
                            break;
                        default:
                            break;
                    }

                    imageView_user_image.setImageBitmap(bitmap );

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    Upload_Image_to_server();

                    ///// for offline uses..

                    Save_Image_to_Internal_Storage();

                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "error image", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            new AlertDialog_For_NoInternet(getActivity()).AlertDialog();
        }
    }


    private void Upload_Image_to_server() {

        String url = UrlValues.SEND_USER_IMAGE;

        Map<String, String> map = new HashMap<>();

        map.put("ENCODED_STRING", Bitmap_to_String(bitmap));
        map.put("USER_ID", ""+new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID));

        Post_to_Server pst = new Post_to_Server(getActivity(), map);
        pst.getJson(url, new PostCall());

    }


    private String Bitmap_to_String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private class PostCall implements HTTP_Post_Callback {
        @Override
        public void onSuccess(String string) {

            try {
                JSONObject object = new JSONObject(string);

                int success = object.getInt(Json_Objects.SUCCESS);

                if (success > 0) {

                    Toast.makeText(getActivity(), "Profile Photo Updated Successfully", Toast.LENGTH_SHORT).show();

                    Save_Image_to_Internal_Storage();

                    progressDialog.dismiss();

                }

            } catch (JSONException e) {
                e.printStackTrace();

                progressDialog.dismiss();
            }
        }
        @Override
        public void onError(VolleyError error) {
            progressDialog.hide();
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
