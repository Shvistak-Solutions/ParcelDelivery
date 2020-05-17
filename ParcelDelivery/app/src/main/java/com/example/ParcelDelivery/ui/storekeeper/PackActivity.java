package com.example.ParcelDelivery.ui.storekeeper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;

public class PackActivity extends AppCompatActivity {

    Button changeStatus;
    private DatabaseHelper dbH;
    String idMessage;
    TextView statusPrint;
    TextView courierPrint;
    TextView idPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);

        changeStatus = (Button)findViewById(R.id.ID_CHANGE_PACK_STATUS);


        Intent intent = getIntent();
        idMessage = intent.getStringExtra("EXTRA_PACK_ID");

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
