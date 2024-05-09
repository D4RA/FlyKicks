 package com.example.cs4048project.activity;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.RouteListingPreference;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Items;
import com.example.cs4048project.MyAdapter;
import com.example.cs4048project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import data.Users;

 public class ShopActivity extends AppCompatActivity {
     private RecyclerView recyclerView;
     private FirebaseFirestore db;
     private static final int REQUEST_IMAGE_PICK = 1;

     EditText itemNameEditText;
     EditText itemPriceEditText;
     ImageView uploadedPictureImageView;
     FirebaseAuth mAuth;
     private Uri selectedImageUri;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         setTitle("Shop");

         recyclerView = findViewById(R.id.recyclerview);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));

         Button postButton = findViewById(R.id.button_post);
         postButton.setOnClickListener(view -> {
             // Show the modal dialog to upload an item
             showUploadItemModal();
         });



         //recyclerView.setAdapter(userAdapter);

         db = FirebaseFirestore.getInstance();
         fetchUsersFromFirebase();
     }

     public void showUploadItemModal() {
         // Inflate the custom layout for the modal dialog
         View modalView = LayoutInflater.from(this).inflate(R.layout.upload_item_modal, null);

         // Find views inside the modal layout
         itemNameEditText = modalView.findViewById(R.id.edit_text_item_name);
         itemPriceEditText = modalView.findViewById(R.id.edit_text_item_price);
         Button uploadPictureButton = modalView.findViewById(R.id.button_upload_picture);
         Button save = modalView.findViewById(R.id.button_save);
         uploadedPictureImageView = modalView.findViewById(R.id.image_view_uploaded_picture);

         // Set click listener for the uploadButton
         uploadPictureButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Open an image picker intent
                 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                 intent.setType("image/*");
                 startActivityForResult(intent, REQUEST_IMAGE_PICK);
             }
         });


         save.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (selectedImageUri != null) {
                     // Upload the selected image to Firebase Storage
                     StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
                     storageRef.putFile(selectedImageUri)
                             .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                 @Override
                                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                     // Get the download URL of the uploaded image
                                     storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                         @Override
                                         public void onSuccess(Uri uri) {
                                             // Use the download URL (uri) of the image
                                             String imageUrl = uri.toString();
                                             upload();
                                         }
                                     });
                                 }
                             })
                             .addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     // Handle any errors that may occur during the upload process
                                     Log.e("Upload Image", "Upload failed: " + e.getMessage());
                                 }
                             });
                 } else {
                     // No image selected, show a message to the user
                     Toast.makeText(ShopActivity.this, "Please select an image to upload", Toast.LENGTH_SHORT).show();
                 }
             }
         });

         // Create an AlertDialog.Builder and set the custom layout
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setView(modalView);

         // Create and show the dialog
         AlertDialog dialog = builder.create();
         dialog.show();
     }



     private void fetchUsersFromFirebase() {
         db.collection("items").get()
                 .addOnSuccessListener(queryDocumentSnapshots -> {
                     List<Items> itemList = new ArrayList<>();
                     for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                         String itemId = documentSnapshot.getId();
                         String title = documentSnapshot.getString("name");
                         // Retrieve price as a number instead of string
                         Double price = documentSnapshot.getDouble("price");
                         String picUrl = documentSnapshot.getString("picUrl");
                         Items item = new Items(itemId, title, price, picUrl);
                         itemList.add(item);
                     }
                     MyAdapter myAdapter = new MyAdapter(getApplicationContext(), itemList);
                     recyclerView.setAdapter(myAdapter);
                 })
                 .addOnFailureListener(e -> Log.e("ShopActivity", "Error fetching items", e));
     }

     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
             // Get the URI of the selected image
             selectedImageUri = data.getData();
             // Set the selected image to the ImageView
             uploadedPictureImageView.setImageURI(selectedImageUri);
             // Show the ImageView
             uploadedPictureImageView.setVisibility(View.VISIBLE);
         }
     }



     public void upload(){
         String name = itemNameEditText.getText().toString().trim();
         int price = Integer.parseInt(itemPriceEditText.getText().toString().trim());

         mAuth = FirebaseAuth.getInstance();
         Log.e("auth", mAuth.getUid());
         // Check if the user is authenticated
         String currentUser = mAuth.getUid();




         // Create a Map to represent the user data
         Map<String, Object> itemData = new HashMap<>();
         itemData.put("name", name);
         itemData.put("price", price);
         itemData.put("downloadUrl", selectedImageUri);

         // Add the user data to Firestore database
         db.collection("items").document(currentUser)
                 .set(itemData)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Log.d("Firestore", "DocumentSnapshot added with ID: " + currentUser);
                         Toast.makeText(ShopActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.e("Firestore", "Error adding document", e);
                         Toast.makeText(ShopActivity.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
                     }
                 });


     }
 }