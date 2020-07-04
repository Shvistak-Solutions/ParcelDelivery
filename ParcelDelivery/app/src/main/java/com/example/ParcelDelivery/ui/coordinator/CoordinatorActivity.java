package com.example.ParcelDelivery.ui.coordinator;

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
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;
import com.example.ParcelDelivery.ui.password_reset.ChangePasswordActivity;
import com.example.ParcelDelivery.ui.user.UserDetailsActivity;
import com.example.ParcelDelivery.ui.user.UserPresence;

public class CoordinatorActivity extends AppCompatActivity {

    int userId;

    boolean presentSet = false;
    boolean absentSet = false;
    boolean workDay = true;
    UserPresence presence;
    Button buttonMyAccount, buttonPresence, buttonLogout, buttonParcelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        userId = getIntent().getIntExtra("userId", 0);

        // logout

        prepareData();


        buttonMyAccount.setOnClickListener(v -> {
            Intent intent = new Intent(CoordinatorActivity.this, UserDetailsActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("id", userId);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, LoginActivity.class);
                Toast.makeText(CoordinatorActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                //Toast.makeText(CoordinatorActivity.this, "Nope", Toast.LENGTH_SHORT).show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // list of packages
        buttonParcelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, ParcelListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
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
                Intent intent = new Intent(CoordinatorActivity.this, AvatarActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("class","com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity");
                startActivity(intent);
            }
        });


        //--------------- ZMIANA HASLA - DEMO
        Button changePassword = (Button)findViewById(R.id.ID_CHANGE_PASSWORD);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoordinatorActivity.this, ChangePasswordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        //------------------------------------
    }

    private void prepareData(){
        buttonMyAccount = (Button) findViewById(R.id.buttonCoordinatorAccount);
        buttonPresence = (Button) findViewById(R.id.buttonPresence);
        buttonLogout = findViewById(R.id.buttonLogoutCoord);
        buttonParcelList = (Button) findViewById(R.id.buttonParcelList);

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
