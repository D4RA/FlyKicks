package com.example.cs4048project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {



    Context context;
    List<Items> Items;
    public MyAdapter(Context context, List<com.example.cs4048project.Items> items) {
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
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
