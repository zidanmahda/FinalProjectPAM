package com.example.finalprojectpam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

public class SplashScreenActivity extends AppCompatActivity {

    AppCompatButton btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(view -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
            startActivity(intent);
        });
    }
}