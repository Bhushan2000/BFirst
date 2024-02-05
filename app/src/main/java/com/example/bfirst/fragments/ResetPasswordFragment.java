package com.example.bfirst.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfirst.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    private EditText forgot_email;
    private Button reset_password;
    private  TextView go_back;

    private FrameLayout parentFrameLayout;
    private FirebaseAuth firebaseAuth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    private ViewGroup email_Icon_container;
    private ProgressBar progressBar;
    private ImageView emailIcon;
    private TextView emailIconTextGreen;
    private ImageView forgot_password_email_icon_green;
    private TextView forgot_password_email_icon_text_red;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
        forgot_email = view.findViewById(R.id.forgot_email);
        reset_password = view.findViewById(R.id.Reset_button);

        email_Icon_container = view.findViewById(R.id.forgot_password_email_icon_container);
        progressBar = view.findViewById(R.id.forgot_password_progressbar);



        go_back = view.findViewById(R.id.go_back);
        progressBar = view.findViewById(R.id.forgot_password_progressbar);
        emailIcon = view.findViewById(R.id.forgot_password_email_icon);
        emailIconTextGreen = view.findViewById(R.id.forgot_password_email_icon_text);
        forgot_password_email_icon_green= view.findViewById(R.id.forgot_password_email_icon_green);
        forgot_password_email_icon_text_red= view.findViewById(R.id.forgot_password_email_icon_text_red);


        parentFrameLayout = getActivity().findViewById(R.id.register_frame_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new SignInFragment());


            }
        });

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(email_Icon_container);
                emailIconTextGreen.setVisibility(View.GONE);






                TransitionManager.beginDelayedTransition(email_Icon_container);
                emailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

              reset_password.setEnabled(false);

              reset_password.setTextColor(Color.argb(50,255,255,255));






                firebaseAuth.sendPasswordResetEmail(forgot_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                       if(task.isSuccessful()){


                           ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0);
                            scaleAnimation.setDuration(100);
                            scaleAnimation.setInterpolator(new AccelerateInterpolator());
                            scaleAnimation.setRepeatMode(Animation.REVERSE);
                            scaleAnimation.setRepeatMode(1);
                            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            emailIcon.setAnimation(scaleAnimation);

                            emailIcon.setVisibility(View.GONE);

//                           emailIconText.setVisibility(View.GONE);
                           forgot_password_email_icon_green.setVisibility(View.VISIBLE);
                           emailIconTextGreen.setVisibility(View.VISIBLE);
                           forgot_password_email_icon_text_red.setVisibility(View.GONE);




                           Toast.makeText(getActivity(),  "Email Sent Successfully ! Check Your Email", Toast.LENGTH_LONG).show();

                       }else{
                           String error = task.getException().getMessage();



                           forgot_password_email_icon_text_red.setText(error);
                           forgot_password_email_icon_text_red.setTextColor(getResources().getColor(R.color.red));
                           TransitionManager.beginDelayedTransition(email_Icon_container);
                           forgot_password_email_icon_text_red.setVisibility(View.VISIBLE);
                           forgot_password_email_icon_green.setVisibility(View.GONE);



                       }

                        progressBar.setVisibility(View.GONE);
//                        emailIconTextGreen.setVisibility(View.GONE);




                        reset_password.setEnabled(true);
                        reset_password.setTextColor(Color.rgb(255,255,255));

                    }
                });

                // checkEmail();

            }
        });

        forgot_email.addTextChangedListener(new TextWatcher() {
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




   private void checkEmail() {
        if(forgot_email.getText().toString().matches(emailPattern)){
            //set progress bar visible  here

            progressBar.setVisibility(View.VISIBLE);

            reset_password.setEnabled(false);

            reset_password.setTextColor(Color.argb(50,255,255,255));

        }else {

            progressBar.setVisibility(View.INVISIBLE);
            emailIcon.setVisibility(View.INVISIBLE);
            reset_password.setEnabled(true);

            reset_password.setTextColor(Color.rgb(255,255,255));

            Toast.makeText(getActivity(),"Invalid email ",Toast.LENGTH_LONG).show();
        }


    }




   private void checkInputs() {

        if (!TextUtils.isEmpty(forgot_email.getText())) {



            reset_password.setEnabled(true);
            reset_password.setTextColor(Color.rgb(255,255,255));





        }else{
            reset_password.setEnabled(false);
            reset_password.setTextColor(Color.argb(50,255,255,255));
        }
    }




    private void setFragment(SignInFragment resetPasswordFragment) {

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(parentFrameLayout.getId(), resetPasswordFragment);
        fragmentTransaction.commit();

    }





}
