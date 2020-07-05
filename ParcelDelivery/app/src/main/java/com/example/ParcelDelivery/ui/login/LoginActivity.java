package com.example.ParcelDelivery.ui.login;

        import androidx.activity.OnBackPressedCallback;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;

        import android.Manifest;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.ParcelDelivery.ui.coordinator.CoordinatorActivity;
        import com.example.ParcelDelivery.ui.courier.CourierActivity;
        import com.example.ParcelDelivery.R;
        import com.example.ParcelDelivery.ui.storekeeper.StorekeeperActivity;
        import com.example.ParcelDelivery.db.DatabaseHelper;
        import com.example.ParcelDelivery.ui.manager.ManagerActivity;
        import com.example.ParcelDelivery.ui.password_reset.ForgotPasswordActivity;
        import android.widget.TextView;

        import static com.example.ParcelDelivery.db.DatabaseHelper.md5;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        db = new DatabaseHelper(this);
        if(db.getUpdate()){
            db.dbSeed();
            db.setUpdate(false);
        }
        db.updateDates();

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btLogin);

        ActivityCompat.requestPermissions(this, new String[]
                { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE }, 0);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        forgotPassword = (TextView)findViewById(R.id.ID_PASSWORD_RESET);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }



    private void validate()
    {
        Intent intent1 = new Intent(LoginActivity.this, ManagerActivity.class);
        Intent intent2 = new Intent(LoginActivity.this, CoordinatorActivity.class);
        Intent intent3 = new Intent(LoginActivity.this, CourierActivity.class);
        Intent intent4 = new Intent(LoginActivity.this, StorekeeperActivity.class);
        int id = db.getUserId(Name.getText().toString().toLowerCase().replaceAll("\\s+",""));
        String position = db.getData("stanowisko", "Pracownicy", id);
        if(id == 0)
        {
            Toast.makeText(getApplicationContext(),"Nie ma takiego użytkownika w bazie danych.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if( !(md5(Password.getText().toString()).equals(db.getData("haslo","Konta", id ))))
            {
                Toast.makeText(getApplicationContext(), "Błędne hasło.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                switch(position)
                {
                    case "Kurier":
                        intent3.putExtra("userId", id );
                        startActivity(intent3);
                        break;
                    case "Magazynier":
                        intent4.putExtra("userId", id );
                        startActivity(intent4);
                        break;
                    case "Koordynator":
                        intent2.putExtra("userId", id );
                        startActivity(intent2);
                        break;
                    case "Manager":
                        intent1.putExtra("userId", id );
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }

            }
        }
    }

}
