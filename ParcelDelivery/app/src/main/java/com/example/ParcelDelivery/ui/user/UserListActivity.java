package com.example.ParcelDelivery.ui.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.avatar.AvatarActivity;
import com.example.ParcelDelivery.ui.manager.ManagerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


@SuppressLint("Registered")
public class UserListActivity extends AppCompatActivity {

    Button saveBtn;
    SearchView search;
    Intent intent;
    int userId;
    DatabaseHelper db;
    ArrayList<HashMap<String, String>> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        userId = getIntent().getIntExtra("userId", 0);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                intent = new Intent(UserListActivity.this, ManagerActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        db = new DatabaseHelper(this);
        final ListView lv = (ListView) findViewById(R.id.user_list);
        userList = db.getData(new String[]{"id","imie","nazwisko","email","stanowisko"}, "Pracownicy", null,null);
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
            int id = Integer.parseInt(a.get("id"));
            int avatarExists = db.doesUserHasAvatar(id);
            if( avatarExists == 0){
                a.put("avatar",Integer.toString(R.drawable.avatar0));
            }
            else{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Bitmap bitmap = db.getAvatarAsBitmap(id);
                    Uri uri =getImageUri(this,bitmap);
                    a.put("avatar",uri.toString());
                }else
                {
                    a.put("avatar",Integer.toString(R.drawable.avatar0));
                }
            }

        }
        final SimpleAdapter adapter = new SimpleAdapter(UserListActivity.this, userList, R.layout.row_userlist,new String[]{"avatar","imie","nazwisko","email","stanowisko"}, new int[]{R.id.imageViewList,R.id.textListName, R.id.textListSurname,R.id.textListEmail, R.id.textListPosition});
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
                intent.putExtra("userId", userId);
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
        deleteImage();
        super.onStop();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void deleteImage(){
        for (HashMap<String,String> a : userList){
            if(!a.get("avatar").equals(Integer.toString(R.drawable.avatar0))){
               int cnt =  getApplicationContext().getContentResolver().delete(Uri.parse(a.get("avatar")), null, null);
               //Toast.makeText(getApplicationContext(),"cnt= "+cnt+" uri= "+a.get("avatar"),Toast.LENGTH_LONG).show();
            }
        }
    }
}


