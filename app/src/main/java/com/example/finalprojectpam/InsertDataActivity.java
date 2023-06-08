package com.example.finalprojectpam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finalprojectpam.model.PlaceModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class InsertDataActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name, desc;
    Button choose;
    ImageView back;
    AppCompatButton upload;
    FirebaseAuth auth;
    FirebaseUser current;
    FirebaseDatabase db;
    DatabaseReference bd;
    PlaceModel place;
    StorageReference store;
    ActivityResultLauncher<Intent> filePickerLauncher;
    Uri fileUri;
    String Link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);
        db = FirebaseDatabase.getInstance();
        bd = db.getReference(PlaceModel.class.getSimpleName());
        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nama_tempat);
        desc = findViewById(R.id.deskripsi_tempat);
        back = findViewById(R.id.back);
        upload = findViewById(R.id.btn_upload);
        choose = findViewById(R.id.btn_choose);
        upload.setOnClickListener(this);
        back.setOnClickListener(this);
        choose.setOnClickListener(this);
        current = auth.getCurrentUser();
        store = FirebaseStorage.getInstance().getReference();

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            fileUri = data.getData();
                            if (fileUri != null) {
                                uploadFile(fileUri);
                            } else {
                                Log.e("insert", "Failed to retrieve file URI");
                            }
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(back.getId()==view.getId()){
            Intent intent = new Intent(InsertDataActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (upload.getId()==view.getId()){
            if(fileUri == null){
                Toast.makeText(this, "Upload photo first", Toast.LENGTH_SHORT).show();
                return;
            }
            String nama = name.getText().toString().trim();
            String deskripsi = desc.getText().toString().trim();
            add(nama,deskripsi);

            finish();
        }else if(choose.getId()==view.getId()){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        }
    }
    public boolean cekform(){
        boolean result = true;
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Required");
            result = false;
        } else {
            name.setError(null);
        }
        if (TextUtils.isEmpty(desc.getText().toString())) {
            desc.setError("Required");
            result = false;
        } else {
            desc.setError(null);
        }
        return result;
    }
    String key;

    private void add(String nama , String desc){
        current = auth.getCurrentUser();
        DatabaseReference dr = bd.child(current.getUid()).push();
        key = dr.getKey();
        place = new PlaceModel(Link,nama,desc);
        if(!cekform()) {
            return;
        }
        dr.setValue(place).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertDataActivity.this, "Data added",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InsertDataActivity.this, "Fail to add",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void uploadFile(Uri fileUri) {
        File file = new File(fileUri.getPath());
        StorageReference fileRef = store.child("img/" +current.getUid()+"/"+ file.getName());
        // Upload file to Firebase Storage
        UploadTask uploadTask = fileRef.putFile(fileUri);
        // Register observers to listen for the upload progress or errors
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // File uploaded successfully
                Log.d("InsertDataActivity", "File uploaded successfully");
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link = uri.toString();
                        // Use the imageUrl as needed
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle unsuccessful uploads
                Log.e("InsertDataActivity", "Failed to upload file: " + e.getMessage());
            }
        });
    }
}