 package com.example.cs4048project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Items;
import com.example.cs4048project.MyAdapter;
import com.example.cs4048project.R;

import java.util.ArrayList;

 public class ShopActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Shop");

        HeaderFooterHelper.setupHeaderButtons(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ArrayList<Items> items = new ArrayList<>();
        items.add(new Items("smabas",R.drawable.sambas,"500"));
        items.add(new Items("smabas",R.drawable.sambas,"500"));
        items.add(new Items("smabas",R.drawable.sambas,"500"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
    }




}