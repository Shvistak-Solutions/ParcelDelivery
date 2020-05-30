package com.example.ParcelDelivery.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;

import com.google.android.material.tabs.TabLayout;



import com.example.ParcelDelivery.R;

public class UserDetailsActivity extends FragmentActivity {

    int thisUserId;
    int userId;
    ViewPager2 viewPager;

    private static final int NUM_PAGES = 3;
    FragmentStateAdapter adapterViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_userdetails);
        viewPager = findViewById(R.id.vpPager);
        thisUserId = getIntent().getIntExtra("id", 0);
        userId = getIntent().getIntExtra("userId", userId);
        adapterViewPager = new ScreenPagerAdapter(this);
        viewPager.setAdapter(adapterViewPager);
        
        String[] description = new String[]{"Detale","Grafik","Pensja"};

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(description[position])
        ).attach();

        // back button
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                startActivity(new Intent(UserDetailsActivity.this, UserListActivity.class));
//            }
//        };
//        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
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
            else if (position == 2){
                new UserDetailsThirdFragment();
                fragment = UserDetailsThirdFragment.newInstance(thisUserId);
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
