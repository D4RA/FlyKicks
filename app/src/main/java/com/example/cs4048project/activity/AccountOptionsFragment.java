package com.example.cs4048project.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4048project.R;

public class AccountOptionsFragment extends AppCompatActivity {
    Button button_Reg;
    Button button_login;
            protected void onCreate(Bundle savedInstance) {
                super.onCreate(savedInstance);
                setContentView(R.layout.fragment_account_options);
            button_Reg = findViewById(R.id.buttonReg);
            button_login = findViewById((R.id.button4));
                button_Reg.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        startActivity(new Intent(AccountOptionsFragment.this, RegisterFragment.class));
                    }
                });
                button_login.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        startActivity(new Intent(AccountOptionsFragment.this, LoginFragment.class));
                    }
                });

            }
}
