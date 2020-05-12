package com.example.ParcelDelivery.ui.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.ui.user.UserListActivity;

public class ManagerActivity extends AppCompatActivity {

    Intent intent;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        userId = getIntent().getIntExtra("userId", 0);

        Button buttonUserList = (Button) findViewById(R.id.buttonUserList);

        buttonUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, UserListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
