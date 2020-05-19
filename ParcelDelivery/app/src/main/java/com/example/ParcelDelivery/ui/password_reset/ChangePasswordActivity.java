package com.example.ParcelDelivery.ui.password_reset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

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
    Button nextStep;
    TextView note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        note = (TextView)findViewById(R.id.ID_NOTE_2);
        userId = getIntent().getIntExtra("userId", 0);
        dbH = new DatabaseHelper(this);
        nextStep = (Button) findViewById(R.id.ID_VERIFY_PASSWORD);


        oldPassword = dbH.getData("haslo","Konta",userId).trim();
        oldPasswordTyped = (EditText)findViewById(R.id.ID_OLD_PASSWORD);
        newPassword = (EditText)findViewById(R.id.ID_NEW_PASSWORD);
        newPasswordAgain = (EditText)findViewById(R.id.ID_NEW_PASSWORD_AGAIN);
        newPassword.setVisibility(View.INVISIBLE);
        newPasswordAgain.setVisibility(View.INVISIBLE);


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
            note.setText("Podaj nowe hasło. W obu polach hasło musi byś takie same");
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
            Toast.makeText(this,"Podaj nowe haslo",Toast.LENGTH_SHORT).show();
            newPasswordAgain.setText("");
        }
        else if(password.length() < 5){
            Toast.makeText(this,"Podane haslo jest za krótkie. Minimum 5 znaków",Toast.LENGTH_SHORT).show();
            newPassword.setText("");
        }

        else if(again.isEmpty()){
            Toast.makeText(this,"Powtórz nowe haslo",Toast.LENGTH_SHORT).show();
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
