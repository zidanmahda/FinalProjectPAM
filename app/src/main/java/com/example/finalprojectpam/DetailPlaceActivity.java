package com.example.finalprojectpam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailPlaceActivity extends AppCompatActivity {

    private ImageView ivBack, ivImage;
    private TextView tvName, tvDesc;
    private AppCompatButton btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_place);

        ivBack = findViewById(R.id.back);
        ivImage = findViewById(R.id.image);
        tvName = findViewById(R.id.name);
        tvDesc = findViewById(R.id.desc);
        btnMaps = findViewById(R.id.btn_maps);

        ivBack.setOnClickListener(view -> {
            Intent intent = new Intent(DetailPlaceActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}