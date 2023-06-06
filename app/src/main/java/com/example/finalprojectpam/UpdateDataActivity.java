package com.example.finalprojectpam;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finalprojectpam.model.PlaceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDataActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNamaTempat, etDeskripsi, etLinkMaps;
    ImageView btnBack;
    AppCompatButton btnUpdate;
    DatabaseReference dr;
    FirebaseDatabase db;
    String key, nama, deskripsi, linkMaps;
    FirebaseAuth mAuth;
    FirebaseUser curret;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        etNamaTempat = findViewById(R.id.nama_tempat);
        etDeskripsi = findViewById(R.id.deskripsi_tempat);
        etLinkMaps = findViewById(R.id.link_maps);
        btnBack = findViewById(R.id.back);
        btnUpdate = findViewById(R.id.btn_upload);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        curret = mAuth.getCurrentUser();
        dr = db.getReference(PlaceModel.class.getSimpleName()).child(curret.getUid());

        key = getIntent().getStringExtra("key");
        nama = getIntent().getStringExtra("nama");
        deskripsi = getIntent().getStringExtra("deskripsi");
        linkMaps = getIntent().getStringExtra("link");

        etDeskripsi.setText(deskripsi);
        etNamaTempat.setText(nama);
        etLinkMaps.setText(linkMaps);
        btnBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(btnBack.getId()==view.getId()){
            finish();
        }else if(btnUpdate.getId()==view.getId()){
            PlaceModel up = new PlaceModel(etNamaTempat.getText().toString(),etDeskripsi.getText().toString(),etLinkMaps.getText().toString());
            dr.child(key).setValue(up);
            finish();
        }

    }
}