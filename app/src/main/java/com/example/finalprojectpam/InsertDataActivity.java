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

    private EditText etNamaTempat, etDeskripsiTempat, etLinkMaps;
    private ImageView btnBack;
    private Button btnChoose;
    private AppCompatButton btnUpload;
    private FirebaseAuth auth;
    private FirebaseUser current;
    private FirebaseDatabase db;
    private DatabaseReference bd;
    private PlaceModel place;
    private StorageReference store;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private Uri fileUri;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        etNamaTempat = findViewById(R.id.nama_tempat);
        etDeskripsiTempat = findViewById(R.id.deskripsi_tempat);
        etLinkMaps = findViewById(R.id.link_maps);
        btnBack = findViewById(R.id.back);
        btnChoose = findViewById(R.id.btn_choose);
        btnUpload = findViewById(R.id.btn_upload);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        bd = db.getReference(PlaceModel.class.getSimpleName());
        current = auth.getCurrentUser();
        store = FirebaseStorage.getInstance().getReference();

        btnChoose.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    fileUri = data.getData();
                    if (fileUri != null) {
                        uploadFile(fileUri);
                    } else {
                        Log.e("plus", "Failed to retrieve file URI");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(btnBack.getId() == view.getId()){
            Intent intent = new Intent(InsertDataActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (btnUpload.getId()==view.getId()){
            if(fileUri == null){
                Toast.makeText(this, "Upload Image First!", Toast.LENGTH_SHORT).show();
                return;
            }
            String namaTempat = etNamaTempat.getText().toString().trim();
            String deskripsiTempat = etDeskripsiTempat.getText().toString().trim();
            String linkMaps = etLinkMaps.getText().toString().trim();
            add(namaTempat, deskripsiTempat, linkMaps);
            finish();
        } else if(btnChoose.getId() == view.getId()){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        }
    }

    public boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(etNamaTempat.getText().toString())) {
            etNamaTempat.setError("Required");
            result = false;
        } else {
            etNamaTempat.setError(null);
        }
        if (TextUtils.isEmpty(etDeskripsiTempat.getText().toString())) {
            etDeskripsiTempat.setError("Required");
            result = false;
        } else {
            etDeskripsiTempat.setError(null);
        }
        if (TextUtils.isEmpty(etLinkMaps.getText().toString())) {
            etLinkMaps.setError("Required");
            result = false;
        } else {
            etLinkMaps.setError(null);
        }
        return result;
    }

    String key;

    private void add(String namaTempat , String deskripsiTempat, String link){
        current = auth.getCurrentUser();
        DatabaseReference dr =  bd.child(current.getUid()).push();
        key = dr.getKey();
        place = new PlaceModel(namaTempat, deskripsiTempat, link);
        if(!validateForm()) {
            return;
        }
        dr.setValue(place).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertDataActivity.this, "Data added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InsertDataActivity.this, "Fail to add", Toast.LENGTH_SHORT).show();
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
                        link = uri.toString();
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