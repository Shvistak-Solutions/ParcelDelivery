package com.example.ParcelDelivery.ui.manager;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.user.UserDetailsActivity;
import com.example.ParcelDelivery.ui.user.UserListActivity;
import com.example.ParcelDelivery.ui.user.UserPresence;

public class ManagerActivity extends AppCompatActivity {

    Intent intent;
    int userId;
    boolean presentSet = false;
    boolean absentSet = false;
    boolean workDay = true;
    UserPresence presence;
    Button buttonUserList, buttonMyAccount, buttonPresence, buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        userId = getIntent().getIntExtra("userId", 0);


        prepareData();


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
        if(workDay) {
            buttonPresence.setOnClickListener(v -> {
                if (presentSet) {
                    presence.addExit();
                    absentSet = true;
                    buttonPresence.setEnabled(false);
                    buttonPresence.setText(R.string.presence_is_set);
                } else {
                    presence.addEnter();
                    presentSet = true;
                    buttonPresence.setText(R.string.absent);
                }
            });
        }


        //---------------- AVATAR -----------------------------
//        ImageView mainAvatarView = (ImageView)findViewById(R.id.ID_MAIN_AVATAR_VIEW);
//        Bitmap bitmap;
//        DatabaseHelper dbH = new DatabaseHelper(this);
//        int avatarExists = dbH.doesUserHasAvatar(userId);
//        if( avatarExists == 0){
//            mainAvatarView.setImageResource(R.drawable.avatar0);
//        }
//        else{
//            bitmap = dbH.getAvatarAsBitmap(userId);
//            mainAvatarView.setImageBitmap(bitmap);
//        }
//
//        Button avatar = (Button) findViewById(R.id.ID_GO_TO_AVATAR);
//        avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ManagerActivity.this, AvatarActivity.class);
//                intent.putExtra("userId", userId);
//                intent.putExtra("class","com.example.ParcelDelivery.ui.manager.ManagerActivity");
//                startActivity(intent);
//            }
//        });

    }

    
    private void prepareData(){
        buttonUserList = (Button) findViewById(R.id.buttonUserList);
        buttonMyAccount = (Button) findViewById(R.id.buttonManagerAccount);
        buttonPresence = (Button) findViewById(R.id.buttonPresence);
        buttonLogout = findViewById(R.id.buttonLogoutManag);

        presence = new UserPresence(userId,this);

        int prs = presence.prepareFields();

        if(prs == -3){
            workDay = false;
            buttonPresence.setText(R.string.not_working);
        }
        else if(prs == -2){
            buttonPresence.setEnabled(false);
            buttonPresence.setText(R.string.presence_is_set);
            absentSet = true;
            presentSet = true;
        }
        else if(prs == -1){
            buttonPresence.setText(R.string.absent);
            presentSet = true;
        }
    }

}
