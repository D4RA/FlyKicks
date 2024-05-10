package com.example.cs4048project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView priceView, titleView, usernameView;
    public MyViewHolder( View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView2);
        priceView = itemView.findViewById(R.id.shoePrice);
        titleView = itemView.findViewById(R.id.title);
        usernameView = itemView.findViewById(R.id.username);

    }
}