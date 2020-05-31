package com.example.ParcelDelivery.ui.storekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;
import com.example.ParcelDelivery.ui.login.LoginActivity;

public class StorekeeperActivity extends AppCompatActivity {

    int userId;
    int test = 0;

    Intent intent;

    private Button viewStorehouse;
    private DatabaseHelper dbH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        userId = getIntent().getIntExtra("userId", 0);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // logout
        Button buttonLogout = findViewById(R.id.buttonLogoutStore);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StorekeeperActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StorekeeperActivity.this, LoginActivity.class));
            }
        });

        viewStorehouse = (Button)findViewById(R.id.ID_VIEW_STOREHOUSE);
        viewStorehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StorekeeperActivity.this, ParcelListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

    }
}
