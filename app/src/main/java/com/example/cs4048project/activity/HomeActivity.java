package com.example.cs4048project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.cs4048project.HeaderFooterHelper;
import com.example.cs4048project.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

/*        setTitle("Home");

        HeaderFooterHelper.setupHeaderButtons(this);

 */

        CardView exit = findViewById(R.id.cardLogOut);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        CardView messaging = findViewById(R.id.cardMessaging);
        messaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MessagingActivity.class));
            }
        });

        CardView shop = findViewById(R.id.cardShopPage);

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ShopActivity.class));
            }
        });

        CardView profile = findViewById(R.id.cardLabTest);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CustomProfileActivity.class));
            }
        });

        CardView logout = findViewById(R.id.cardLogOut);

        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, AccountOptionsActivity.class));
            }
        });

    }
}