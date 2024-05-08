package com.example.cs4048project;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MyViewHolder extends RecyclerView.ViewHolder{
ImageView imageView;
TextView priceView, titleView;
MapView mapView;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView2);
        priceView = itemView.findViewById(R.id.pricetxt);
        titleView = itemView.findViewById(R.id.title);
        mapView = itemView.findViewById(R.id.viewmap);
    mapView.onCreate(null);
    mapView.getMapAsync(new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    });
    }

}