package com.example.ParcelDelivery.ui.parcel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class ParcelListActivity extends AppCompatActivity {
    Intent intent;
    private ArrayList<HashMap<String,String>> mParcelData = new ArrayList<>();
    private int userId;
    private static final String TAG = "ParcelListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_list);

        Button saveBtn = (Button)findViewById(R.id.buttonAddParcel);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ParcelListActivity.this, ParcelAddActivity.class);
                startActivity(intent);
            }
        });

        userId = getIntent().getIntExtra("userId",0);
        getParcelData();
        initRecyclerView();


    }


    private void getParcelData() {
        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        String userPosition = dbHelper.getData("stanowisko","Pracownicy","id",Integer.toString(userId)).get(0);
        Log.d(TAG, "getParcelData: entered with userpos: "+userPosition);
        switch(userPosition){
            case "Kurier":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki WHERE id_kuriera = " + userId);
                break;
            case "Koordynator":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki");
                break;
            case "Magazynier":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki WHERE status = 3");// tylko te paczki które w magazynie
                break;
            default:
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki");

        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.parcel_list_recycler_view);

        ParcelListRecyclerViewAdapter adapter = new ParcelListRecyclerViewAdapter(mParcelData);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
