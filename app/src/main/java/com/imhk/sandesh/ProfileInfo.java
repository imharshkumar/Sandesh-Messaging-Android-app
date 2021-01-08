package com.imhk.sandesh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileInfo extends AppCompatActivity {
    Button button;
    EditText nameText;
    ImageView profileImageView;

    String number;
    String uid;

    StorageReference mStorage;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;
    static final int GALLERY = 4;
    String downloadUrl="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        button=findViewById(R.id.button);
        nameText=findViewById(R.id.nameedittext);
        profileImageView=findViewById(R.id.profileimageview);
        mStorage= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        number=firebaseUser.getPhoneNumber();
        uid=firebaseUser.getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameText.getText().toString().trim();
                if(name.isEmpty()) {
                    nameText.setError("Enter your name");
                    return;
                }
                progressDialog.setMessage("Initialising..");
                progressDialog.show();
                myRef = FirebaseDatabase.getInstance().getReference("Users").child(number);

                HashMap<String,String> userMap=new HashMap<>();
                userMap.put("mobile",number);
                userMap.put("UID",uid);
                userMap.put("name",name);
                userMap.put("imageURL",downloadUrl);
                userMap.put("status","offline");

                myRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Intent intent=new Intent(ProfileInfo.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileInfo.this, "OPPS..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,GALLERY);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY && data.getData()!=null) {
            progressDialog.setMessage("UPLOADING...");
            Uri uri= data.getData();
            progressDialog.show();
            profileImageView.setImageURI(uri);

            final StorageReference profilePhoto = mStorage.child("profilePhoto"+number+uri.getLastPathSegment()+".jpg");

            profilePhoto.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return profilePhoto.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        downloadUrl=downloadUri.toString();
                        progressDialog.dismiss();
                        Toast.makeText(ProfileInfo.this, "Profile Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileInfo.this, "failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
