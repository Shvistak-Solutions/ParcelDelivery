package com.example.ParcelDelivery.ui.parcel;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ParcelAddActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_add);

        // get couriers from database
        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        String[] columns = {"id", "imie", "nazwisko"};
        ArrayList<HashMap<String, String>> couriers = dbHelper.getData(columns, "Pracownicy", "stanowisko", "0");
        List<String> spinnerArray =  new ArrayList<String>();
        for (HashMap<String,String> item : couriers) {
            spinnerArray.add(item.get("imie") + " " + item.get("nazwisko"));
        }

        // assign courier list to spinner component
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinnerCouriers = (Spinner) findViewById(R.id.spinnerCourierSelect);
        spinnerCouriers.setAdapter(adapter);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                intent = new Intent(ParcelAddActivity.this, ParcelListActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // add parcel into database and get back to parcel list
        Button buttonParcelConfirm = (Button) findViewById(R.id.buttonConfirmParcel);
        buttonParcelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courierSurname = spinnerCouriers.getSelectedItem().toString().split(" ")[1];
                String courierId = dbHelper.getData("id", "Pracownicy", "nazwisko", courierSurname).get(0);

                long result = dbHelper.insertNewParcel(parseInt(courierId));
                if (result > 0) {
                    intent = new Intent(ParcelAddActivity.this, ParcelListActivity.class);
                    intent.putExtra("userId",getIntent().getIntExtra("userId",0));
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Dodano przesyłkę", Toast.LENGTH_SHORT).show();
                }
                else if (result == 0)
                    Toast.makeText(getApplicationContext(), "Pola nie mogą być puste", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Nie udało się dodać zamówienia", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
