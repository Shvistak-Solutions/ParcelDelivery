package com.example.ParcelDelivery.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("Registered")
public class UserListActivity extends AppCompatActivity {

    Button saveBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        final DatabaseHelper db = new DatabaseHelper(this);
        ListView lv = (ListView) findViewById(R.id.user_list);
        final ArrayList<HashMap<String, String>> userList = db.GetUsers();
        final ListAdapter adapter = new SimpleAdapter(UserListActivity.this, userList, R.layout.list_row,new String[]{"imie","nazwisko","email","stanowisko"}, new int[]{R.id.textListName, R.id.textListSurname,R.id.textListEmail, R.id.textListPosition});
        lv.setAdapter(adapter);
        lv.setClickable(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> userDetail = userList.get(position);
                intent = new Intent(UserListActivity.this, UserDetailsActivity.class);
                //String idWorker = Integer.toString(db.getUserId(userDetail.get("email")));
                int idWorker = db.getUserId(userDetail.get("email"));
                intent.putExtra("id", idWorker );
                startActivity(intent);
            }
        });



        saveBtn = (Button)findViewById(R.id.buttonAddUser);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserListActivity.this, UserAddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}


