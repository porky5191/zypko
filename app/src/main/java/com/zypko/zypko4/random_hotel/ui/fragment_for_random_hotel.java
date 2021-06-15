package com.zypko.zypko4.random_hotel.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.LoginActivity;
import com.zypko.zypko4.activity.util.GridAdapter;
import com.zypko.zypko4.cart.ui.fragment_for_Cart;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.Database;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.random_hotel.util.Food_category;
import com.zypko.zypko4.random_hotel.util.Random_Food_Category_utils;
import com.zypko.zypko4.server.HTTP_Get;
import com.zypko.zypko4.server.JSONProvider;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.user_account.ui.fragment_for_rate_delivery_boy;

import org.json.JSONObject;

import java.util.ArrayList;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class fragment_for_random_hotel extends Fragment implements GridAdapter.ClickListener,View.OnKeyListener {

    View root;
    RecyclerView recyclerView;
    GridAdapter mAdapter;

    TextView floating_textView;

    boolean clicked = false;
    RelativeLayout relative_layout_in_random_hotel_search_content, relative_layout_in_random_hotel_search_animaton;
    ShimmerLayout shimmerLayout;

    LinearLayout linearLayout_for_floating_search_bar;

    ArrayList<Food_category> food_category_ArrayList;
    Database myDB;

    //////////////////////////////////////////////////////////////////////////////////////////

    FloatingActionButton fab_account, fab_cart, fab_call;
    FloatingActionMenu floatingActionMenu;
    boolean backPressed = false;

    //SwipeRefreshLayout swipeRefreshLayout;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.fragment_for_random_hotel, container, false);

            food_category_ArrayList = new ArrayList<>();

            ///*******************************************************************//
            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            ///*******************************************************************//
            initialise();
        }

        return root;
    }

    private void initialise() {

        myDB = new Database(getActivity());

        // Floating Action Button

        fab_account = root.findViewById(R.id.fab_account);
        fab_cart = root.findViewById(R.id.fab_cart);
        fab_call = root.findViewById(R.id.fab_call);

        recyclerView = root.findViewById(R.id.recycler_view_of_main_activity);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        floating_textView = root.findViewById(R.id.floating_text_button_for_station);

        relative_layout_in_random_hotel_search_content = root.findViewById(R.id.relative_layout_in_random_hotel_search_content);
        relative_layout_in_random_hotel_search_animaton = root.findViewById(R.id.relative_layout_in_random_hotel_search_animation);

        shimmerLayout = root.findViewById(R.id.shimmer_layout_food_category);
        linearLayout_for_floating_search_bar = root.findViewById(R.id.linear_layout_for_floating_search_bar);
        floatingActionMenu = root.findViewById(R.id.floating_action_menu);

        //swipeRefreshLayout      = root.findViewById(R.id.SwipeRefreshLayout_tracking_page);
        //swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorBlue_tez_dark));
        //swipeRefreshLayout.setOnRefreshListener(this);

        if (new CheckInternet(getActivity()).Internet()) {
            set_up();
        } else {
            new AlertDialog_For_NoInternet(getActivity()).AlertDialog();
        }

    }

    private void set_up() {

        shimmerLayout.startShimmerAnimation();
        shimmerLayout.setVisibility(View.VISIBLE);

        linearLayout_for_floating_search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentOpener(getActivity()).setManager(getActivity().getSupportFragmentManager()).open_Replace_Backstack(new fragment_for_search_station());

            }
        });


        fab_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(),UserAccountActivity.class));
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                intent.putExtra("FRAGMENT","USER_ACCOUNT");
                startActivity(intent);
                floatingActionMenu.close(true);

            }
        });


        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hotel_id = myDB.get_hotel_id_of_cart_items();

                if (hotel_id != 0) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("HOTEL_ID", hotel_id);

                    new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(new fragment_for_Cart(), bundle);
                    floatingActionMenu.close(true);

                } else {

                    Toast.makeText(getActivity(), "You don't have any items in cart", Toast.LENGTH_SHORT).show();

                }

            }
        });

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_call();
                //FirebaseMessaging.getInstance().subscribeToTopic("CMS");
                floatingActionMenu.close(true);

            }
        });

        JSONProvider provider = new JSONProvider(getActivity());
        provider.getJson(UrlValues.FOOD_CATEGORY_PHP_URL, new HTTP_Get() {
            @Override
            public void onSuccess(JSONObject object) {

                food_category_ArrayList = new Random_Food_Category_utils().Get_all_food_category(object);

                set_recycler();
            }

            @Override
            public void onError(VolleyError error) {
                alert_dialog_for_retry();

                set_recycler();
            }
        });



        // rate delivery boy

        if (new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERED).equals("y")) {
            rating_of_delivery_boy();
        }

        //rating_of_delivery_boy();
    }


    private void make_call() {
//        String number = "6002015766";
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, request_call);
//        } else {
//            String dial = "tel:" + number;
//            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//        }

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        }else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        builder.setTitle("Copy the number to call");
        builder.setMessage("Call +916002015766 this number for Order Your food");
        builder.setCancelable(true);

        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Number", "+916002015766");
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

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == request_call) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                make_call();
//
//            } else {
//                Toast.makeText(getActivity(), "You can't access this feature", Toast.LENGTH_SHORT).show();
//
//            }
//
//        }
//
//    }


    private void alert_dialog_for_retry() {

        final android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder = new android.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        } else {
            builder = new android.app.AlertDialog.Builder(getActivity());
        }

        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(false);
        builder.setTitle("Oopss.. !!");
        builder.setMessage("Your internet connection is slow. Please try again.");


        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                set_up();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                dialog.dismiss();
            }
        });


        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlue_tez));


    }

    private void set_recycler() {

        if (food_category_ArrayList != null) {
            mAdapter = new GridAdapter(food_category_ArrayList);
            mAdapter.setClickListener(this);
            recyclerView.setAdapter(mAdapter);

            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {

            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            shimmerLayout.stopShimmerAnimation();
            shimmerLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            alert_dialog_for_retry();
        }
    }


    @Override
    public void itemClicked(View view) {

        if (!clicked) {

            TextView textView = view.findViewById(R.id.textview_name_of_the_photo);

            String Name_of_food = textView.getText().toString();

            Fragment fragment = new fragment_for_asking_to_enter_station();
            Bundle bundle = new Bundle();
            bundle.putString("FOOD_CATEGORY_NAME", "" + Name_of_food);

            fragment.setArguments(bundle);
            new FragmentOpener(getActivity()).setManager(getFragmentManager()).open_Replace_Backstack(fragment);

            clicked = true;
        }
    }



    private void rating_of_delivery_boy() {


        new FragmentOpener(getActivity()).setManager(getFragmentManager()).openReplace(new fragment_for_rate_delivery_boy());


//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        View view = getLayoutInflater().inflate(R.layout.layout_for_rating,null);
//
//        TextView name           = view.findViewById(R.id.tv_name_delivery_boy_rating);
//        TextView phone_no       = view.findViewById(R.id.tv_phone_no_delivery_boy_rating);
//        final EditText comment        = view.findViewById(R.id.et_comment_delivery_boy_rating);
//        ImageView del_boy_img   = view.findViewById(R.id.imageView_delivery_boy_rating);
//        final Button btn_submit       = view.findViewById(R.id.btn_submit_delivery_boy_rating);
//        final RatingBar ratingBar     = view.findViewById(R.id.rating_delivery_boy_rating);
//
//        name.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_NAME));
//        phone_no.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO));
//        Picasso.get().load(UrlValues.SERVER+new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE)).centerCrop().fit().into(del_boy_img);
//
//        builder.setCancelable(true);
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                rate_delivery_boy(ratingBar.getRating());
//                //dialog.hide();
//            }
//        });

    }

    private void rate_delivery_boy(float rate) {


        int rating = (int) rate;


        if (rating != 5) {

            if (rating < rate) {
                rating = rating + 1;
            }
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onResume() {
        super.onResume();
        clicked = false;
        floatingActionMenu.close(true);
        backPressed = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
            }
        }
        return true;
    }

    private void onBackPressed() {

        if (backPressed) {
            getActivity().onBackPressed();
        }else {
            Toast.makeText(getActivity(), "Touch again to exit", Toast.LENGTH_SHORT).show();
            backPressed = true;
        }

    }

}