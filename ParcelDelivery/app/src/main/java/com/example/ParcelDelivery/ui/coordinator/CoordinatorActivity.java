package com.example.ParcelDelivery.ui.coordinator;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;
import com.example.ParcelDelivery.ui.password_reset.ChangePasswordActivity;

public class CoordinatorActivity extends AppCompatActivity {

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        userId = getIntent().getIntExtra("userId", 0);

        // logout
        Button buttonLogout = findViewById(R.id.buttonLogoutCoord);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, LoginActivity.class);
                Toast.makeText(CoordinatorActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                //Toast.makeText(CoordinatorActivity.this, "Nope", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // list of packages
        Button buttonParcelList = (Button) findViewById(R.id.buttonParcelList);
        buttonParcelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, ParcelListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        //--------------- ZMIANA HASLA - DEMO
        Button changePassword = (Button)findViewById(R.id.ID_CHANGE_PASSWORD);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, ChangePasswordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        //------------------------------------
    }
}
