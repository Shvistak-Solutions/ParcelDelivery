package com.example.ParcelDelivery.ui.storekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.parcel.ParcelListActivity;

public class PackActivity extends AppCompatActivity {

    Button changeStatus;
    private DatabaseHelper dbH;
    String idMessage;
    private int userId;
    private int parcelId;
    TextView statusPrint;
    TextView courierPrint;
    TextView idPrint;
    Intent intent;

    private static final String TAG = "PackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        Log.d(TAG, "onCreate: started");
        intent = getIntent();
        userId = intent.getIntExtra("userId",0);
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

        changeStatus = (Button)findViewById(R.id.ID_CHANGE_PACK_STATUS);



        dbH = new DatabaseHelper(this);

        idPrint = (TextView)findViewById(R.id.ID_PACKID_VIEW);
        statusPrint = (TextView)findViewById(R.id.ID_STATUS_VIEW);
        courierPrint = (TextView)findViewById(R.id.ID_COURIER_INFO_VIEW);

        if( dbH.checkIfInStorehouse(idMessage)) {



            Cursor data = dbH.getCourierInfoByPack(idMessage);

            data.moveToFirst();
            String courierId = data.getString(0);
            String courierName = data.getString(2);
            String courierName2 = data.getString(1);

            idPrint.setText("ID paczki : " + idMessage);
            statusPrint.setText("Status: w magazynie");
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
        }

    }

    private void updateStatus(){
        dbH.changePackStatus(idMessage,"4");
        Toast.makeText(this,"Przekazano do dostarczenia",Toast.LENGTH_SHORT).show();
        idPrint.setText("ID paczki : " + idMessage);
        statusPrint.setText("Status: w drodze do odbiorcy");
        courierPrint.setText("DO WYZNACZENIA !!!");
        changeStatus.setVisibility(View.INVISIBLE);

    }

}
