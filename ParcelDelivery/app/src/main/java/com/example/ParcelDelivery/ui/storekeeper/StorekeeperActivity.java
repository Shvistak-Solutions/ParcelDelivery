package com.example.ParcelDelivery.ui.storekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.password_reset.ForgotPasswordActivity;

public class StorekeeperActivity extends AppCompatActivity {

    int userId;
    int test = 0;

    private Button viewStorehouse;
    private DatabaseHelper dbH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        userId = getIntent().getIntExtra("userId", 0);


        viewStorehouse = (Button)findViewById(R.id.ID_VIEW_STOREHOUSE);
        viewStorehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StorekeeperActivity.this, StorehouseActivity.class));
            }
        });

    }
}
