package com.example.ParcelDelivery.ui.storekeeper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.avatar.AvatarActivity;
import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.user.UserDetailsActivity;
import com.example.ParcelDelivery.ui.user.UserPresence;

public class StorekeeperActivity extends AppCompatActivity {

    int userId;
    int test = 0;

    Intent intent;
    private DatabaseHelper dbH;

    boolean presentSet = false;
    boolean absentSet = false;
    boolean workDay = true;
    UserPresence presence;
    Button buttonMyAccount, buttonPresence, buttonLogout, buttonParcelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storekeeper);

        userId = getIntent().getIntExtra("userId", 0);

        prepareData();

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        // logout

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StorekeeperActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StorekeeperActivity.this, LoginActivity.class));
            }
        });



        buttonMyAccount.setOnClickListener(v -> {
            Intent intent = new Intent(StorekeeperActivity.this, UserDetailsActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("id", userId);
            startActivity(intent);
        });


        buttonParcelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StorekeeperActivity.this, ParcelListActivity.class);
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
                Intent intent = new Intent(StorekeeperActivity.this, AvatarActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("class","com.example.ParcelDelivery.ui.storekeeper.StorekeeperActivity");
                startActivity(intent);
            }
        });

    }

    private void prepareData(){
        buttonMyAccount = (Button) findViewById(R.id.buttonStoreKeeperAccount);
        buttonPresence = (Button) findViewById(R.id.buttonPresence);
        buttonLogout = findViewById(R.id.buttonLogoutStore);
        buttonParcelList = (Button)findViewById(R.id.ID_VIEW_STOREHOUSE);

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
