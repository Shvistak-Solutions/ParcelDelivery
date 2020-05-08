package com.example.ParcelDelivery.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.manager.ManagerActivity;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        db = new DatabaseHelper(this);
        db.dbSeed(); // to robi dropa i insertuje seeda

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }



    private void validate(String name, String password)
    {
        if((name.equals("siwy@email.com")) && (password.equals("1234")))
        {
            Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
            startActivity(intent);
        }
    }

}
