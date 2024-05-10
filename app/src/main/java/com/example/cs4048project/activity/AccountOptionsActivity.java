package com.example.cs4048project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4048project.R;

public class AccountOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);

        Button buttonReg = findViewById(R.id.buttonReg);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment_container with RegisterFragment
                loadFragment(new RegisterFragment());
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment_container with LoginFragment
                loadFragment(new LoginFragment());
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // Create a FragmentTransaction to replace the fragment_container with the given fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: Add the transaction to the back stack
        transaction.commit();  // Commit the transaction
    }
}
