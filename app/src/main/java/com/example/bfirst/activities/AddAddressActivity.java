package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.bfirst.models.AddressModel;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button saveBtn;
    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pinCode;
    private EditText landmark;
    private EditText name;
    private EditText phoneNo;
    private EditText alternatePhoneNo;
    private Spinner stateSpinner;

    private String[] stateList;
    private String selectedState;
    private Dialog loadingDialogue;

    private boolean updateAddress = false;
    private AddressModel addressModel;
    private int position;
    private ArrayAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initViews();
        toolbarSetup();
        setUpAdapter();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getStringExtra("INTENT").equals("update_address")) {
            updateAddress = true;
            position = getIntent().getIntExtra("index", -1);
            addressModel = DBQueries.addressModelList.get(position);

            city.setText(addressModel.getCity());
            locality.setText(addressModel.getLocality());
            flatNo.setText(addressModel.getFlatNo());
            pinCode.setText(addressModel.getPinCode());
            landmark.setText(addressModel.getLandmark());
            name.setText(addressModel.getName());
            phoneNo.setText(addressModel.getPhoneNo());
            alternatePhoneNo.setText(addressModel.getAlternatePhoneNo());

            for (int i = 0; i < stateList.length; i++) {
                if (stateList[i].equals(addressModel.getState())) {

                    stateSpinner.setSelection(i);
                }


            }

            saveBtn.setText("Update");


        } else {
            position = DBQueries.addressModelList.size();
        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())) {

                    if (!TextUtils.isEmpty(locality.getText())) {

                        if (!TextUtils.isEmpty(flatNo.getText())) {

                            if (!TextUtils.isEmpty(pinCode.getText()) && pinCode.getText().length() == 6) {

                                if (!TextUtils.isEmpty(name.getText())) {

                                    if (!TextUtils.isEmpty(phoneNo.getText()) && phoneNo.getText().length() == 10) {


                                        loadingDialogue.show();


                                        Map<String, Object> addresses = new HashMap<>();
                                        addresses.put("city_" + String.valueOf(position + 1), city.getText().toString());
                                        addresses.put("locality_" + String.valueOf(position + 1), locality.getText().toString());
                                        addresses.put("flat_no_" + String.valueOf(position + 1), flatNo.getText().toString());
                                        addresses.put("pin_code_" + String.valueOf(position + 1), pinCode.getText().toString());
                                        addresses.put("landmark_" + String.valueOf(position + 1), landmark.getText().toString());
                                        addresses.put("name_" + String.valueOf(position + 1), name.getText().toString());
                                        addresses.put("mobile_no_" + String.valueOf(position + 1), phoneNo.getText().toString());
                                        addresses.put("alternate_mobile_no_" + String.valueOf(position + 1), alternatePhoneNo.getText().toString());
                                        addresses.put("state_" + String.valueOf(position + 1), selectedState);

                                        if (!updateAddress) {

                                            addresses.put("list_size", (long) DBQueries.addressModelList.size() + 1);
                                            if (getIntent().getStringExtra("INTENT").equals("manage")) {
                                                if (DBQueries.addressModelList.size() == 0) {

                                                    addresses.put("selected_" + String.valueOf(position + 1), true);
                                                } else {
                                                    addresses.put("selected_" + String.valueOf(position + 1), false);

                                                }
                                            } else {
                                                addresses.put("selected_" + String.valueOf(position + 1), true);

                                            }
                                            if (DBQueries.addressModelList.size() > 0) {
                                                addresses.put("selected_" + (DBQueries.selectedAddress + 1), false);
                                            }
                                        }


                                        FirebaseFirestore.getInstance().collection("USERS")
                                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                .document("MY_ADDRESSES")
                                                .update(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    if (!updateAddress) {

                                                        if (DBQueries.addressModelList.size() > 0) {

                                                            DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);

                                                        }

                                                        DBQueries.addressModelList.add(new AddressModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pinCode.getText().toString(), landmark.getText().toString(), name.getText().toString(), phoneNo.getText().toString(), alternatePhoneNo.getText().toString(), selectedState));
                                                        if (getIntent().getStringExtra("INTENT").equals("manage")) {
                                                            if (DBQueries.addressModelList.size() == 0) {

                                                                DBQueries.selectedAddress = DBQueries.addressModelList.size() - 1;
                                                            }
                                                        } else {
                                                            DBQueries.selectedAddress = DBQueries.addressModelList.size() - 1;

                                                        }

                                                    } else {
                                                        DBQueries.addressModelList.set(position, new AddressModel(true, city.getText().toString(), locality.getText().toString(), flatNo.getText().toString(), pinCode.getText().toString(), landmark.getText().toString(), name.getText().toString(), phoneNo.getText().toString(), alternatePhoneNo.getText().toString(), selectedState));

                                                    }


                                                    if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                        Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                        startActivity(deliveryIntent);
                                                    } else {
                                                        MyAddressesActivity.refreshItems(DBQueries.selectedAddress, DBQueries.addressModelList.size() - 1);
                                                    }

                                                    finish();

                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialogue.dismiss();
                                            }
                                        });


                                    } else {
                                        phoneNo.requestFocus();

                                    }
                                } else {
                                    name.requestFocus();

                                }

                            } else {
                                pinCode.requestFocus();
                                Toast.makeText(AddAddressActivity.this, "Please Provide Valid PinCode.....", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            flatNo.requestFocus();

                        }
                    } else {
                        locality.requestFocus();

                    }
                } else {
                    city.requestFocus();

                }


            }
        });


    }

    private void initViews() {
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pinCode = findViewById(R.id.pincode);
        stateSpinner = findViewById(R.id.stateSpinner);
        stateList = getResources().getStringArray(R.array.india_states);
        landmark = findViewById(R.id.landMark);
        name = findViewById(R.id.name);
        phoneNo = findViewById(R.id.phone_No);
        alternatePhoneNo = findViewById(R.id.alternate_phone_no);
        saveBtn = findViewById(R.id.saveBtn);
    }

    private void setUpAdapter() {

        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdapter);
    }


    private void toolbarSetup() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a New Address");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
