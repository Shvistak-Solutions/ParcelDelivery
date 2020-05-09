package com.example.ParcelDelivery.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.R;

public class UserAddActivity extends AppCompatActivity {

    EditText name, surname, pesel, email, idNum, address, postal;
    Spinner position;
    Button saveBtn;
    Intent intent;

    //    public void toUserList(View view) {
//        Intent intent = new Intent(UserAddActivity.this, UserListActivity.class);
//        startActivity(intent);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useradd);

        name = (EditText)findViewById(R.id.editTextName);
        surname = (EditText)findViewById(R.id.editTextSurname);
        email = (EditText)findViewById(R.id.editTextEmail) ;
        position = (Spinner)findViewById(R.id.spinnerPosition);
        pesel = (EditText)findViewById(R.id.editTextPesel);
        idNum = (EditText)findViewById(R.id.editTextIdNum);
        address = (EditText)findViewById(R.id.editTextAddress);
        postal = (EditText)findViewById(R.id.editTextPostal);
        saveBtn = (Button)findViewById(R.id.buttonResetPasswd);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userSurname = surname.getText().toString();
                String userEmail = email.getText().toString();
                String userPesel = pesel.getText().toString();
                String userId = idNum.getText().toString();
                String userAddress = surname.getText().toString();
                String userPostal = postal.getText().toString();

                String SpinnerValue = position.getSelectedItem().toString();
                int userPosition = positionStringToint(SpinnerValue);
                DatabaseHelper dbHandler = new DatabaseHelper(UserAddActivity.this);
                if(dbHandler.insertUserDetails(userName,userSurname,userPosition,userEmail,userPesel,userId,userAddress,userPostal) != -1)
                {
                    intent = new Intent(UserAddActivity.this, com.example.ParcelDelivery.ui.user.UserListActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Dodano sukcesywnie",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Oj... Coś poszło nie tak",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private int positionStringToint(String position)
    {
        int userPosition;
        switch(position)
        {
            case "Kurier":
                userPosition = 0;
                break;
            case "Magazynier":
                userPosition = 1;
                break;
            case "Koordynator":
                userPosition = 2;
                break;
            case "Manager":
                userPosition = 3;
                break;
            default:
                userPosition = 0;
                break;
        }
        return userPosition;
    }

}


