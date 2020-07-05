package com.example.ParcelDelivery.ui.storekeeper;

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
import androidx.appcompat.app.AppCompatActivity;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;

public class PackActivity extends AppCompatActivity {

    Button buttonChangeStatus;
    private DatabaseHelper dbH;
    String idMessage;
    private int userId;
    private int parcelId;
    TextView statusPrint;
    TextView idPrint;
    TextView senderAddressText;
    TextView recipientAddressText;


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



        dbH = new DatabaseHelper(this);

        idPrint = (TextView)findViewById(R.id.ID_PACKID_VIEW);
        statusPrint = (TextView)findViewById(R.id.ID_STATUS_VIEW);
        senderAddressText = findViewById(R.id.textViewSenderAddress);
        recipientAddressText = findViewById(R.id.textViewRecipientAddress);

        idPrint.setText("Paczka #" + idMessage);
        statusPrint.setText("Status: " + statusIntToText( Integer.parseInt( dbH.getData("status","Paczki",parcelId))));
        int courierId = Integer.parseInt( dbH.getData("id_kuriera","Paczki",parcelId));
        courierPrint.setText(" Kurier: " + dbH.getData("imie","Pracownicy",courierId) + " " + dbH.getData("nazwisko","Pracownicy",courierId) + " #" + dbH.getData("id_kuriera","Paczki",parcelId));
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

        /*if( dbH.checkIfInStorehouse(idMessage)) {



            Cursor data = dbH.getCourierInfoByPack(idMessage);

            data.moveToFirst();
            String courierId = data.getString(0);
            String courierName = data.getString(2);
            String courierName2 = data.getString(1);

            idPrint.setText("ID paczki : " + idMessage );
            statusPrint.setText("Status: w magazynie" );
            courierPrint.setText("Dostarczona do magazynu przez: " + courierName + " " + courierName2 + "  |  ID: " + courierId);


            ((AppCompatButton) changeStatus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStatus();
                }
            });

        }
        else{
            updateStatus();
        }*/

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
