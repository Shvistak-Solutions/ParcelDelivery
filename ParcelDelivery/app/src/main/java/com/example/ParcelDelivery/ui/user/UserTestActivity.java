package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.example.ParcelDelivery.R;

public class UserTestActivity extends FragmentActivity {



    
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    FragmentStateAdapter adapterViewPager;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        viewPager = findViewById(R.id.vpPager);
        adapterViewPager = new ScreenPagerAdapter(this);
        viewPager.setAdapter(adapterViewPager);

    }

    public static class ScreenPagerAdapter extends FragmentStateAdapter {
        

        public ScreenPagerAdapter(FragmentActivity fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            if (position == 0) {
                new UserDetailsFirstFragment();
                fragment = UserDetailsFirstFragment.newInstance(1);
            } else if (position == 1) {
                new UserDetailsSecondFragment();
                fragment = UserDetailsSecondFragment.newInstance(3);
            }
            assert fragment != null;
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

        }


}
