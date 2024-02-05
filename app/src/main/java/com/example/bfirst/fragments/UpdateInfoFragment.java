package com.example.bfirst.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    private CircleImageView circleImageView;
    private Button changePhotoButton, removePhotoButton, update;
    private EditText nameField, emailField;
    private Dialog loadingDialogue, passwordDialogue;
    private EditText password;
    private Button doneButton;
    private String name;
    private String email;
    private String photo;
    private Uri imageUri;
    private boolean updatePhoto = false;


    // TODO: Rename and change types and number of parameters
    public static UpdateInfoFragment newInstance(String param1, String param2) {
        UpdateInfoFragment fragment = new UpdateInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_update_info, container, false);
        ///////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(getContext());
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


/////////////////////////////////////loading dialogue/////////////////////////////////////////////

        ///////////////////////////////////password dialogue/////////////////////////////////////////////

        passwordDialogue = new Dialog(getContext());
        passwordDialogue.setContentView(R.layout.password_confirmation_dialogue);
        passwordDialogue.setCancelable(true);
        passwordDialogue.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        passwordDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        password = passwordDialogue.findViewById(R.id.password);
        doneButton = passwordDialogue.findViewById(R.id.done);

/////////////////////////////////////password dialogue/////////////////////////////////////////////
        circleImageView = view.findViewById(R.id.profile_image);
        changePhotoButton = view.findViewById(R.id.change_photo_btn);
        removePhotoButton = view.findViewById(R.id.remove_photo_btn);
        update = view.findViewById(R.id.update_button);
        nameField = view.findViewById(R.id.name);
        emailField = view.findViewById(R.id.email);

        name = getArguments().getString("Name");
        email = getArguments().getString("Email");
        photo = getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).apply(new RequestOptions().placeholder(R.drawable.unload)).into(circleImageView);
        emailField.setText(email);
        nameField.setText(name);

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);

                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }


                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }

            }
        });

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                updatePhoto = true;

                Glide.with(getContext()).load(R.drawable.ic_account_circle_black_24dp).into(circleImageView);

            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
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
        nameField.addTextChangedListener(new TextWatcher() {
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmail_Password();

            }
        });


        return view;

    }

    private void checkInputs() {

        if (!TextUtils.isEmpty(emailField.getText())) {

            if (!TextUtils.isEmpty(nameField.getText())) {


                update.setEnabled(true);
                update.setTextColor(Color.rgb(255, 255, 255));


            } else {
                update.setEnabled(false);
                update.setTextColor(Color.argb(50, 255, 255, 255));

            }

        } else {
            update.setEnabled(false);
            update.setTextColor(Color.argb(50, 255, 255, 255));

        }


    }

    private void checkEmail_Password() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

        Drawable customErrorIcon = getResources().getDrawable(R.drawable.ic_error_black_24dp);
        customErrorIcon.setBounds(0, 0, customErrorIcon.getIntrinsicWidth(), customErrorIcon.getIntrinsicHeight());

        if (emailField.getText().toString().matches(emailPattern)) {

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())) {

                loadingDialogue.show();

                photoUpdate(user);
                // same email
            } else {
                // update email

                passwordDialogue.show();
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialogue.show();

                        String userPassword = password.getText().toString();

                        passwordDialogue.dismiss();
                        AuthCredential credential = EmailAuthProvider.getCredential(email, userPassword);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> updateEmailInDatabase = new HashMap<>();
                                                updateEmailInDatabase.put("email", emailField.getText().toString());

                                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).update(updateEmailInDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(getContext(), "SuccessFully Updated", Toast.LENGTH_SHORT).show();

                                                            photoUpdate(user);


                                                        } else {
                                                            loadingDialogue.dismiss();
                                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                        loadingDialogue.dismiss();


                                                    }
                                                });

                                            } else {
                                                loadingDialogue.dismiss();
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                } else {
                                    loadingDialogue.dismiss();
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });
            }

            // update email & name


        } else {
            emailField.setError("Invalid Email", customErrorIcon);
            emailField.requestFocus();
        }

    }

    private void photoUpdate(final FirebaseUser user) {
        if (updatePhoto) {

            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + user.getUid() + ".jpg");


            if (imageUri != null) {


                Glide.with(getContext()).asBitmap().load(imageUri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);

                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {

                                                imageUri = task.getResult();
                                                DBQueries.profile = task.getResult().toString();
                                                Glide.with(getActivity()).load(DBQueries.profile).into(circleImageView);

                                                Map<String, Object> updateData = new HashMap<>();
                                                updateData.put("email", emailField.getText().toString());
                                                updateData.put("fullname", nameField.getText().toString());
                                                updateData.put("profile", DBQueries.profile);


                                                updateFields(user, updateData);


                                            } else {
                                                DBQueries.profile = "";
                                                loadingDialogue.dismiss();

                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                } else {
                                    loadingDialogue.dismiss();


                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        return;


                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        circleImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

                    }
                });

            } else {


                // remove photo

                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            DBQueries.profile = "";

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("email", emailField.getText().toString());
                            updateData.put("fullname", nameField.getText().toString());
                            updateData.put("profile", "");


                            updateFields(user, updateData);


                        } else {
                            loadingDialogue.dismiss();

                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("fullname", nameField.getText().toString());

            updateFields(user, updateData);
        }

        /// updating photo

    }

    private void updateFields(FirebaseUser user, final Map<String, Object> updateData) {
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            if (updateData.size() > 1) {
                                DBQueries.email = emailField.getText().toString().trim();
                                DBQueries.fullname = nameField.getText().toString().trim();


                            } else {
                                DBQueries.fullname = nameField.getText().toString().trim();

                            }
                            loadingDialogue.dismiss();
                            Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();

                            getActivity().finish();


                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        loadingDialogue.dismiss();

                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    imageUri = data.getData();
                    updatePhoto = true;

                    Glide.with(getContext()).load(imageUri).into(circleImageView);

                } else {
                    Toast.makeText(getContext(), "Image not found", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);

            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }


    }
}