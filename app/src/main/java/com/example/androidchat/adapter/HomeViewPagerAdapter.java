package com.example.androidchat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mTitleList = new ArrayList<>();

    public HomeViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    // this will be use to return the tab name which is title
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;

    }

    // this will be use to add fragment and its corresponding title to its tab
    public void addFragmentWithTitle(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }
}
