package com.example.finalprojectpam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mFier;
    FirebaseDatabase data;
    DatabaseReference db;
    List<PlaceModel> itemList ;
    PlaceAdapter adapt;
    ImageView insert, logout;
    FirebaseUser currentU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFier = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();
        db = data.getReference(PlaceModel.class.getSimpleName());
        logout = findViewById(R.id.logout);
        insert = findViewById(R.id.add);
        RecyclerView re = findViewById(R.id.recycle);
        load();
        re.setLayoutManager(new LinearLayoutManager(this));
        adapt = new PlaceAdapter(isi);
        re.setAdapter(adapt);

        logout.setOnClickListener(this);
        insert.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentU = mFier.getCurrentUser();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==logout.getId()){
            mFier.signOut();
            Intent lo = new Intent(MainActivity.this, LoginPageActivity.class);
            lo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(lo);
        }else if (view.getId()== insert.getId()){
            Intent lo = new Intent(MainActivity.this, InsertDataActivity.class);
            startActivity(lo);
        }
    }
    List<PlaceModel> isi = new ArrayList<>();
    public void load(){
        currentU = mFier.getCurrentUser();
        String uid = currentU.getUid();
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
                adapt.setItemList(isi);
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Failed to load data",
                        Toast.LENGTH_SHORT ).show();
            }
        });
    }
}