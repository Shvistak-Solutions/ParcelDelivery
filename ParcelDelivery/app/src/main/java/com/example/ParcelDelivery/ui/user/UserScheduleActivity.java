package com.example.ParcelDelivery.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

        buttonSchedule = findViewById(R.id.buttonSchedule);
        textSchedule = findViewById(R.id.textSchedule);

        calendar = Calendar.getInstance();
        int i = 0;
        while(i < 7) {
            db.insertSchedule(makeDate(calendar), makeDateTime(calendar), makeDateTime(calendar), userId);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }


        buttonSchedule.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            list = db.getDataBetween(new String[]{"data"},"Grafik","id_prac",Integer.toString(userId),"data","2020-05-15","2020-05-21");
            list = db.getDataSQL("SELECT data FROM Grafik where id_prac = 7");
            for(HashMap<String,String> a : list)
            {
                Toast.makeText(getApplicationContext(),a.get("data"), Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(),"cos powinno byc", Toast.LENGTH_SHORT).show();

        });

    }

    private String makeDate(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        dateFormat.setTimeZone(calendar.getTimeZone());
        return dateFormat.format(calendar.getTime());

    }

    private String makeDateTime(Calendar calendar){
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        datetimeFormat.setTimeZone(calendar.getTimeZone());
        return datetimeFormat.format(calendar.getTime());
    }
}
