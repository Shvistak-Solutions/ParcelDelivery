package com.example.ParcelDelivery.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;
import com.example.ParcelDelivery.ui.courier.CourierActivity;
import com.example.ParcelDelivery.ui.manager.ManagerActivity;
import com.example.ParcelDelivery.ui.storekeeper.StorekeeperActivity;
import com.google.android.material.tabs.TabLayoutMediator;

import com.google.android.material.tabs.TabLayout;



import com.example.ParcelDelivery.R;

public class UserDetailsActivity extends FragmentActivity {

    int thisUserId;
    int userId;
    ViewPager2 viewPager;
    Intent intent;

    private static final int NUM_PAGES = 4;
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
        
        String[] description = new String[]{"Ogólne","Detale","Grafik","Pensja"};

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(description[position])
        ).attach();


    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            //super.onBackPressed();

            if(thisUserId != userId){
                intent = new Intent(UserDetailsActivity.this, UserListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
            else
            {
                DatabaseHelper db = new DatabaseHelper(this);
                int who = db.positionStringToInt(db.getData("stanowisko","Pracownicy", userId));
                switch (who){
                    case 0:
                        intent = new Intent(UserDetailsActivity.this, CourierActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(UserDetailsActivity.this, StorekeeperActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(UserDetailsActivity.this, CoordinatorActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(UserDetailsActivity.this, ManagerActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        break;
                }
            }
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
                new UserDetailsZeroFragment();
                fragment = UserDetailsZeroFragment.newInstance(thisUserId, userId);
            } else if (position == 1) {
                new UserDetailsFirstFragment();
                fragment = UserDetailsFirstFragment.newInstance(thisUserId, userId);
            } else if (position == 2) {
                new UserDetailsSecondFragment();
                fragment = UserDetailsSecondFragment.newInstance(thisUserId, userId);
            }
            else if (position == 3){
                new UserDetailsThirdFragment();
                fragment = UserDetailsThirdFragment.newInstance(thisUserId, userId);
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
