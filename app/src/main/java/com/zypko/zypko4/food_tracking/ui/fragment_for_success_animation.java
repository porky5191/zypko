package com.zypko.zypko4.food_tracking.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.TrackingActivity;

public class fragment_for_success_animation extends Fragment implements View.OnKeyListener {

    View root;
    LottieAnimationView lottieAnimationView;
    int order_table_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.layout_for_successful_payment, container, false);
            ///*************************************************////

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(this);

            ///*************************************************////

            initialise();
        }

        return root;
    }

    private void initialise() {

        lottieAnimationView     = root.findViewById(R.id.lottie_animation_success_order_placed);

        set_up();

    }

    private void set_up() {

        Bundle bundle = getArguments();

        if (bundle != null) {

            order_table_id = bundle.getInt("ORDER_TABLE_ID");
        }

        lottieAnimationView.playAnimation();
        lottieAnimationView.setSpeed((float) 1.4);

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                Intent intent3 = new Intent(getActivity(), TrackingActivity.class);
                intent3.putExtra("ORDER_TABLE_ID", order_table_id);
                intent3.putExtra("FRAGMENT", "PAYMENT_ACTIVITY");
                startActivity(intent3);
                getActivity().finish();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

                Intent intent3 = new Intent(getActivity(), TrackingActivity.class);
                intent3.putExtra("ORDER_TABLE_ID", order_table_id);
                intent3.putExtra("FRAGMENT", "PAYMENT_ACTIVITY");
                startActivity(intent3);
                getActivity().finish();

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //onBackPressed();
                Log.e("ON_BACK_PRESSED","TRIGGER");
            }
        }
        return true;
    }
}
