package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bfirst.R;
import com.example.bfirst.repo.CancelledOrders;
import com.example.bfirst.repo.Categories;
import com.example.bfirst.repo.AppViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import static java.lang.Thread.sleep;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    public static Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        onAuthStateChanged(firebaseAuth);


    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            FirebaseFirestore.getInstance().collection("USERS").
                    document(firebaseUser.getUid()).
                    update("Last seen", FieldValue.serverTimestamp()).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else {
            startActivity(new Intent(this, Register.class));
            finish();

        }

    }


}
