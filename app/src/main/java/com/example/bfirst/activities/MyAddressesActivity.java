package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.adapters.AddressAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.bfirst.activities.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {
    private RecyclerView myAddressRecyclerView;
    private static AddressAdapter adapter;
    private Button deliveredHereBtn;
    private LinearLayout addNewAddressBtn;
    private TextView addressesSaved;
    private int previousAddress;
    private int mode;
    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        setUpToolbar();
        initViews();


        previousAddress = DBQueries.selectedAddress;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myAddressRecyclerView.setLayoutManager(layoutManager);


        mode = getIntent().getIntExtra("MODE", -1);
        if (mode == SELECT_ADDRESS) {
            deliveredHereBtn.setVisibility(View.VISIBLE);

        } else {
            deliveredHereBtn.setVisibility(View.GONE);

        }

        deliveredHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBQueries.selectedAddress != previousAddress) {
                    final int previousAddressIndex = previousAddress;

                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_" + String.valueOf(previousAddress + 1), false);
                    updateSelection.put("selected_" + String.valueOf(DBQueries.selectedAddress + 1), true);

                    previousAddress = DBQueries.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                            .update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();

                            } else {
                                previousAddress = previousAddressIndex;
                                String error = task.getException().getMessage();
                                Toast.makeText(MyAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    finish();
                }
            }
        });

        adapter = new AddressAdapter(DBQueries.addressModelList, mode);
        myAddressRecyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) myAddressRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter.notifyDataSetChanged();


        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent addAddressIntent = new Intent(MyAddressesActivity.this, AddAddressActivity.class);
                if (mode != SELECT_ADDRESS) {
                    addAddressIntent.putExtra("INTENT", "manage");

                } else {

                    addAddressIntent.putExtra("INTENT", "null");
                }

                startActivity(addAddressIntent);
            }
        });

    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Addresses");

    }

    private void initViews() {
        myAddressRecyclerView = findViewById(R.id.address_recyclerView);
        deliveredHereBtn = findViewById(R.id.deliver_here_btn);

        addNewAddressBtn = findViewById(R.id.add_new_address_btn);
        addressesSaved = findViewById(R.id.address_saved);
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(String.valueOf(DBQueries.addressModelList.size()) + " saved addresses");

    }

    public static void refreshItems(int deselect, int select) {
        adapter.notifyItemChanged(deselect);
        adapter.notifyItemChanged(select);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mode == SELECT_ADDRESS) {

                if (DBQueries.selectedAddress != previousAddress) {
                    DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);
                    DBQueries.addressModelList.get(previousAddress).setSelected(true);
                    DBQueries.selectedAddress = previousAddress;

                }
            }

            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mode == SELECT_ADDRESS) {


            if (DBQueries.selectedAddress != previousAddress) {
                DBQueries.addressModelList.get(DBQueries.selectedAddress).setSelected(false);
                DBQueries.addressModelList.get(previousAddress).setSelected(true);
                DBQueries.selectedAddress = previousAddress;

            }
        }
        super.onBackPressed();
    }
}
