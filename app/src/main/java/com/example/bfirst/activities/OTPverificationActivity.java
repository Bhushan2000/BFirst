package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bfirst.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPverificationActivity extends AppCompatActivity {
    private TextView phoneNo;
    private EditText otp;
    private Button verifyBtn;
    private String userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_pverification);

        phoneNo = findViewById(R.id.phone_no);
        otp = findViewById(R.id.otp);
        verifyBtn = findViewById(R.id.verify_Btn);
        userNumber = getIntent().getStringExtra("mobileNo");
        phoneNo.setText("Verification Code Has Been Send To +91 " + userNumber);

        Random random = new Random();
        final int otp_no = random.nextInt(999999 - 111111) + 111111;
        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                verifyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (otp.getText().toString().equals(String.valueOf(otp_no))) {

                            Map<String, Object> updateStatus = new HashMap<>();
                            updateStatus.put("Order Status", "Ordered");
                            final String OrderID = getIntent().getStringExtra("OrderID");
                            FirebaseFirestore.getInstance().collection("ORDERS").document(OrderID).update(updateStatus)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String,Object> userOrder = new HashMap<>();
                                                userOrder.put("order_id",OrderID);

                                                userOrder.put("time", FieldValue.serverTimestamp());


                                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(OrderID).set(userOrder)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    DeliveryActivity.codOrderConfirmed = true;
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(OTPverificationActivity.this, "Failed to update user order List", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });



                                            } else {
                                                Toast.makeText(OTPverificationActivity.this, "ORDERED CANCELLED", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });






                        } else {
                            Toast.makeText(OTPverificationActivity.this, " incorrect  OTP ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(OTPverificationActivity.this, "Fail to send OTP verification code.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "cS47t6Ld9oERzKuWeQM8blsrNInUjHTOhiPfG015gqp3mAxCwVKYRMkuGEiScaoZCxQbXVWsqfLJl7dw");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", userNumber);
                body.put("message", "27748");
                body.put("variables", "{#BB#}");
                body.put("variables_values", String.valueOf(otp_no));

                return body;
            }
        };

        stringRequest.setRetryPolicy( new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(OTPverificationActivity.this);
        requestQueue.add(stringRequest);

    }
}
