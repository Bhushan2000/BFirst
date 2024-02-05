package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.adapters.CartAdapter;
import com.example.bfirst.models.CartItemModel;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;
    private Dialog loadingDialogue;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;
    private LinearLayout cartTotalAndContinueLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Cart");


        /////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(this);
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(true);
        loadingDialogue.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

///////////////////////////////////loading dialogue/////////////////////////////////////////////


        cartItemsRecyclerView = findViewById(R.id.cart_recyclerview);
        continueBtn = findViewById(R.id.cart_continue_button);

        totalAmount = findViewById(R.id.total_cart_amount);

        cartTotalAndContinueLayout = findViewById(R.id.linearLayout6);
        cartTotalAndContinueLayout.setVisibility(View.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(DBQueries.cartItemModelList, totalAmount, true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;

                for (int x = 0; x < DBQueries.cartItemModelList.size(); x++) {
                    CartItemModel cartItemModel = DBQueries.cartItemModelList.get(x);
                    if (cartItemModel.isInStock()) {
                        DeliveryActivity.cartItemModelList.add(cartItemModel);

                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


//                loadingDialogue.show();
                if (DBQueries.addressModelList.size() == 0) {

                   // DBQueries.loadAddresses(NewActivity.this, loadingDialogue);
                    loadingDialogue.dismiss();
                } else {
                    loadingDialogue.dismiss();
                    Intent intent = new Intent(NewActivity.this, DeliveryActivity.class);
                    startActivity(intent);
                }

            }
        });


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