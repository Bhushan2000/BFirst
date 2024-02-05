package com.example.bfirst.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfirst.R;
import com.example.bfirst.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.bfirst.activities.Register.onResetPasswordFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private ProgressDialog progressDialog;
    private TextView forgot_password;

    private EditText email, password;
    private Button signIn;
    private ImageButton closebutton;

    private FirebaseAuth firebaseAuth;
    public static boolean disableCloseButton = false;

    private TextView Dont_have_an_accounnt;
    private FrameLayout parentFrameLayout;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Dont_have_an_accounnt = view.findViewById(R.id.dont_have_an_Account_SignUp);
        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);


        signIn = view.findViewById(R.id.sign_in);
        forgot_password = view.findViewById(R.id.forgot_password);

        email = view.findViewById(R.id.sign_in_email);
        password = view.findViewById(R.id.sign_in_password);

        closebutton = view.findViewById(R.id.closeButton);

        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseButton) {
            closebutton.setVisibility(View.GONE);
        } else {
            closebutton.setVisibility(View.VISIBLE);
        }


        return view;


    }

    private void goMainScreen() {
        if (disableCloseButton) {
            disableCloseButton = false;
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);

            progressDialog.dismiss();
            startActivity(intent);
        }

        getActivity().finish();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dont_have_an_accounnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new SignUpFragment());


            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());


            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmail_Password();

            }
        });


    }

    private void setFragment(ResetPasswordFragment resetPasswordFragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(parentFrameLayout.getId(), resetPasswordFragment);
        fragmentTransaction.commit();

    }


    private void setFragment(SignUpFragment signUpFragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(parentFrameLayout.getId(), signUpFragment);
        fragmentTransaction.commit();

    }

    private void checkInputs() {

        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {

                signIn.setEnabled(true);
                signIn.setTextColor(Color.rgb(255, 255, 255));


            } else {

                signIn.setEnabled(false);
                signIn.setTextColor(Color.argb(50, 255, 255, 255));

            }
        } else {

            signIn.setEnabled(false);
            signIn.setTextColor(Color.argb(50, 255, 255, 255));
        }


    }


    private void checkEmail_Password() {
        if (email.getText().toString().matches(emailPattern)) {

            if (password.length() >= 8) {

                //set progress bar visible  here

                progressDialog = new ProgressDialog(getActivity());
                //show Progressbar
                progressDialog.show();

                //set content view
                progressDialog.setContentView(R.layout.progress_dialog);
                //set transparent background
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //

                signIn.setEnabled(false);

                signIn.setTextColor(Color.argb(50, 255, 255, 255));


                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            goMainScreen();

                        } else {
                            progressDialog.dismiss();
                            signIn.setEnabled(true);

                            signIn.setTextColor(Color.rgb(255, 255, 255));


                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();


                        }

                    }
                });


            } else {


                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_LONG).show();

            }


        } else {

            Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_LONG).show();

        }


    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();
            signIn.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && passwordInput.length() >= 8);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}