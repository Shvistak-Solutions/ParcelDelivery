package com.example.ParcelDelivery.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.manager.ManagerActivity;

import java.util.ArrayList;
import java.util.HashMap;


@SuppressLint("Registered")
public class UserListActivity extends AppCompatActivity {

    Button saveBtn;
    SearchView search;
    Intent intent;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        userId = getIntent().getIntExtra("userId", 0);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(UserListActivity.this, ManagerActivity.class));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        final DatabaseHelper db = new DatabaseHelper(this);
        final ListView lv = (ListView) findViewById(R.id.user_list);
        final ArrayList<HashMap<String, String>> userList = db.getData(new String[]{"imie","nazwisko","email","stanowisko"}, "Pracownicy", null,null);
        String myMail = db.getData("email","Pracownicy",userId);
        int i = 0;
        for( HashMap<String,String> a : userList)
        {
            String toCheck = a.get("email");
            if(toCheck.equals(myMail)) {
                userList.remove(i);
                break;
            }
            i++;
        }
        final SimpleAdapter adapter = new SimpleAdapter(UserListActivity.this, userList, R.layout.row_userlist,new String[]{"imie","nazwisko","email","stanowisko"}, new int[]{R.id.textListName, R.id.textListSurname,R.id.textListEmail, R.id.textListPosition});
        lv.setAdapter(adapter);
        lv.setClickable(true);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String email = null;
                email = getEmailFromAdapter(adapter, position);
                intent = new Intent(UserListActivity.this, UserDetailsActivity.class);
                int idWorker = db.getUserId(email);
                intent.putExtra("id", idWorker );
                intent.putExtra("userId", userId);
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

    private String getEmailFromAdapter(SimpleAdapter adapter, int position)
    {
        String str = adapter.getItem(position).toString();
        str = str.substring(1,str.length()-1);
        String[] lol = str.split("email=");
        lol = lol[lol.length-1].split(" ");
        return lol[0];
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


