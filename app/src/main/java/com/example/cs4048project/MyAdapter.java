package com.example.cs4048project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        holder.priceView.setText(String.valueOf(Items.get(position).getPrice()));
        holder.usernameView.setText(Items.get(position).getUsername());
        String imageUrl = Items.get(position).getPicUrl();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.baseline_attach_money_24)
                .error(R.drawable.baseline_attach_money_24)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}