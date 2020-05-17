package com.example.ParcelDelivery.ui.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;

public class CourierParcelListActivity extends AppCompatActivity {

    private static final String TAG = "CourierParcelListActivi";

    private String parcelID;
    private int userId;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> parcelIdArray = new ArrayList<>();
    private ArrayList<String> tempParcelIdArray = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_parcel_list);

        final DatabaseHelper db = new DatabaseHelper(this);

        userId = getIntent().getIntExtra("userId", 0);

        tempParcelIdArray = db.getData("id", "Paczki","id_kuriera", String.valueOf(userId));

        initImageBitmaps();

    }
    
    
    private void initImageBitmaps(){


        for(String x : tempParcelIdArray) {
            imageUrls.add("https://en.pimg.jp/059/212/115/1/59212115.jpg");
            parcelIdArray.add("id paczki: " + x);
        }

        initRecyclerView();
    }
    
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.rvParcelList);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, parcelIdArray, imageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
