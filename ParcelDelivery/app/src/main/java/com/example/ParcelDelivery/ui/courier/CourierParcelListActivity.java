package com.example.ParcelDelivery.ui.courier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ParcelDelivery.R;

import java.util.ArrayList;

public class CourierParcelListActivity extends AppCompatActivity {

    private static final String TAG = "CourierParcelListActivi";

    private int userId;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_parcel_list);

        userId = getIntent().getIntExtra("userId", 0);

        initImageBitmaps();

    }
    
    
    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        imageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        names.add("Trondheim");

        imageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        names.add("Portugal");

        imageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        names.add("Rocky Mountain National Park");

        initRecyclerView();
    }
    
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.rvParcelList);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, names, imageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
