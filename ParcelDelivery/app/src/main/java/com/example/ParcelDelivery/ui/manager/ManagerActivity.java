package com.example.ParcelDelivery.ui.manager;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.avatar.AvatarActivity;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.user.UserDetailsActivity;
import com.example.ParcelDelivery.ui.user.UserListActivity;
import com.example.ParcelDelivery.ui.user.UserScheduleActivity;

public class ManagerActivity extends AppCompatActivity {

    Intent intent;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        userId = getIntent().getIntExtra("userId", 0);

        Button buttonUserList = (Button) findViewById(R.id.buttonUserList);
        Button buttonSchedule = (Button) findViewById(R.id.buttonScheduleView);
        Button buttonMyAccount = (Button) findViewById(R.id.buttonMyAccount);

        // logout
        Button buttonLogout = findViewById(R.id.buttonLogoutManag);
        buttonLogout.setOnClickListener( v -> {
            Toast.makeText(ManagerActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
        });

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        buttonSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, UserScheduleActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);

        });

        buttonUserList.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, UserListActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        buttonMyAccount.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerActivity.this, UserDetailsActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("id", userId);
            startActivity(intent);
        });

        //---------------- AVATAR -----------------------------
        ImageView mainAvatarView = (ImageView)findViewById(R.id.ID_MAIN_AVATAR_VIEW);
        Bitmap bitmap;
        DatabaseHelper dbH = new DatabaseHelper(this);
        int avatarExists = dbH.doesUserHasAvatar(userId);
        if( avatarExists == 0){
            mainAvatarView.setImageResource(R.drawable.avatar0);
        }
        else{
            bitmap = dbH.getAvatarAsBitmap(userId);
            mainAvatarView.setImageBitmap(bitmap);
        }

        Button avatar = (Button) findViewById(R.id.ID_GO_TO_AVATAR);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerActivity.this, AvatarActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("class","com.example.ParcelDelivery.ui.manager.ManagerActivity");
                startActivity(intent);
            }
        });

    }
}
