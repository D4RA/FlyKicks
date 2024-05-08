package com.example.cs4048project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {



    Context context;
    List<Items> Items;
    public MyAdapter(Context context, List<Items> items) {
        this.context = context;
        Items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.viewholder_pop_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleView.setText(Items.get(position).getTitle());
        holder.priceView.setText(Items.get(position).getPrice());
        holder.imageView.setImageResource(Items.get(position).getPicUrl());

        LatLng itemLocation =Items.get(position).getLocation();
        if(itemLocation != null){
            holder.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    googleMap.addMarker(new MarkerOptions().position(itemLocation).title(Items.get(holder.getAdapterPosition()).getTitle()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(itemLocation));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
