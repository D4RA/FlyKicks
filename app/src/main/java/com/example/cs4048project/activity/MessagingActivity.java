package com.example.cs4048project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Message;
import com.example.cs4048project.R;
import com.example.cs4048project.UserList;
import com.example.cs4048project.UserListAdapter;
import com.example.cs4048project.UserListSetupListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity implements UserListAdapter.OnChatClickListener {

    ArrayList<UserList> userlist = new ArrayList<>();

    DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setTitle("Messaging");
        HeaderFooterHelper.setupHeaderButtons(this);
        // setupUserList();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_users);
        setupUserList(new UserListSetupListener() {
            @Override
            public void onUserListSetupComplete(ArrayList<UserList> userList) {
                UserListAdapter adapter = new UserListAdapter(MessagingActivity.this, userList, MessagingActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MessagingActivity.this));
            }
        });
    }

    @Override
    public void onChatClick(String recipientId, String recipientName) {
       System.out.println(recipientId + " -> " + recipientName);
    }


    private void sendMessage() {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String recipientId = "LrvqL3eHVudaBH8MbqSfFF2Q7MH2";
        String messageText = "Hello!";
        long timestamp = System.currentTimeMillis();

        Message message = new Message(senderId, recipientId, messageText, timestamp);
        messagesRef.push().setValue(message);

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
                            userlist.add(user);
                        }
                        listener.onUserListSetupComplete(userlist);
                    } else {
                        Log.d("Error getting documents: ", task.getException().toString());
                    }
                });

    }
}