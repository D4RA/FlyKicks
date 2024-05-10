package com.example.cs4048project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Message;
import com.example.cs4048project.MessageAdapter;
import com.example.cs4048project.R;
import com.example.cs4048project.UserList;
import com.example.cs4048project.UserListAdapter;
import com.example.cs4048project.UserListSetupListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MessagingActivity extends AppCompatActivity implements UserListAdapter.OnChatClickListener {

    ArrayList<UserList> userlist = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String recipientId;

    DatabaseReference messagesRef;

    ValueEventListener valueEventListener;
    private String recipientName;
    private List<Message> messages = new ArrayList<>();
    private List<Message> filteredMessages;
    private MessageAdapter messageAdapter;
    private final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setTitle("Messaging");
        HeaderFooterHelper.setupHeaderButtons(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_users);
        setupUserList(new UserListSetupListener() {
            @Override
            public void onUserListSetupComplete(ArrayList<UserList> userList) {
                UserListAdapter adapter = new UserListAdapter(MessagingActivity.this, userList, MessagingActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MessagingActivity.this));
            }
        });


        Button sendButton = findViewById(R.id.button_send);

        //Send button listener, so we can call sendMessage to send the message to our database.
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messageAdapter = new MessageAdapter(messages, this.currentUserId);
        RecyclerView recyclerViewMessages = findViewById(R.id.recycler_view_messages);
        recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onChatClick(String recipientId, String recipientName) {
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        System.out.println(recipientId + " -> " + recipientName);
        String chatRoomId = generateChatRoomId(recipientId, this.currentUserId);

        messagesRef = FirebaseDatabase.getInstance().getReference("chatRooms")
                .child(chatRoomId)
                .child("messages");

        if (valueEventListener != null) {
            messagesRef.removeEventListener(valueEventListener);
        }
        valueEventListener = messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear(); // Clear existing messages
                // Iterate through each message in the dataSnapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Deserialize message from the snapshot
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        messages.add(message); // Add message to the list
                    }
                }
                messages.sort(new Comparator<Message>() {
                    @Override
                    public int compare(Message message1, Message message2) {
                        // Compare timestamps (ascending order)
                        return Long.compare(message1.getTimestamp(), message2.getTimestamp());
                    }
                });
                messageAdapter.setMessagesList(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential error
                Log.e("MessagingActivity", "Error retrieving messages: " + databaseError.getMessage());
            }
        });
    }

    private String generateChatRoomId(String recipientId, String currentUserId) {
        if (recipientId.compareTo(currentUserId) < 0) {
            return recipientId + "_" + currentUserId;
        } else {
            return currentUserId + "_" + recipientId;
        }
    }


    private void sendMessage() {
        if (this.recipientId != null) {
            long timestamp = System.currentTimeMillis();
            EditText editText = findViewById(R.id.edit_text_message);
            String messageText = editText.getText().toString();
            Message message = new Message(this.currentUserId, this.recipientId, messageText, timestamp);
            messagesRef.push().setValue(message);
        }
    }

    void setupUserList(UserListSetupListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get every document in the users collection
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userlist.clear(); // Clear existing list
                        //For each document we are storing the Id by adding it to the array, username and pictureUrl so we can utilize these.
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getId();
                            String username = document.getString("username");
                            String pictureUrl = document.getString("downloadUrl");

                            UserList user = new UserList(username, pictureUrl, userId);
                            if (!userId.equals(this.currentUserId)) {
                                userlist.add(user);
                            }
                        }
                        listener.onUserListSetupComplete(userlist);
                    } else {
                        Log.d("Error getting documents: ", task.getException().toString());
                    }
                });

    }
}