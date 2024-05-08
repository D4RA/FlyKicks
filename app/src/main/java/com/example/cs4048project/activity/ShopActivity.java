 package com.example.cs4048project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Items;
import com.example.cs4048project.MyAdapter;
import com.example.cs4048project.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

 public class ShopActivity extends AppCompatActivity implements OnMapReadyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Shop");

        HeaderFooterHelper.setupHeaderButtons(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<Items> items = new ArrayList<>();
        items.add(new Items("smabas",R.drawable.sambas,"500",new LatLng(40.7128, -74.0060)));
        items.add(new Items("smabas",R.drawable.sambas,"500",new LatLng(40.7128, -74.0060)));
        items.add(new Items("smabas",R.drawable.sambas,"500",new LatLng(40.7128, -74.0060)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }


     @Override
     public void onMapReady(@NonNull GoogleMap googleMap) {

     }

     @Override
     public void onPointerCaptureChanged(boolean hasCapture) {
         super.onPointerCaptureChanged(hasCapture);
     }
 }