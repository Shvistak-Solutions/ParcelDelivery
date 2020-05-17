package com.example.ParcelDelivery.ui.storekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.R.layout;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;


public class StorehouseActivity extends AppCompatActivity {

    ListView  packList;
    DatabaseHelper dbH;
    SearchView searchView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_storehouse);

        packList = (ListView)findViewById(R.id.ID_PACK_LIST);


        dbH = new DatabaseHelper(this);
        Cursor data = dbH.getAllParcelIdByStatus("3");
        ArrayList<String>  list = new ArrayList<>();
        searchView = (SearchView)findViewById(R.id.ID_SEARCH_FOR_PACK);
        ArrayAdapter<String> arrayAdapter;


        if( data.getCount() == 0){
            Toast.makeText(StorehouseActivity.this,"Empty",Toast.LENGTH_LONG).show();
        }
        else{
            while(data.moveToNext()){
                list.add("IDENTYFIKATOR PACZKI : "+data.getString(0));
            }
            arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,list);
            packList.setAdapter(arrayAdapter);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    arrayAdapter.getFilter().filter(text);
                    return false;
                }
            });

        }




        packList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(StorehouseActivity.this, PackActivity.class));

                String idMessage = packList.getItemAtPosition(position).toString();
                idMessage = idMessage.replaceAll("[^0-9]+", "");
                Intent intent = new Intent(getApplicationContext(), PackActivity.class);
                //TextView id_print = (TextView)findViewById(R.id.ID_PACKID_VIEW);
                intent.putExtra("EXTRA_PACK_ID" ,idMessage);
                startActivity(intent);
            }
        });

    }
}
