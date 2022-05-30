package com.example.androidchat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.imageView);

        getWindow().setStatusBarColor(getColor(R.color.white));

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1, 0.3f);
        fadeOut.setDuration(2000);      //fadeout for 2sec
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0.3f, 1);
        fadeIn.setDuration(2000);       //fadeIn for 2sec

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(fadeIn).after(fadeOut);       //telling play fadeIn after fadeOut to animator
        mAnimatorSet.start();               //starting the animation

        //we like to animate 2 times, so for second time when 1st animation gets over play again when 1st going to be end like that loop will be formed
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorSet.start();
            }
        });

        // this will count down the screen time , after 4sec we have to go to other intent
        new CountDownTimer(1000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();



    }
}