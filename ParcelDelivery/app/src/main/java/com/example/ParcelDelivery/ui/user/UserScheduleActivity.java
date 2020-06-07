package com.example.ParcelDelivery.ui.user;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
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

        db.insertSchedule(db.makeDateYMD(calendar), db.makeDateTime(calendar), db.makeDateTime(calendar), userId);
        String entry = db.makeDateTime(calendar);
        calendar.add(Calendar.HOUR, 5);
        db.updateDataSQL("update Pensje set ilosc_godzin=5 where id_prac = 7");
//        SQLiteDatabase lol = db.getWritableDatabase();
//        lol.delete("Pensje","Data=?",new String[]{db.makeDateYM(calendar)});
//        lol.close();
        db.monthlyUpdate();


        buttonSchedule.setOnClickListener(v -> {

            Toast.makeText(getApplicationContext(),entry+" id: "+userId, Toast.LENGTH_SHORT).show();
            long test = db.updatePresence(db.makeDateYMD(calendar),entry,db.makeDateTime(calendar),userId);
            list = db.getDataSQL("SELECT wejscie,wyjscie from Grafik where id_prac ="+userId+" and data like '"+db.makeDateYMD(calendar)+"'");
            for(HashMap<String,String> a : list)
            {
                Toast.makeText(getApplicationContext(),a.get("wejscie")+" a wyszem "+a.get("wyjscie"), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),a.get("pensja")+" a ilosc godzin "+a.get("ilosc_godzin")+" a stawka "+a.get("stawka"), Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(getApplicationContext(),test+"", Toast.LENGTH_SHORT).show();

        });

    }

}
