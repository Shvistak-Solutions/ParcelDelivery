package com.example.ParcelDelivery.ui.parcel;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;
import com.example.ParcelDelivery.ui.storekeeper.PackActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ParcelListRecyclerViewAdapter extends RecyclerView.Adapter<ParcelListRecyclerViewAdapter.ViewHolder>{

    private ArrayList<HashMap<String,String>> mParcelData = new ArrayList<>();
    private int userId;
    private int pos;

    public ParcelListRecyclerViewAdapter(ArrayList<HashMap<String, String>> mParcelData, int userId) {
        this.mParcelData = mParcelData;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_list_item,parent,false);

        ViewHolder parcelListViewHolder = new ViewHolder(listItemView);
        return parcelListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        pos = position;
        final int temp = position;
        holder.parcel_id.setText("#" + mParcelData.get(pos).get("id") );
        holder.parcel_courier_id.setText("Kurier: " + mParcelData.get(pos).get("id_kuriera"));

        String status = "?";
        switch (Integer.parseInt(mParcelData.get(pos).get("status"))) {
            case 1:
                status = "Przyjęte do realizacji";
                break;
            case 2:
                status = "Odebrane od nadawcy";
                break;
            case 3:
                status = "W magazynie";
                break;
            case 4:
                status = "W drodze do odbiorcy";
                break;
            case 5:
                status = "Dostarczone";
                break;
            case -1:
                status = "Anulowane";
                break;
            default:
                break;
        }
        holder.parcel_status.setText("Status: " + status);


        holder.parcel_list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext() , PackActivity.class);
                intent.putExtra("userId",userId);
                intent.putExtra("parcelId", Integer.parseInt(mParcelData.get(position).get("id")));

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mParcelData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView parcel_id;
        private TextView parcel_status;
        private TextView parcel_courier_id;
        private ConstraintLayout parcel_list_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parcel_id = itemView.findViewById(R.id.parcel_id);
            parcel_status = itemView.findViewById(R.id.parcel_status);
            parcel_courier_id = itemView.findViewById(R.id.parcel_courier_id);
            parcel_list_item = itemView.findViewById(R.id.parcel_list_item);

        }
    }



}
