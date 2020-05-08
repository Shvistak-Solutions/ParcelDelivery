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

    EditText name, surname, pesel, idNum, address, postal;
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

        name = (EditText)findViewById(R.id.editName);
        surname = (EditText)findViewById(R.id.editSurname);
        position = (Spinner)findViewById(R.id.spinnerPlacement);
        pesel = (EditText)findViewById(R.id.editPesel);
        idNum = (EditText)findViewById(R.id.editIdNum);
        address = (EditText)findViewById(R.id.editAddress);
        postal = (EditText)findViewById(R.id.editPostal);
        saveBtn = (Button)findViewById(R.id.buttonAdd);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userSurname = surname.getText().toString();
                String userPesel = pesel.getText().toString();
                String userId = idNum.getText().toString();
                String userAddress = surname.getText().toString();
                String userPostal = postal.getText().toString();
                int userPosition = 1;
                String SpinnerValue = position.toString();
                switch(SpinnerValue)
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
                DatabaseHelper dbHandler = new DatabaseHelper(UserAddActivity.this);
                dbHandler.insertUserDetails(userName,userSurname,userPosition,userPesel,userId,userAddress,userPostal);
                intent = new Intent(UserAddActivity.this, com.example.ParcelDelivery.ui.user.UserListActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Details Inserted Successfully",Toast.LENGTH_SHORT).show();
            }
        });
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
        super.onStop();
    }
}
