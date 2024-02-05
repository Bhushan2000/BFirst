package com.example.bfirst.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private TextView existing_user;
    private FrameLayout parentFrameLayout;

    private EditText fullname, email, password, confirm_password;
    private TextInputLayout textInputLayoutFullName, textInputLayoutEmail, textInputLayoutPassword, textInputLayoutConfirmPassword;
    private Button signUp;
    private ImageButton closebutton;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseButton = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initViews(view);

        clicks();

        initFirebase();


        if (disableCloseButton) {

            closebutton.setVisibility(View.GONE);

        } else {

            closebutton.setVisibility(View.VISIBLE);
        }


        return view;
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void clicks() {
        closebutton.setOnClickListener(this::onClick);
        existing_user.setOnClickListener(this::onClick);
        signUp.setOnClickListener(this::onClick);
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

    private void initViews(View view) {
        existing_user = view.findViewById(R.id.existing_user_Sign_In);
        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);

        fullname = view.findViewById(R.id.sign_up_full_name);
        email = view.findViewById(R.id.sign_up_email);
        password = view.findViewById(R.id.sign_up_password);
        confirm_password = view.findViewById(R.id.sign_up_confirm_password);

        signUp = view.findViewById(R.id.sign_up);
        closebutton = view.findViewById(R.id.closeButton);

        textInputLayoutFullName = view.findViewById(R.id.textInputLayoutFullName);
        textInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = view.findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = view.findViewById(R.id.textInputLayoutConfirmPassword);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        fullname.addTextChangedListener(new TextWatcher() {
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
        confirm_password.addTextChangedListener(new TextWatcher() {
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


    }


    private void setFragment(FrameLayout frameLayout, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }

    private void checkInputs() {

        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(fullname.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirm_password.getText())) {

                        signUp.setEnabled(true);
                        signUp.setTextColor(Color.rgb(255, 255, 255));


                    } else {
                        signUp.setEnabled(false);
                        signUp.setTextColor(Color.argb(50, 255, 255, 255));
                    }

                } else {
                    signUp.setEnabled(false);
                    signUp.setTextColor(Color.argb(50, 255, 255, 255));
                }

            } else {
                signUp.setEnabled(false);
                signUp.setTextColor(Color.argb(50, 255, 255, 255));

            }

        } else {
            signUp.setEnabled(false);
            signUp.setTextColor(Color.argb(50, 255, 255, 255));

        }


    }

    private void checkEmail_Password() {

        Drawable customErrorIcon = getResources().getDrawable(R.drawable.ic_error_black_24dp);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirm_password.getText().toString())) {


                //set progress bar visible  here

                progressDialog = new ProgressDialog(getActivity());
                //show Progressbar
                progressDialog.show();

                //set content view
                progressDialog.setContentView(R.layout.progress_dialog);
                //set transparent background
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //

                signUp.setEnabled(false);

                signUp.setTextColor(Color.argb(50, 255, 255, 255));


                firebaseAuth.createUserWithEmailAndPassword(
                        email.getText().toString(),
                        password.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    final Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("fullname", fullname.getText().toString());
                                    userdata.put("email", email.getText().toString());
                                    userdata.put("profile", "");

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        CollectionReference userDataReference = firebaseFirestore.
                                                                collection("USERS").
                                                                document(firebaseAuth.getUid()).
                                                                collection("USER_DATA");

                                                        ////////////////////Maps////////////////////////

                                                        Map<String, Object> wishlistMap = new HashMap<>();
                                                        wishlistMap.put("list_size", (long) 0);

                                                        Map<String, Object> ratingsMap = new HashMap<>();
                                                        ratingsMap.put("list_size", (long) 0);

                                                        Map<String, Object> cartMap = new HashMap<>();
                                                        cartMap.put("list_size", (long) 0);

                                                        Map<String, Object> myAddressesMap = new HashMap<>();
                                                        myAddressesMap.put("list_size", (long) 0);

                                                        Map<String, Object> notificationsMap = new HashMap<>();
                                                        notificationsMap.put("list_size", (long) 0);

                                                        ////////////////////Maps////////////////////////


                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");
                                                        documentNames.add("MY_NOTIFICATIONS");

                                                        List<Map<String, Object>> documentsFields = new ArrayList<>();
                                                        documentsFields.add(wishlistMap);
                                                        documentsFields.add(ratingsMap);
                                                        documentsFields.add(cartMap);
                                                        documentsFields.add(myAddressesMap);
                                                        documentsFields.add(notificationsMap);


                                                        for (int x = 0; x < documentNames.size(); x++) {
                                                            final int finalX = x;
                                                            userDataReference.document(documentNames.get(x)).
                                                                    set(documentsFields.get(x)).
                                                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (finalX == documentNames.size() - 1) {
                                                                            goMainScreen();
                                                                        }

                                                                    } else {

                                                                        progressDialog.dismiss();
                                                                        signUp.setEnabled(true);
                                                                        signUp.setTextColor(Color.rgb(255, 255, 255));
                                                                        String error = task.getException().getMessage();
                                                                        Snackbar.make(signUp, error, Snackbar.LENGTH_LONG).show();
                                                                    }

                                                                }
                                                            });
                                                        }


                                                    } else {


                                                        String error = task.getException().getMessage();
                                                        Snackbar.make(signUp, error, Snackbar.LENGTH_LONG).show();

                                                    }

                                                }
                                            });


                                } else {
                                    //set progressbar invisible Here

                                    progressDialog.dismiss();
                                    signUp.setEnabled(true);

                                    signUp.setTextColor(Color.rgb(255, 255, 255));

                                    String error = task.getException().getMessage();
                                    Snackbar.make(signUp, error, Snackbar.LENGTH_LONG).show();
                                }


                            }
                        });


            } else {
                textInputLayoutConfirmPassword.setError("Password Doesn't Match");
                textInputLayoutConfirmPassword.requestFocus();
            }

        } else {
            textInputLayoutEmail.setError("Invalid Email");
            textInputLayoutEmail.requestFocus();
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_up:
                checkEmail_Password();
                break;

            case R.id.closeButton:
                goMainScreen();
                break;

            case R.id.existing_user_Sign_In:
                setFragment(parentFrameLayout, new SignInFragment());
                break;

        }
    }
}
