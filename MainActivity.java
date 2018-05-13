package com.example.maigoje.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    Button b1;
    TextView t1;
    private ProgressBar progressbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        b1 = findViewById(R.id.butt);
        email = findViewById(R.id.mail);
        pass = findViewById(R.id.passw);
        t1 = findViewById(R.id.login);
        progressbar = (ProgressBar) findViewById(R.id.pb);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == b1) {
                    register();
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == t1) {
                    finish();
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    startActivity(intent);

                }
            }
        });

    }



    public void register() {
        //do something
        String enter = email.getText().toString().trim();
        String passing = pass.getText().toString().trim();

        if (enter.isEmpty()) {
            email.setError("Email required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(enter).matches()) {
            email.setError("please enter a valid email address");
            email.requestFocus();
            return;
        }

        if (passing.isEmpty()) {
            pass.setError("Password is required");
           pass.requestFocus();
            return;
        }
        if (passing.length() < 6) {
            pass.setError("minimum length is 6");
            pass.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(enter, passing).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "Authentication successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }
}
