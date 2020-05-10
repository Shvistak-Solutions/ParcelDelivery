package com.example.ParcelDelivery.ui.password_reset;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.R.layout;


import com.example.ParcelDelivery.R; //parceldelivery.R;


public class ForgotPasswordActivity extends AppCompatActivity {

    //private AppCompatButton resetButton;
    private Button resetButton;
    private EditText emailOrigin;
    private DatabaseHelper dbH;
    private TextView passwordPrint;
    private TextView newPasswordSign;
    private TextView note;


    String email2;
    String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_forgot_password);
        resetButton = (Button)findViewById(R.id.ID_PASSWORD_RESET_BUTTON);
        emailOrigin = (EditText)findViewById(R.id.ID_USERNAME_RESET);
        passwordPrint = (TextView)findViewById(R.id.ID_NEW_PASSWORD);
        passwordPrint.setVisibility(View.INVISIBLE);
        newPasswordSign = (TextView)findViewById(R.id.ID_NEW_PASSWORD_TEXT);
        newPasswordSign.setVisibility(View.INVISIBLE);
        note = (TextView)findViewById(R.id.ID_NOTE);
        note.setText("Po podaniu prawidlowego adresu email i kliknieciu klawisza resetujacego haslo zostanie wygenerowany nowy kod");
        //email = (EditText)


        dbH = new DatabaseHelper(this);
        //onClicked

        ((AppCompatButton)resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email2 = emailOrigin.getText().toString().trim();
                updatePassword();
            }
        });

    }


    private void updatePassword(){
        if( email2.isEmpty() )
        {
            Toast.makeText(this,"Podaj adres email",Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbH.checkIfExists(email2)) {
            note.setVisibility(View.INVISIBLE);

            ForgotPasswordGenerator password1 = new ForgotPasswordGenerator();
            password1.numberOfCharacters = 6;
            password1.makeBox();
            password1.generateBox();
            password1.generateLetter();
            password2 = password1.generatePassword();

            dbH.updateForgotenPassword(email2, password2);

            newPasswordSign.setVisibility(View.VISIBLE);
            passwordPrint.setVisibility(View.VISIBLE);
            passwordPrint.setTextSize(35);
            passwordPrint.setText(password2);
            Toast.makeText(this,"Powroc do strony logowania i zaloguj sie za pomoca nowego hasla",Toast.LENGTH_LONG).show();




        }
        else {
            Toast.makeText(this,"Nieprawidlowy adres email",Toast.LENGTH_LONG).show();
            return;

        }


        //finish();


    }
}
