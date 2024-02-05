package com.example.bfirst.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bfirst.R;
import com.example.bfirst.fragments.SignInFragment;
import com.example.bfirst.fragments.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.bfirst.activities.SplashActivity.thread;

public class Register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private long backPressedTime;
    private Toast backToast;

    private FrameLayout frameLayout;
    public static boolean setSignUpFragment = false;

    public static boolean onResetPasswordFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.register);

        firebaseAuth = FirebaseAuth.getInstance();

        frameLayout = findViewById(R.id.register_frame_layout);


        if (setSignUpFragment) {
            setSignUpFragment = false;
            setDefaultFragment(new SignUpFragment());
        } else {
            setDefaultFragment(new SignInFragment());

        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SignInFragment.disableCloseButton = false;
            SignUpFragment.disableCloseButton = false;
            if (onResetPasswordFragment) {
                onResetPasswordFragment = false;
                setFragment(new SignInFragment());
                return false;
            }


        }

        return super.onKeyDown(keyCode, event);


    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(frameLayout.getId(), fragment);


        fragmentTransaction.commit();

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }


//    @Override
//    protected void onStart() {
//
//        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
//        if(firebaseUser!=null)
//        {
//            startActivity(new Intent(this,MainActivity.class));
//        }
//        super.onStart();
//    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();
        thread.stop();


    }
}