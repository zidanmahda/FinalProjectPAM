package com.example.finalprojectpam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectpam.adapter.PlaceAdapter;
import com.example.finalprojectpam.model.PlaceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase data;
    private DatabaseReference db;
    List<PlaceModel> itemList;
    PlaceAdapter adapter;
    private ImageView btnLogout, btnAdd;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();
        db = data.getReference(PlaceModel.class.getSimpleName());

        btnLogout = findViewById(R.id.logout);
        btnAdd = findViewById(R.id.add);

        RecyclerView re = findViewById(R.id.recycle);

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, InsertDataActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(view -> {
            logOut();
        });

        load();
        re.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter(isi);
        re.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    List<PlaceModel> isi = new ArrayList<>();

    public void load(){
        currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        String path = PlaceModel.class.getSimpleName()+"/"+uid;
        if(data.getReference(path)==null){
            data.getReference().child(uid).setValue("");
        }
        db =data.getReference(path);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isi.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    PlaceModel place = snap.getValue(PlaceModel.class);
                    place.setKey(snap.getKey());
                    isi.add(place);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Failed to load data",
                        Toast.LENGTH_SHORT ).show();
            }
        });

    }
}