package com.example.cs4048project.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cs4048project.R;

public class IntroductionFragment extends AppCompatActivity {
    Button buttonStart;
        protected void onCreate(Bundle savedInstance) {
            super.onCreate(savedInstance);
            setContentView(R.layout.fragment_introduction);
            buttonStart = (Button) findViewById(R.id.buttonStart);
            buttonStart.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    startActivity(new Intent(IntroductionFragment.this, AccountOptionsFragment.class));
                }
            });
        }
}
