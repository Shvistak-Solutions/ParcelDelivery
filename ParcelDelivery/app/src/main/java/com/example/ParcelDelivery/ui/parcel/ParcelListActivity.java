package com.example.ParcelDelivery.ui.parcel;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;
import com.example.ParcelDelivery.ui.courier.CourierActivity;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.storekeeper.StorekeeperActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class ParcelListActivity extends AppCompatActivity {
    Intent intent;
    private ArrayList<HashMap<String,String>> mParcelData = new ArrayList<>();
    private int userId;
    Button saveBtn, buttonLogout;


    private static final String TAG = "ParcelListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_list);
        userId = getIntent().getIntExtra("userId",0);
        // back button

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {


                Class<?> destinationActivity;
                switch(getUserPosition(userId)){
                    case "Kurier":
                        destinationActivity = CourierActivity.class;
                        break;
                    case "Koordynator":
                        destinationActivity = CoordinatorActivity.class;
                        break;
                    case "Magazynier":
                        destinationActivity = StorekeeperActivity.class;
                        break;
                    default:
                        destinationActivity = LoginActivity.class;
                        break;

                }

                intent = new Intent(ParcelListActivity.this, destinationActivity);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        buttonLogout = findViewById(R.id.buttonLogoutParcel);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ParcelListActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ParcelListActivity.this, LoginActivity.class));
            }
        });


        // add a new parcel
        saveBtn = findViewById(R.id.buttonAddParcel);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ParcelListActivity.this, ParcelAddActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });


        getParcelData();
        initRecyclerView();


    }


    private void getParcelData() {
        final DatabaseHelper dbHelper = new DatabaseHelper(this);


        switch(getUserPosition(userId)){
            case "Kurier":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki WHERE id_kuriera = " + userId);

                saveBtn.setVisibility(View.GONE);
                break;
            case "Koordynator":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki");
                break;
            case "Magazynier":
                mParcelData = dbHelper.getDataSQL("SELECT id, status, id_kuriera FROM Paczki WHERE status = 3");// tylko te paczki które w magazynie
                saveBtn.setVisibility(View.GONE);
                break;
            default:
                break;

        }
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.parcel_list_recycler_view);

        ParcelListRecyclerViewAdapter adapter = new ParcelListRecyclerViewAdapter(mParcelData,userId);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private String getUserPosition(int userId)
    {
        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        return dbHelper.getData("stanowisko","Pracownicy",userId);
    }
}
