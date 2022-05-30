package com.example.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;

import com.example.androidchat.adapter.HomeViewPagerAdapter;
import com.example.androidchat.fragment.DiscoverFragment;
import com.example.androidchat.fragment.MatchFragment;
import com.example.androidchat.fragment.ProfileFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    private ViewPager mHomeViewPager;
    private TabLayout mHomeTabs;

    private HomeViewPagerAdapter homeViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //changing status bar color to violish
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.violish));
        }


        mHomeViewPager = findViewById(R.id.homeViewPager);
        mHomeTabs = findViewById(R.id.homeTabLayout);

        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragmentWithTitle(new ProfileFragment(), "Profile");
        homeViewPagerAdapter.addFragmentWithTitle(new DiscoverFragment(), "Discover");
        homeViewPagerAdapter.addFragmentWithTitle(new MatchFragment(), "Chat");
        mHomeViewPager.setAdapter(homeViewPagerAdapter);
        mHomeTabs.setupWithViewPager(mHomeViewPager);

        //setting images to the tab bar
        mHomeTabs.getTabAt(0).setIcon(R.drawable.user11);
        mHomeTabs.getTabAt(1).setIcon(R.drawable.trending);
        mHomeTabs.getTabAt(2).setIcon(R.drawable.chat1);

        //to set default opening with discover
        mHomeTabs.selectTab(mHomeTabs.getTabAt(1));

        mHomeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}