package com.zypko.zypko4.activity.ui;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.zypko.zypko4.R;

public class SplashActivity extends AppCompatActivity {


    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lottieAnimationView = findViewById(R.id.lottie_animation_splash_screen);


        lottieAnimationView.setImageAssetsFolder("images/");
        lottieAnimationView.setAnimation("zypko.json");
        lottieAnimationView.loop(false);
        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(new Intent(SplashActivity.this,RandomHotelActivity.class));
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                startActivity(new Intent(SplashActivity.this,RandomHotelActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }
}
