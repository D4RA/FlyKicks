package com.example.cs4048project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class CustomProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private FirebaseFirestore db;

    private String pictureUrl;
    FirebaseAuth mAuth;
    private static final int REQUEST_IMAGE_PICK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_profile);
        setTitle("Profile");



        editTextName = findViewById(R.id.edit_text_name);
        editTextUsername = findViewById(R.id.edit_text_username);
        Button buttonSaveProfile = findViewById(R.id.button_save_profile);
        ImageView imageView = findViewById(R.id.image_profile_picture);
        db = FirebaseFirestore.getInstance();

        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(editTextUsername);
                mAuth = FirebaseAuth.getInstance();
                saveProfile();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the device's image picker
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        HeaderFooterHelper.setupHeaderButtons(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri imageUri = data.getData();

            // Set the selected image to the ImageView
            ImageView imageView = findViewById(R.id.image_profile_picture);
            imageView.setImageURI(imageUri);

            // TODO: Upload the selected image to server or cloud storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("pictures");

            mAuth = FirebaseAuth.getInstance();
            // Create a reference to the storage location
            StorageReference imageRef = storageRef.child(mAuth.getUid());
            // Upload the image
            UploadTask uploadTask = imageRef.putFile(imageUri);

            // Register observers to listen for upload success or failure
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        Glide.with(CustomProfileActivity.this)
                                .load(downloadUri)
                                .into(imageView);
                        pictureUrl = downloadUri.toString();
                    });
                    Log.d("Upload", "Image uploaded successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful uploads
                    Log.e("Upload", "Image upload failed: " + e.getMessage());
                }
            });

        }
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();
        Log.e("auth", mAuth.getUid());
        // Check if the user is authenticated
        String currentUser = mAuth.getUid();


        if (currentUser == null) {
            // Handle the case when no user is authenticated
            // For example, prompt the user to log in
            Toast.makeText(this, "Please log in to save your profile", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate input
        if (name.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please enter name and username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Map to represent the user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("username", username);
        userData.put("downloadUrl", pictureUrl);

        // Add the user data to Firestore database
        db.collection("users").document(currentUser)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + currentUser);
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