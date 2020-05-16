package com.example.ParcelDelivery.ui.parcel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ParcelDelivery.R;
import com.example.ParcelDelivery.db.DatabaseHelper;

import java.util.HashMap;

public class ParcelListRecyclerViewAdapter extends RecyclerView.Adapter<ParcelListRecyclerViewAdapter.ViewHolder>{

    HashMap<String,String> mParcelData = new HashMap<>();

    public ParcelListRecyclerViewAdapter(HashMap<String, String> mParcelData) {
        this.mParcelData = mParcelData;
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
        //TODO: get contents of mParcelData hashmap and put them in viewholder fields

    }

    @Override
    public int getItemCount() {
        return mParcelData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView parcel_id;
        private TextView parcel_status;
        private TextView parcel_courier_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parcel_id = itemView.findViewById(R.id.parcel_id);
            parcel_status = itemView.findViewById(R.id.parcel_status);
            parcel_courier_id = itemView.findViewById(R.id.parcel_courier_id);

        }
    }



}
