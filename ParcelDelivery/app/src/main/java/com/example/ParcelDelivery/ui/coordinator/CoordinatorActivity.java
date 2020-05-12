package com.example.ParcelDelivery.ui.coordinator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ParcelDelivery.R;

public class CoordinatorActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        userId = getIntent().getIntExtra("userId", 0);
    }
}
