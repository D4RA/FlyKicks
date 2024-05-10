package com.example.cs4048project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages;
    private String currentUserId;

    private static final int VIEW_TYPE_CURRENT_USER = 1;
    private static final int VIEW_TYPE_OTHER_USER = 2;

    public MessageAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_CURRENT_USER;
        } else {
            return VIEW_TYPE_OTHER_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_CURRENT_USER) {
            View view = inflater.inflate(R.layout.message_sent, parent, false);
            return new CurrentUserMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.message_received, parent, false);
            return new OtherUserMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_CURRENT_USER) {
            // Bind message sent by current user
            ((CurrentUserMessageViewHolder) holder).bind(message);
        } else {
            // Bind message sent by other user
            ((OtherUserMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        if (messages == null || messages.isEmpty()) {
            return 0; // Return 0 if messages list is null or empty meaning the user has not clicked to Chat with anyone
        } else {
            return messages.size();
        }
    }

    public void setMessagesList(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    // ViewHolder for messages sent by current user
    private static class CurrentUserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        CurrentUserMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_body);
        }

        void bind(Message message) {
            messageTextView.setText(message.getMessageText());
        }
    }

    // ViewHolder for messages sent by other users
    private static class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        OtherUserMessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_body);
        }

        void bind(Message message) {
            messageTextView.setText(message.getMessageText());
        }
    }
}