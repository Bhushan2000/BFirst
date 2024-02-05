package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.models.MyOrderItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class OrdersDetailActivity extends AppCompatActivity {

    private int position;
    private TextView product_title;
    private TextView product_price;
    private TextView product_qty;
    private ImageView product_image;
    private ImageView orderIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private TextView orderDate,packedDate,shippedDate,deliveredDate;
    private TextView orderBody,packedBody,shippedBody,deliveredBody;
    private TextView orderTitle,packedTitle,shippedTitle,deliveredTitle;
    private ProgressBar O_P_progress, P_S_progress, S_D_progress;
    private LinearLayout rate_Now_Container;
    private int rating;
    private TextView fullName,address,pinCode;
    private TextView totalItems,totalItemPrice,deliveryPrice,totalAmount,savedAmount;
    private    Dialog loadingDialogue,cancelDialogue;
    private SimpleDateFormat  simpleDateFormat;
    private Button cancelOrderButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Orders details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ///////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(OrdersDetailActivity.this);
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


/////////////////////////////////////loading dialogue/////////////////////////////////////////////


        ///////////////////////////////////cancel dialogue/////////////////////////////////////////////

        cancelDialogue = new Dialog(OrdersDetailActivity.this);
        cancelDialogue.setContentView(R.layout.order_cancel_dialogue);
        cancelDialogue.setCancelable(true);
        cancelDialogue.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        //cancelDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


/////////////////////////////////////cancel dialogue/////////////////////////////////////////////


        product_title = findViewById(R.id.product_heading);
        product_price = findViewById(R.id.product_price);
        product_qty = findViewById(R.id.product_qty);
        product_image = findViewById(R.id.product_image);

        orderIndicator = findViewById(R.id.order_indicator);
        packedIndicator= findViewById(R.id.packed_indicator);
        shippedIndicator= findViewById(R.id.shipping_indicator);
        deliveredIndicator= findViewById(R.id.delivered_indicator);


        orderDate=findViewById(R.id.Ordered_date);
        packedDate=findViewById(R.id.packed_date);
        shippedDate=findViewById(R.id.shipping_date);
        deliveredDate=findViewById(R.id.delivered_date);

        orderBody = findViewById(R.id.ordered_body);
        packedBody = findViewById(R.id.packed_body);
        shippedBody = findViewById(R.id.shipping_body);
        deliveredBody = findViewById(R.id.delivered_body);

        orderTitle = findViewById(R.id.ordered_title);
        packedTitle= findViewById(R.id.packed_title);
        shippedTitle= findViewById(R.id.shipping_title);
        deliveredTitle= findViewById(R.id.delivered_title);


        O_P_progress = findViewById(R.id.ordered_packed_progress);

        P_S_progress = findViewById(R.id.packed_shipping_progress);
        S_D_progress = findViewById(R.id.shipping_delivered_progress);

        rate_Now_Container = findViewById(R.id.rate_now_container);

        fullName = findViewById(R.id.full_name);
        address = findViewById(R.id.address);
        pinCode = findViewById(R.id.address_pincode);

        totalItemPrice = findViewById(R.id.total_item_price);
        deliveryPrice = findViewById(R.id.delivery_price);
        totalAmount = findViewById(R.id.total_price);
        totalItems= findViewById(R.id.total_items);
        savedAmount= findViewById(R.id.saved_amount);

        cancelOrderButton= findViewById(R.id.cancel_button);
        cancelOrderButton.setVisibility(View.GONE);







        position = getIntent().getIntExtra("Position",-1);
        final MyOrderItemModel model = DBQueries.myOrderItemModelList.get(position);

        product_title.setText(model.getProductTitle());

        if (! model.getDiscountedPrice().equals("")){
            product_price.setText("Rs."+ model.getDiscountedPrice()+ "/-");
        }else{
            product_price.setText("Rs."+ model.getProductPrice() + "/-");
        }



        product_qty.setText("Qty: "+String.valueOf(model.getProductQuantity()));

        Glide.with(this).load(model.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.unload)).into(product_image);

        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");
        switch (model.getOrderStatus()){
            case "Ordered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));


                O_P_progress.setVisibility(View.GONE);
                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);




                break;

            case "Packed":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));


                O_P_progress.setProgress(100);
                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);

                shippedIndicator.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedBody.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredIndicator.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);


                break;

            case "Shipped":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));



                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setVisibility(View.GONE);



                deliveredIndicator.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);


                break;

            case "Out for Delivered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));




                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                deliveredTitle.setText("Out for Delivered");
                deliveredBody.setText("Your Order is Out for Delivery");


                break;

            case "Delivered":
                orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));




                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);







                break;

            case "Cancelled":

                if (model.getPackedDate().after(model.getOrderedDate())){
                    if (model.getShippedDate().after(model.getPackedDate())){

                        orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));

                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your Order has been cancelled!");





                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setProgress(100);




                    }else{

                        orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));

                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your Order has been cancelled!");



                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setVisibility(View.GONE);



                        deliveredIndicator.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredBody.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);


                    }
                }else{
                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderedDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));

                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your Order has been cancelled!");


                    O_P_progress.setProgress(100);
                    P_S_progress.setVisibility(View.GONE);
                    S_D_progress.setVisibility(View.GONE);

                    shippedIndicator.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedBody.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);

                    deliveredIndicator.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredBody.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);

                }
                break;


        }

        /////////////rating layout//////////
        rating = model.getRating();
        setRating(rating);

        for (int x = 0; x < rate_Now_Container.getChildCount(); x++) {
            final int startPosition = x;
            rate_Now_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                loadingDialogue.show();


                    setRating(startPosition);

                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductId());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);
                            if (rating != 0) {

                                Long increase = documentSnapshot.getLong(startPosition + 1 + "_star") + 1;
                                Long decrease = documentSnapshot.getLong(rating + 1 + "_star") - 1;
                                transaction.update(documentReference, startPosition + 1 + 1 + "_star", increase);
                                transaction.update(documentReference, rating + 1 + "_star", decrease);


                            } else {
                                Long increase = documentSnapshot.getLong(startPosition + 1 + "_star") + 1;
                                transaction.update(documentReference, startPosition + 1 + "_star", increase);

                            }

                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {

                            final Map<String, Object> myRating = new HashMap<>();
                            if (DBQueries.myRatedIds.contains(model.getProductId())) {
                                myRating.put("rating_" + DBQueries.myRatedIds.indexOf(model.getProductId()), (long) startPosition + 1);


                            } else {

                                myRating.put("list_size", (long) DBQueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBQueries.myRatedIds.size(), model.getProductId());
                                myRating.put("rating_" + DBQueries.myRatedIds.size(), (long) startPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(OrdersDetailActivity.this, "Ratings updated in myOrder", Toast.LENGTH_LONG).show();

                                        DBQueries.myOrderItemModelList.get(position).setRating(startPosition);
                                        if (DBQueries.myRatedIds.contains(model.getProductId())) {
                                            DBQueries.myRatings.set(DBQueries.myRatedIds.indexOf(model.getProductId()), Long.parseLong(String.valueOf(startPosition + 1)));

                                        } else {
                                            DBQueries.myRatedIds.add(model.getProductId());
                                            DBQueries.myRatings.add(Long.parseLong(String.valueOf(startPosition + 1)));

                                        }

                                    }else{
                                        String  error = task.getException().getMessage();
                                        Toast.makeText( OrdersDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialogue.dismiss();

                                }

                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          loadingDialogue.dismiss();
                        }
                    });


                }
            });

        }

        /////////////rating layout//////////

        if (model.isCancellationRequested()){
            cancelOrderButton.setVisibility(View.VISIBLE);

            cancelOrderButton.setEnabled(false);
            cancelOrderButton.setText("Cancellation in process.");
            cancelOrderButton.setTextColor(getResources().getColor(R.color.red));
            cancelOrderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));

        }else{
            if (model.getOrderStatus().equals("Ordered") || model.getOrderStatus().equals("Packed") ){
                cancelOrderButton.setVisibility(View.VISIBLE);
                cancelOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDialogue.findViewById(R.id.no_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialogue.dismiss();
                            }
                        });


                        cancelDialogue.findViewById(R.id.yes_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialogue.dismiss();
                                loadingDialogue.show();
                                Map<String,Object> map = new HashMap<>();
                                map.put("Ordered Id",model.getOrderID());
                                map.put("Product Id",model.getProductId());
                                map.put("Order cancelled",false);


                                FirebaseFirestore.getInstance().collection("CANCELLED_ORDERS").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrderID()).collection("OrderItems").document(model.getProductId()).update("Cancellation requested",true)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                model.setCancellationRequested(true);

                                                                cancelOrderButton.setEnabled(false);
                                                                cancelOrderButton.setText("Cancellation in process.");
                                                                cancelOrderButton.setTextColor(getResources().getColor(R.color.red));
                                                                cancelOrderButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));


                                                            }else{
                                                                Toast.makeText(OrdersDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                            loadingDialogue.dismiss();

                                                        }
                                                    });

                                        }else{
                                            loadingDialogue.dismiss();
                                            Toast.makeText(OrdersDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });



                            }
                        });


                        cancelDialogue.show();


                    }
                });
            }
        }







        fullName.setText(model.getFullName());
        address.setText(model.getAddress());
        pinCode.setText(model.getPinCode());

        totalItems.setText("Price("+model.getProductQuantity()+" items)");

        Long totalItemPriceValue;

        if (model.getDiscountedPrice().equals("")){

            totalItemPriceValue = model.getProductQuantity()*Long.valueOf(model.getProductPrice());
            totalItemPrice.setText("Rs."+ totalItemPriceValue +"/-");

        }else{
            totalItemPriceValue = model.getProductQuantity()*Long.valueOf(model.getDiscountedPrice());
        }

        // NullPointerException because in database there is no value get by the model & it return null
        if (model.getDeliveryPrice().equals("FREE")){

            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText(totalItemPrice.getText());
        }else{

            deliveryPrice.setText("Rs." + model.getDeliveryPrice() + "/-" );
            totalAmount.setText("Rs."+ (totalItemPriceValue + Long.valueOf(model.getDeliveryPrice()))+"/-");


        }





        if (! model.getCuttedPrice().equals("")){
            if (! model.getDiscountedPrice().equals("")){
                    savedAmount.setText("You Saved Rs." + model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice()))+ " on this order");
            }else{
                savedAmount.setText("You Saved Rs." + model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProductPrice()))+ " on this order");

            }

        }else{
            if (! model.getDiscountedPrice().equals("")){
                savedAmount.setText("You Saved Rs." + model.getProductQuantity()*(Long.valueOf(model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice()))+ " on this order");
            }else{
                savedAmount.setText("You Saved Rs. 0/-  on this order");

            }

        }



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setRating(int startPosition) {
        for (int x = 0; x < rate_Now_Container.getChildCount(); x++) {
            ImageView startButton = (ImageView) rate_Now_Container.getChildAt(x);
            startButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= startPosition) {
                startButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }


}
