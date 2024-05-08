package com.example.cs4048project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.R;

public class CustomProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_profile);
        setTitle("Profile");

        HeaderFooterHelper.setupHeaderButtons(this);
    }
}