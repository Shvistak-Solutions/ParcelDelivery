package com.example.ParcelDelivery.ui.storekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ParcelDelivery.R;

public class StorekeeperActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        userId = getIntent().getIntExtra("userId", 0);
    }
}
