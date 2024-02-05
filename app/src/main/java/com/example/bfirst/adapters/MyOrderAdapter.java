package com.example.bfirst.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.Utils.Utils;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.activities.OrdersDetailActivity;
import com.example.bfirst.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderItemModel> myOrderItemModelList;


    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = myOrderItemModelList.get(position).getProductId();

        String resource = myOrderItemModelList.get(position).getProductImage();
        int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitle();

        String orderStatus = myOrderItemModelList.get(position).getOrderStatus();
        Date date;
        switch (orderStatus) {

            case "Ordered":
                date = myOrderItemModelList.get(position).getOrderedDate();
                break;
            case "Packed":
                date = myOrderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date = myOrderItemModelList.get(position).getShippedDate();
                break;
            case "Delivered":
                date = myOrderItemModelList.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = myOrderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = myOrderItemModelList.get(position).getCancelledDate();

        }


        holder.setData(resource, title, orderStatus, date, rating, productId, position);


    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private ImageView orderIndicator;

        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rate_Now_Container;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            productTitle = itemView.findViewById(R.id.product_title);
            deliveryStatus = itemView.findViewById(R.id.order_status_date);
            rate_Now_Container = itemView.findViewById(R.id.rate_now_container);


        }

        private void setData(String resource, String title, String orderStatus, Date date, final int rating, final String productId, final int position) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.unload)).into(productImage);

            productTitle.setText(title);

            if (orderStatus.equals("Cancelled")) {

                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));

            } else {

                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.green)));

            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY hh:mm aa");

            deliveryStatus.setText(orderStatus + " " + String.valueOf(simpleDateFormat.format(date)));

            setRating(rating);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent orderDetailsIntent = new Intent(itemView.getContext(), OrdersDetailActivity.class);
//                    orderDetailsIntent.putExtra("Position", position);
//                    itemView.getContext().startActivity(orderDetailsIntent);
//
//                }
//            });

            /////////////rating layout//////////


            rate_Now_Container = itemView.findViewById(R.id.rate_now_container);

            for (int x = 0; x < rate_Now_Container.getChildCount(); x++) {
                final int startPosition = x;
                rate_Now_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setRating(startPosition);

                        final DocumentReference documentReference = FirebaseFirestore.getInstance().
                                collection("PRODUCTS").document(productId);

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
                                if (DBQueries.myRatedIds.contains(productId)) {
                                    myRating.put("rating_" + DBQueries.myRatedIds.indexOf(productId), (long) startPosition + 1);


                                } else {

                                    myRating.put("list_size", (long) DBQueries.myRatedIds.size() + 1);
                                    myRating.put("product_ID_" + DBQueries.myRatedIds.size(), productId);
                                    myRating.put("rating_" + DBQueries.myRatedIds.size(), (long) startPosition + 1);
                                }

                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                        .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(itemView.getContext(), "Ratings updated in myOrder", Toast.LENGTH_LONG).show();

                                            DBQueries.myOrderItemModelList.get(position).setRating(startPosition);
                                            if (DBQueries.myRatedIds.contains(productId)) {
                                                DBQueries.myRatings.set(DBQueries.myRatedIds.indexOf(productId), Long.parseLong(String.valueOf(startPosition + 1)));

                                            } else {
                                                DBQueries.myRatedIds.add(productId);
                                                DBQueries.myRatings.add(Long.parseLong(String.valueOf(startPosition + 1)));

                                            }

                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                });

            }

            /////////////rating layout//////////


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

}
