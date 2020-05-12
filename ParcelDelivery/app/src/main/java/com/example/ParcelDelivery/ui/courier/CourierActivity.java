package com.example.ParcelDelivery.ui.courier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ParcelDelivery.R;

public class CourierActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        userId = getIntent().getIntExtra("userId", 0);
    }
}
