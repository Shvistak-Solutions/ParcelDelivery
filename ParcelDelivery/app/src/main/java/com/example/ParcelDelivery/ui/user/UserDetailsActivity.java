package com.example.ParcelDelivery.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {

    TextView name, surname, pesel, email, idNum, address, postal, position;
    int userId;
    Button buttonRmv;
    Intent intent;
    DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
        userId = getIntent().getIntExtra("id", 0);

        name = (TextView)findViewById(R.id.textDetailName);
        surname = (TextView)findViewById(R.id.textDetailSurname);
        email = (TextView)findViewById(R.id.textDetailEmail) ;
        position = (TextView) findViewById(R.id.textDetailPosition);
        pesel = (TextView)findViewById(R.id.textDetailPesel);
        idNum = (TextView)findViewById(R.id.textDetailIdNum);
        address = (TextView)findViewById(R.id.textDetailAddress);
        postal = (TextView)findViewById(R.id.textDetailPostal);
        buttonRmv = (Button)findViewById(R.id.buttonRemoveAccount);

        db = new DatabaseHelper(this);
        final HashMap<String,String> details = db.GetUserDetails(userId);
        fillTextViews(details);

        final AlertDialog dialog = removeAlert(details.get("email"));

        buttonRmv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });




    }

    private AlertDialog removeAlert(String email)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
        builder.setMessage("Remove user: "+email)
                .setTitle("Are you sure?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.deleteUser(userId);
                intent = new Intent(UserDetailsActivity.this, UserListActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    private void fillTextViews(HashMap<String,String> details)
    {
        name.setText(details.get("imie"));
        surname.setText(details.get("nazwisko"));
        email.setText(details.get("email"));
        position.setText(details.get("stanowisko"));
        pesel.setText(details.get("pesel"));
        address.setText(details.get("adres"));
        idNum.setText(details.get("nr_dowodu"));
        postal.setText(details.get("kod_pocztowy"));

    }
}
