package com.example.maigoje.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class Main4Activity extends AppCompatActivity {

    EditText ed;
    ImageView img;
    Button btn;
    ProgressBar pgb;
    TextView txtv;
    String pimurl;
    private static final int CHOOSE_IMAGE=101;

    Uri uriprofileimage;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        ed = (EditText) findViewById(R.id.details);
        img = (ImageView) findViewById(R.id.toppic);
        btn = (Button) findViewById(R.id.butt);
        txtv = (TextView) findViewById(R.id.verify);
        pgb = (ProgressBar) findViewById(R.id.pgbar);
        
        loadinfo();

        auth = FirebaseAuth.getInstance();

img.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
showimage();
    }
});

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
    saveinfo();
    }
});

    }

    private void loadinfo() {
        final FirebaseUser user = auth.getCurrentUser();

        if (user != null)
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(img);
        }
        if (user.getDisplayName() != null) {
            ed.setText(user.getDisplayName());
        }

        if (user.isEmailVerified()){
            txtv.setText("Email verified");
        }
        else {
            txtv.setText("click to verify");
            txtv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Main4Activity.this, "Verification email sent", Toast.LENGTH_LONG).show();
                        }
                    })
                }
            });
        }
        }


    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() == null){
            finish();;
            startActivity(new Intent(this, Main3Activity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData()!=null){
            uriprofileimage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriprofileimage);
                img.setImageBitmap(bitmap);
                
                uploadtostorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadtostorage() {
        StorageReference pfimage = FirebaseStorage.getInstance().getReference("profilepic/"+System.currentTimeMillis() + ".jpg");

        if(uriprofileimage != null){
            pgb.setVisibility(View.VISIBLE);
            pfimage.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pgb.setVisibility(View.GONE);
                    pimurl = taskSnapshot.getDownloadUrl().toString();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pgb.setVisibility(View.GONE);
                            Toast.makeText(Main4Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void saveinfo(){
        String display = ed.getText().toString();

        if(display.isEmpty()){
            ed.setError("User display name required");
            ed.requestFocus();
            return;
        }
        FirebaseUser user = auth.getCurrentUser();

        if(user!=null && pimurl!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(display)
                    .setPhotoUri(Uri.parse(pimurl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Main4Activity.this, "Profile updated", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void showimage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
}
