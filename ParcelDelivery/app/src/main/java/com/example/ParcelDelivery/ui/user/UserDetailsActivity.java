package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.widget.TableLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import com.google.android.material.tabs.TabLayout;



import com.example.ParcelDelivery.R;

public class UserDetailsActivity extends FragmentActivity {

    int thisUserId;
    int userId;

    private static final int NUM_PAGES = 2;
    FragmentStateAdapter adapterViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        ViewPager2 viewPager = findViewById(R.id.vpPager);
        thisUserId = getIntent().getIntExtra("id", 0);
        userId = getIntent().getIntExtra("userId", userId);
        adapterViewPager = new ScreenPagerAdapter(this);
        viewPager.setAdapter(adapterViewPager);
        
        String[] description = new String[]{"Detale","Pensja"};

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(description[position])
        ).attach();

    }



    public class ScreenPagerAdapter extends FragmentStateAdapter {


        public ScreenPagerAdapter(FragmentActivity fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            if (position == 0) {
                new UserDetailsFirstFragment();
                fragment = UserDetailsFirstFragment.newInstance(thisUserId,userId);
            } else if (position == 1) {
                new UserDetailsSecondFragment();
                fragment = UserDetailsSecondFragment.newInstance(thisUserId);
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
