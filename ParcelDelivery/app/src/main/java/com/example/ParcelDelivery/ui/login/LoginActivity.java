package com.example.ParcelDelivery.ui.login;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.ParcelDelivery.CoordinatorActivity;
        import com.example.ParcelDelivery.CourierActivity;
        import com.example.ParcelDelivery.R;
        import com.example.ParcelDelivery.StorekeeperActivity;
        import com.example.ParcelDelivery.db.DatabaseHelper;
        import com.example.ParcelDelivery.ui.manager.ManagerActivity;

        import static com.example.ParcelDelivery.db.DatabaseHelper.md5;


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
                validate();
            }
        });
    }



    private void validate()
    {
        Intent intent1 = new Intent(LoginActivity.this, ManagerActivity.class);
        Intent intent2 = new Intent(LoginActivity.this, CoordinatorActivity.class);
        Intent intent3 = new Intent(LoginActivity.this, CourierActivity.class);
        Intent intent4 = new Intent(LoginActivity.this, StorekeeperActivity.class);
        int id = db.getUserId(Name.getText().toString());
        String position = db.GetUserData("stanowisko", "Pracownicy", id);
        if(id == 0)
        {
            Toast.makeText(getApplicationContext(),"Nie ma takiego użytkownika w bazie danych.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if( !(md5(Password.getText().toString()).equals(db.GetUserData("haslo","Konta", id ))))
            {
                Toast.makeText(getApplicationContext(), "Błędne hasło.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                switch(position)
                {
                    case "0":
                        startActivity(intent3);
                        break;
                    case "1":
                        startActivity(intent4);
                        break;
                    case "2":
                        startActivity(intent2);
                        break;
                    case "3":
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }

            }
        }
    }

}
