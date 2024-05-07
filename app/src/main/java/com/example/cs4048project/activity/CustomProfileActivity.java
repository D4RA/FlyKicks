package com.example.cs4048project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class CustomProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_profile);
        setTitle("Profile");

        editTextName = findViewById(R.id.edit_text_name);
        editTextUsername = findViewById(R.id.edit_text_username);
        Button buttonSaveProfile = findViewById(R.id.button_save_profile);
        db = FirebaseFirestore.getInstance();

        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(editTextUsername);
                saveProfile();
            }
        });

        HeaderFooterHelper.setupHeaderButtons(this);
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please enter name and username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user object
        User user = new User(name, username);



        // Add the user to Firestore database with a randomly generated document ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("User", "Name: " + user.getName() + ", Username: " + user.getUsername());
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(CustomProfileActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding document", e);
                        Toast.makeText(CustomProfileActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

class User {
    private String name;
    private String username;

    private String picUlr;


    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    // Getters and setters (optional)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}