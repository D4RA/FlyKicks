package com.example.cs4048project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserList> userlist;
    public UserListAdapter(Context context, ArrayList<UserList> userlist) {
        this.context = context;
        this.userlist = userlist;
    }
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_list_row, parent, false);

        return new UserListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        UserList user = userlist.get(position);

        holder.username.setText(user.getUsername());

        // Load image using Glide
        Glide.with(context)
                .load(user.getPictureUrl()) // getPictureUrl() returns the image URL
                .placeholder(R.drawable.baseline_person_24) // Placeholder image while loading
                .error(R.drawable.baseline_person_24) // Image to show if loading fails
                .into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        TextView username;
        Button chatButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_picture_userlist);
            username = itemView.findViewById(R.id.username_userlist);
            chatButton = itemView.findViewById(R.id.button_chat);

        }
    }

}
