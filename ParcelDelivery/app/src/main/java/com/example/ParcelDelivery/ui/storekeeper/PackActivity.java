package com.example.ParcelDelivery.ui.storekeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;

public class PackActivity extends AppCompatActivity {

    Button buttonChangeStatus;
    Button buttonRemoveParcel;
    private DatabaseHelper dbH;
    String idMessage;
    private int userId;
    private int parcelId;
    TextView statusPrint;
    TextView idPrint;
    TextView senderAddressText;
    TextView recipientAddressText;
    TextView courierIdText;

    Spinner spinnerStatusSelect;

    Intent intent;

    private static final String TAG = "PackActivity";
    private static final String[] stats = {"Przyjęte do realizacji","Odebrane od Nadawcy", "W magazynie", "W drodze do odbiorcy", "Dostarczone", "Anulowane"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        Log.d(TAG, "onCreate: started");
        intent = getIntent();
        userId = intent.getIntExtra("userId",1);
        parcelId = intent.getIntExtra("parcelId",1);

        idMessage = Integer.toString(parcelId);
        // back button
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PackActivity.this, ParcelListActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        buttonChangeStatus = (Button)findViewById(R.id.buttonChangeStatus);
        buttonRemoveParcel = findViewById(R.id.buttonRemoveParcel);


        dbH = new DatabaseHelper(this);

        idPrint = (TextView)findViewById(R.id.ID_PACKID_VIEW);
        statusPrint = (TextView)findViewById(R.id.ID_STATUS_VIEW);
        senderAddressText = findViewById(R.id.textViewSenderAddress);
        recipientAddressText = findViewById(R.id.textViewRecipientAddress);
        courierIdText = findViewById(R.id.textViewCourierId);


        idPrint.setText("Paczka #" + idMessage);
        statusPrint.setText("Status: " + statusIntToText( Integer.parseInt( dbH.getData("status","Paczki",parcelId))));
        int courierId = Integer.parseInt( dbH.getData("id_kuriera","Paczki",parcelId));
        courierIdText.setText(" Kurier: " + dbH.getData("imie","Pracownicy",courierId) + " " + dbH.getData("nazwisko","Pracownicy",courierId) + " #" + dbH.getData("id_kuriera","Paczki",parcelId));
        senderAddressText.setText("Adres nadawcy: "+ dbH.getData("adres_nadawcy","Paczki",parcelId));
        recipientAddressText.setText("Adres odbiorcy: "+ dbH.getData("adres_odbiorcy","Paczki",parcelId));

        spinnerStatusSelect = findViewById(R.id.spinnerStatusSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stats);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatusSelect.setAdapter(adapter);

        buttonChangeStatus.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int newStatus = spinnerStatusSelect.getSelectedItemPosition();
                if(newStatus == 5)
                    newStatus = -1;
                else
                    newStatus = newStatus + 1;
                updateStatus(newStatus);
            }
        });

        buttonRemoveParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PackActivity.this)
                        .setTitle("Na pewno?")
                        .setMessage("Na pewno chcesz usunąć paczkę?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(PackActivity.this, "Usunięto paczkę #"+parcelId, Toast.LENGTH_SHORT).show();

                                dbH.removeParcel(parcelId);
                                Intent intent = new Intent(PackActivity.this, ParcelListActivity.class);
                                intent.putExtra("userId",userId);
                                startActivity(intent);
                            }})
                        .setNegativeButton(R.string.cancel, null).show();

            }
        });

        if(dbH.getData("stanowisko","Pracownicy",userId) != "Koordynator")
        {
            buttonRemoveParcel.setVisibility(View.GONE);
        }

    }

    private void updateStatus(int newStatus){
        dbH.changePackStatus(Integer.toString(parcelId),Integer.toString(newStatus));
        Toast.makeText(this,"Zmieniono status",Toast.LENGTH_SHORT).show();
        statusPrint.setText("Status: " + statusIntToText(newStatus));
    }

    private String statusIntToText(int status)
    {
        /*
            1 - przyjete
            -1 anulowane
            2 - odebrane od nadawcy
            3 - w magazynie
            4 - w drodze do odbiorcy
            5 - dostarczone
         */

        if(status > 0 && status < 6)
        {
            return stats[status-1];
        }
        else if(status == -1)
        {
            return "Anulowane";
        }
        else
        {
            return "";
        }
    }

}
