package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;

public class UserDetailsActivity extends AppCompatActivity {

    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
         id = getIntent().getStringExtra("id");
    }
}
