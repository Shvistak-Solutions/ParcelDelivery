package com.example.ParcelDelivery.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.R;

public class UserAddActivity extends AppCompatActivity {

    TextView name, surname, pesel, email, idNum, address, postal, position;
    Spinner positionSpinner;
    Button saveBtn, buttonGone1, buttonGone2;
    Intent intent;
    DatabaseHelper dbHandler;
    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_userdetails1);
        findLayoutItems();
        userId = getIntent().getIntExtra("userId", 0);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                intent = new Intent(UserAddActivity.this, UserListActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

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
                dbHandler = new DatabaseHelper(UserAddActivity.this);

                String SpinnerValue = positionSpinner.getSelectedItem().toString();
                int userPosition = dbHandler.positionStringToInt(SpinnerValue);

                long res = dbHandler.insertNewUser(userName,userSurname,userPosition,userEmail,userPesel,userId,userAddress,userPostal);
                if(res > 0)
                {
                    intent = new Intent(UserAddActivity.this, com.example.ParcelDelivery.ui.user.UserListActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Dodano sukcesywnie",Toast.LENGTH_SHORT).show();
                }
                else if(res == 0)
                    Toast.makeText(getApplicationContext(), "Pole Imie, Nazwisko i Email nie mogą być puste",Toast.LENGTH_SHORT).show();

                else
                    Toast.makeText(getApplicationContext(), "Nie udało się dodać pracownika",Toast.LENGTH_SHORT).show();


            }
        });
    }

//    private int positionStringToint(String position)
//    {
//        int userPosition;
//        switch(position)
//        {
//            case "Kurier":
//                userPosition = 0;
//                break;
//            case "Magazynier":
//                userPosition = 1;
//                break;
//            case "Koordynator":
//                userPosition = 2;
//                break;
//            case "Manager":
//                userPosition = 3;
//                break;
//            default:
//                userPosition = 0;
//                break;
//        }
//        return userPosition;
//    }

    private void findLayoutItems()
    {

        positionSpinner = (Spinner)findViewById(R.id.spinnerDetailPosition);

        name = (TextView)findViewById(R.id.textDetailName);
        surname = (TextView)findViewById(R.id.textDetailSurname);
        email = (TextView)findViewById(R.id.textDetailEmail) ;
        position = (TextView) findViewById(R.id.textDetailPosition);
        pesel = (TextView)findViewById(R.id.textDetailPesel);
        idNum = (TextView)findViewById(R.id.textDetailIdNum);
        address = (TextView)findViewById(R.id.textDetailAddress);
        postal = (TextView)findViewById(R.id.textDetailPostal);

//        name.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//        surname.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        address.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
//        pesel.setInputType(InputType.TYPE_CLASS_NUMBER);
//        idNum.setInputType(InputType.TYPE_CLASS_TEXT);
//        postal.setInputType(InputType.TYPE_CLASS_NUMBER);


        position.setVisibility(View.INVISIBLE);
        positionSpinner.setVisibility(View.VISIBLE);
        


        saveBtn = (Button)findViewById(R.id.buttonEditDetails);
        saveBtn.setText("Zapisz Dane");
        buttonGone1 = (Button)findViewById(R.id.buttonRemoveAccount);
        buttonGone2 = (Button)findViewById(R.id.buttonResetPasswd);
        buttonGone1.setVisibility(View.GONE);
        buttonGone2.setVisibility(View.GONE);


    }


}


