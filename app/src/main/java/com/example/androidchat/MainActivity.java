package com.example.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.androidchat.adapter.LogViewPagerAdapter;
import com.example.androidchat.fragment.SignInFragment;
import com.example.androidchat.fragment.SignUpFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ViewPager mLogViewPager;
    private TabLayout mLogTabLayout;
    private ConstraintLayout mLogLayout;
    private LogViewPagerAdapter logViewPagerAdapter;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        mLogViewPager = findViewById(R.id.logViewPager);
        mLogTabLayout = findViewById(R.id.logTabLayout);
        mLogLayout = findViewById(R.id.logLayout);

        logViewPagerAdapter = new LogViewPagerAdapter(getSupportFragmentManager());
        logViewPagerAdapter.addFragmentWithTitle(new SignInFragment(), "Sign In");
        logViewPagerAdapter.addFragmentWithTitle(new SignUpFragment(), "Sign Up");

        //changing status bar color to pinkish
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.bluesh));
        }

        mLogViewPager.setAdapter(logViewPagerAdapter);       //setting adapter to the logViewPager

        // Setting or attaching tabLayout with View Pager so that when ever tab moves pager also moves
        mLogTabLayout.setupWithViewPager(mLogViewPager);


        mLogTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //For sign in
                if(tab.getPosition() == 0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //change the background for sign in
                        mLogLayout.setBackground(getDrawable(R.drawable.layout_sigin_bg));
                        //changing status bar color to pinkish
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getColor(R.color.bluesh));
                        }
                    }
                }

                //For sign up
                if(tab.getPosition() == 1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //change the background for sign in
                        mLogLayout.setBackground(getDrawable(R.drawable.layout_signup_bg));
                        //changing status bar color to pinkish
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getColor(R.color.bluesh));
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}