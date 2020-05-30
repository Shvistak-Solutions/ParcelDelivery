package com.example.ParcelDelivery.ui.courier;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;
import com.example.ParcelDelivery.ui.login.LoginActivity;

public class CourierActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        userId = getIntent().getIntExtra("userId", 0);

        // logout
        Button buttonLogout = findViewById(R.id.buttonLogoutCou);
        buttonLogout.setOnClickListener( v -> {
            Toast.makeText(CourierActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CourierActivity.this, LoginActivity.class));
        });

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        Button buttonParcelList = (Button) findViewById(R.id.buttonParcelList);
        buttonParcelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourierActivity.this, ParcelListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
