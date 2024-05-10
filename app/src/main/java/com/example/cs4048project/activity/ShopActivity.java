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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.Items;
import com.example.cs4048project.MyAdapter;
import com.example.cs4048project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import data.Users;

public class ShopActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private String username;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    private static final int REQUEST_IMAGE_PICK = 1;

    EditText itemNameEditText;
    EditText itemPriceEditText;
    Spinner postCounty;
    String countyName;
    ImageView uploadedPictureImageView;
    private Uri selectedImageUri;
    private String picUrl;
    private String selectedItem = "All";
    ArrayList<String> locationArray = new ArrayList<>();
    ArrayList<String> modalLocationArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Shop");
        HeaderFooterHelper.setupHeaderButtons(this);
        populateCountryArray();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button postButton = findViewById(R.id.button_post);
        postButton.setOnClickListener(view -> {
            if (username != null) {
                showUploadItemModal();
            } else {
                Toast.makeText(getApplicationContext(), "Please create a profile first", Toast.LENGTH_SHORT).show();
            }
        });

        Button refresh = findViewById(R.id.button_refresh);
        refresh.setOnClickListener(view -> {
            fetchUsersFromFirebase();
        });

        //recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();
        fetchUserFromFirebase();
        fetchUsersFromFirebase();

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locationArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedItem = parentView.getItemAtPosition(position).toString();
                System.out.println(selectedItem);
                fetchUsersFromFirebase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void fetchUserFromFirebase() {
        if (currentUser != null) {
            // User is authenticated, proceed with Firestore document retrieval
            String userId = currentUser.getUid();

            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                username = document.getString("username");
                            } else {
                                Log.d("Firestore", "User document does not exist");
                            }
                        } else {
                            Exception exception = task.getException();
                            Log.e("Firestore", "Error getting user document", exception);
                        }
                    });
        } else {
            Log.d("Auth", "User is not authenticated");
        }
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
        postCounty = modalView.findViewById(R.id.spinner_county);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modalLocationArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postCounty.setAdapter(adapter2);
        postCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                countyName = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

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
                                            picUrl = uri.toString();
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
                        String picUrl = documentSnapshot.getString("downloadUrl");
                        String postUsername = documentSnapshot.getString("username");
                        String countyName = documentSnapshot.getString("county");
                        Items item = new Items(itemId, title, price, picUrl, postUsername, countyName);
                        itemList.add(item);
                    }
                    if (!selectedItem.equals("All")) {
                        List<Items> filteredList = new ArrayList<>();
                        for (Items item : itemList) {
                            if (item.getCounty().equals(selectedItem)) {
                                filteredList.add(item);
                            }
                        }
                        itemList = filteredList;
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


    public void upload() {
        String name = itemNameEditText.getText().toString().trim();
        int price = Integer.parseInt(itemPriceEditText.getText().toString().trim());

        Log.e("auth", currentUser.getUid());
        // Check if the user is authenticated
        String userUid = currentUser.getUid();


        // Create a Map to represent the user data
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("name", name);
        itemData.put("price", price);
        itemData.put("downloadUrl", picUrl);
        itemData.put("username", username);
        itemData.put("county", countyName);

        // Add the user data to Firestore database
        db.collection("items").document()
                .set(itemData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + currentUser);
                        Toast.makeText(ShopActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding document", e);
                        Toast.makeText(ShopActivity.this, "Failed to save post", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void populateCountryArray() {
        String[] countyArray = {
                "All", "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
                "Galway", "Kerry", "Kildare", "Kilkenny", "Laois", "Leitrim",
                "Limerick", "Longford", "Louth", "Mayo", "Meath", "Monaghan",
                "Offaly", "Roscommon", "Sligo", "Tipperary", "Waterford",
                "Westmeath", "Wexford", "Wicklow"
        };
        String[] countyArray2 = {
                "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
                "Galway", "Kerry", "Kildare", "Kilkenny", "Laois", "Leitrim",
                "Limerick", "Longford", "Louth", "Mayo", "Meath", "Monaghan",
                "Offaly", "Roscommon", "Sligo", "Tipperary", "Waterford",
                "Westmeath", "Wexford", "Wicklow"
        };
        locationArray = new ArrayList<>(Arrays.asList(countyArray));
        modalLocationArray = new ArrayList<>(Arrays.asList(countyArray2));

    }

}