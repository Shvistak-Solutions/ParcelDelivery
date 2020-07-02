package com.example.ParcelDelivery.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserScheduleActivity extends AppCompatActivity {

    private int userId;

    TextView textSchedule;
    Button buttonSchedule;

    ArrayList<HashMap<String,String>> list;

    Calendar calendar;

    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_schedule);
        userId = getIntent().getIntExtra("userId", 0);

        buttonSchedule = findViewById(R.id.buttonAvailability);
        textSchedule = findViewById(R.id.textSchedule);

        calendar = Calendar.getInstance();
        String entry = db.makeDateTime(calendar);
        calendar.add(Calendar.HOUR, 5);


        buttonSchedule.setOnClickListener(v -> {

            Toast.makeText(getApplicationContext(),"Click", Toast.LENGTH_SHORT).show();

        });

    }

}
