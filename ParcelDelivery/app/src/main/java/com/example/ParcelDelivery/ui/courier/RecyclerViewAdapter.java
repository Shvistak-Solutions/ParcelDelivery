package com.example.ParcelDelivery.ui.courier;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ParcelDelivery.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images) {
        this.imageNames = imageNames;
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_parcellist, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Glide.with(context)
                .asBitmap()
                .load(images.get(position))
                .into(holder.image);

        holder.imageName.setText(imageNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,imageNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.parcelImage);
            imageName = itemView.findViewById(R.id.imageName);
            parentLayout = itemView.findViewById(R.id.parentLayout);

        }
    }

}
