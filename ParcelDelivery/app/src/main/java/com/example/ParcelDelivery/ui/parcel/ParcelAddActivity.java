package com.example.ParcelDelivery.ui.parcel;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParcelAddActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_add);

        Button buttonParcelConfirm = (Button) findViewById(R.id.buttonConfirmParcel);

        buttonParcelConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ParcelAddActivity.this, ParcelListActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Dodano przesyłkę",Toast.LENGTH_SHORT).show();
            }
        });

        /*List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Pan Świstak");
        spinnerArray.add("Mister Marmot");*/

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String[] columns = {"id", "imie", "nazwisko"};
        ArrayList<HashMap<String, String>> couriers = dbHelper.getData(columns, "Pracownicy", "stanowisko", "0");
        List<String> spinnerArray =  new ArrayList<String>();
        for (HashMap<String,String> item : couriers) {
            spinnerArray.add(item.get("nazwisko"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerItems = (Spinner) findViewById(R.id.spinnerCourierSelect);
        spinnerItems.setAdapter(adapter);
    }
}
