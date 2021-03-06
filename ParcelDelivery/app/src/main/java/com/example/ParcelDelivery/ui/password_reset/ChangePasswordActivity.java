package com.example.ParcelDelivery.ui.password_reset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.login.LoginActivity;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    int userId;
    private DatabaseHelper dbH;
    String oldPassword;
    String password;
    String again;
    EditText oldPasswordTyped;
    EditText newPassword;
    EditText newPasswordAgain;
    String passwordCompare;
    Button nextStep, buttonLogout;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        note = (TextView)findViewById(R.id.ID_NOTE_2);
        userId = getIntent().getIntExtra("thisUserId", 0);
        dbH = new DatabaseHelper(this);
        nextStep = (Button) findViewById(R.id.ID_VERIFY_PASSWORD);

        oldPassword = dbH.getData("haslo","Konta",userId).trim();
        oldPasswordTyped = (EditText)findViewById(R.id.ID_OLD_PASSWORD);
        newPassword = (EditText)findViewById(R.id.ID_NEW_PASSWORD);
        newPasswordAgain = (EditText)findViewById(R.id.ID_NEW_PASSWORD_AGAIN);
        newPassword.setVisibility(View.INVISIBLE);
        newPasswordAgain.setVisibility(View.INVISIBLE);

        buttonLogout = findViewById(R.id.buttonLogoutPassword);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangePasswordActivity.this, "Wylogowano", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
            }
        });


        ((AppCompatButton)nextStep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordCompare = oldPasswordTyped.getText().toString().trim();
                passwordCompare = dbH.md5(passwordCompare);
                newPasswordStep1();
            }
        });
    }


    private void newPasswordStep1(){
        if( oldPassword.equals(passwordCompare) ){
            nextStep.setText("Zmień hasło");
            oldPasswordTyped.setVisibility(View.INVISIBLE);
            newPassword.setVisibility(View.VISIBLE);
            newPasswordAgain.setVisibility(View.VISIBLE);
            ((AppCompatButton)nextStep).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password = newPassword.getText().toString().trim();
                    again = newPasswordAgain.getText().toString().trim();
                    newPasswordStep2();
                }
            });


        }
        else if (passwordCompare.equals(dbH.md5(""))){
            Toast.makeText(this,"Podaj obecne hasło",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Niepoprawne hasło",Toast.LENGTH_SHORT).show();
            oldPasswordTyped.setText("");
        }
    }


    private void newPasswordStep2(){

        if(password.isEmpty()){
            Toast.makeText(this,"Podaj nowe hasło",Toast.LENGTH_SHORT).show();
            newPasswordAgain.setText("");
        }
        else if(password.length() < 4){
            Toast.makeText(this,"Podane hasło jest za krótkie - minimum 5 znaków",Toast.LENGTH_SHORT).show();
            newPassword.setText("");
            newPasswordAgain.setText("");
        }

        else if(again.isEmpty()){
            Toast.makeText(this,"Powtórz nowe hasło",Toast.LENGTH_SHORT).show();
        }
        else if( !password.equals(again) ){
            Toast.makeText(this,"W obu polach hasło musi byś takie same",Toast.LENGTH_SHORT).show();
            newPassword.setText("");
            newPasswordAgain.setText("");

        }
        else{
            String email = dbH.getData("email","Konta",userId);
            dbH.updatePassword(email,password);
            Toast.makeText(this,"Hasło zostało zmienione",Toast.LENGTH_SHORT).show();
        }
    }
}
