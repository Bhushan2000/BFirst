package com.example.bfirst.adapters;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.models.CartItemModel;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.activities.DeliveryActivity;
import com.example.bfirst.activities.MainActivity;
import com.example.bfirst.activities.ProductsDetailsActivity;
import com.example.bfirst.R;
import com.example.bfirst.models.RewardModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteButton;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteButton) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteButton = showDeleteButton;
    }

    @Override
    public int getItemViewType(int position) {

        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;

            case 1:
                return CartItemModel.TOTAL_AMOUNT;

            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartItemView);


            case CartItemModel.TOTAL_AMOUNT:

                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new CartTotalAmountViewHolder(cartTotalView);

            default:
                return null;


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttingPrice = cartItemModelList.get(position).getCuttingPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                boolean inStock = cartItemModelList.get(position).isInStock();
                Long productQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIDs();
                long stockQty = cartItemModelList.get(position).getStockQuantity();
                boolean COD = cartItemModelList.get(position).isCOD();

                ((CartItemViewHolder) holder).setItemDetails(productID, resource, title, freeCoupons, productPrice, cuttingPrice, offersApplied, position, inStock, String.valueOf(productQuantity), maxQuantity, qtyError, qtyIds, stockQty,COD);

                break;


            case CartItemModel.TOTAL_AMOUNT:

                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

//
                for (int x = 0; x < cartItemModelList.size(); x++) {

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()) {

                        int quantity = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));

                        totalItems = totalItems + quantity;

                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {

                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice()) * quantity;
                        } else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()) * quantity;

                        }

                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getCuttingPrice())) {

                            savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getCuttingPrice()) - Integer.parseInt(cartItemModelList.get(x).getProductPrice())) * quantity;


                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {
                                savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                            } else {

                                if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())) {
                                    savedAmount = savedAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice()) - Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())) * quantity;
                                }
                            }

                        }

                    }
                }




                if (totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;

                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }

                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(savedAmount);


                ((CartTotalAmountViewHolder) holder).setAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);
                break;


            default:
                return;
        }

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;

        }


    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCouponIcon;

        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView cuttingPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private LinearLayout deleteBtn;
        private ConstraintLayout couponRedemptionLayout;
        private Button reedemBtn;
        private TextView couponReedemptioBody;
        private ImageView codIndicator;

        ///////////////coupon dialogue/////////
        private TextView couponTitle;
        private TextView couponBody;
        private TextView couponExpiryDate;
        private RecyclerView couponsrecyclerView;
        private LinearLayout selectedCoupon;

        private TextView discountedPrice;
        private TextView originalPrice;

        private Button removeCouponBtn;
        private Button applyCouponBtn;

        private LinearLayout applyOrRemoveBtnContainer;
        private TextView footerText;
        private String productOriginalPrice;
///////////////coupon dialogue/////////


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);


            productImage = itemView.findViewById(R.id.product_image);
            freeCouponIcon = itemView.findViewById(R.id.free_cupon_icon);



            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttingPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            offersApplied.setVisibility(View.GONE);
            couponsApplied = itemView.findViewById(R.id.coupons_apply);
            couponsApplied.setVisibility(View.GONE);
            productQuantity = itemView.findViewById(R.id.product_qunatity);
            deleteBtn = itemView.findViewById(R.id.remove_item_button);
            couponRedemptionLayout = itemView.findViewById(R.id.coupon_redemption_layout);
            reedemBtn = itemView.findViewById(R.id.tv_coupon_redemption_button);
            couponReedemptioBody = itemView.findViewById(R.id.tv_coupon_redemption);

            codIndicator = itemView.findViewById(R.id.cod_indicator);
            codIndicator.setVisibility(View.GONE);


        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setItemDetails(final String productID, String resource, String title, Long freeCouponsNo, final String productPriceText, String cuttingPriceText, Long offersAppliedNo, final int position, boolean inStock, final String quantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty,boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_small_unload_24dp)).into(productImage);


            productTitle.setText(title);


            /////////////////////in stock //////////////////////////////////////////////////
            final Dialog checkCouponPrice = new Dialog(itemView.getContext());
            checkCouponPrice.setContentView(R.layout.coupons_redemption_dialogue);
            checkCouponPrice.setCancelable(false);
            checkCouponPrice.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



            if (COD)
            {
                codIndicator.setVisibility(View.VISIBLE);
            }else{
                codIndicator.setVisibility(View.INVISIBLE);

            }
            if (inStock) {


                if (freeCouponsNo > 0) {
                    freeCouponIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        freeCoupons.setText("  free  " + freeCouponsNo + "  Coupon");

                    } else {
                        freeCoupons.setText("  free  " + freeCouponsNo + "  Coupons");

                    }
                } else {
                    freeCouponIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);

                }


                productPrice.setText("Rs." + productPriceText + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));



                cuttingPrice.setText("Rs." + cuttingPriceText + "/-");
                couponRedemptionLayout.setVisibility(View.VISIBLE);

                ///////////////coupon dialogue///////////


                ImageView toogleRecyclerView = checkCouponPrice.findViewById(R.id.toogle_recyclerview);
                couponsrecyclerView = checkCouponPrice.findViewById(R.id.coupons_recyclerView);
                couponsrecyclerView.setVisibility(View.GONE);
                selectedCoupon = checkCouponPrice.findViewById(R.id.selected_coupon);

                couponTitle = checkCouponPrice.findViewById(R.id.coupon_title);
                couponBody = checkCouponPrice.findViewById(R.id.coupon_body);

                couponExpiryDate = checkCouponPrice.findViewById(R.id.coupon_validity);


                originalPrice = checkCouponPrice.findViewById(R.id.original_price);
                discountedPrice = checkCouponPrice.findViewById(R.id.discounted_price);

                removeCouponBtn = checkCouponPrice.findViewById(R.id.remove_btn);
                applyCouponBtn = checkCouponPrice.findViewById(R.id.apply_btn);

                footerText = checkCouponPrice.findViewById(R.id.fotter_text);
                applyOrRemoveBtnContainer = checkCouponPrice.findViewById(R.id.apply_or_remove_buttons_container);

                footerText.setVisibility(View.GONE);
                applyOrRemoveBtnContainer.setVisibility(View.VISIBLE);


                LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                couponsrecyclerView.setLayoutManager(layoutManager);


                ///////// coupon dialogue adapter ////////////

                originalPrice.setText(productPrice.getText());
                productOriginalPrice = productPriceText;



                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position, DBQueries.rewardModelList, true, couponsrecyclerView, selectedCoupon,
                        productOriginalPrice, couponTitle, couponExpiryDate, couponBody, discountedPrice, cartItemModelList);
                couponsrecyclerView.setAdapter(myRewardsAdapter);
                myRewardsAdapter.notifyDataSetChanged();


                applyCouponBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {

                            for (RewardModel rewardModel : DBQueries.rewardModelList) {
                                if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                    rewardModel.setAllReadyUsed(true);


                                    couponRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.gradient_background));
                                    couponReedemptioBody.setText(rewardModel.getCouponBody());
                                    reedemBtn.setText("coupon");

                                }
                            }
                            couponsApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2));
                            productPrice.setText(discountedPrice.getText());

                            String offerDiscountedAmount = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountedPrice.getText().toString().substring(3, discountedPrice.getText().length() - 2)));

                            couponsApplied.setText("Coupons Applied - Rs. " + offerDiscountedAmount + "/-");
                            notifyItemChanged(cartItemModelList.size() - 1);
                            checkCouponPrice.dismiss();
                        }
                    }
                });

                removeCouponBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RewardModel rewardModel : DBQueries.rewardModelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAllReadyUsed(false);
                            }
                        }
                        couponTitle.setText("Coupon");
                        couponBody.setText("Tap the icon on top right corner to select your coupon.");
                        couponExpiryDate.setText("validity");
                        couponsApplied.setVisibility(View.INVISIBLE);
                        couponRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.couponRed));
                        couponReedemptioBody.setText("Apply your coupon Here.");
                        reedemBtn.setText("Redeem");




                        cartItemModelList.get(position).setSelectedCouponId("null");
                        productPrice.setText("Rs." + productPriceText + "/-");


                        notifyItemChanged(cartItemModelList.size() - 1);
                        checkCouponPrice.dismiss();


                    }
                });


                toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showDialogueRecyclerView();

                    }
                });


                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {

                    for (RewardModel rewardModel : DBQueries.rewardModelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {


                            couponRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.gradient_background));
                            couponReedemptioBody.setText(rewardModel.getCouponBody());
                            reedemBtn.setText("coupon");


                            couponBody.setText(rewardModel.getCouponBody());

                            if (rewardModel.getType().equals("Discount")) {
                                couponTitle.setText(rewardModel.getType());
                            } else {
                                couponTitle.setText("Flat Rs. " + rewardModel.getDisOrAmo() + "OFF");
                            }

                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            couponExpiryDate.setText(simpleDateFormat.format(rewardModel.getValidity()));


                        }
                    }

                    discountedPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    couponsApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rs." + cartItemModelList.get(position).getDiscountedPrice() + "/-");
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));

                    couponsApplied.setText("Coupons Applied - Rs." + offerDiscountedAmount + "/-");

                } else {

                    couponsApplied.setVisibility(View.INVISIBLE);
                    couponRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.couponRed));
                    couponReedemptioBody.setText("Apply your coupon Here.");
                    reedemBtn.setText("Redeem");

                }


                ////////////////coupon dialogue/////////////////////////////////////////////////////////////////////////////////////


                productQuantity.setText("Qty: " + quantity);

                if (!showDeleteButton) {


                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
//                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.red)));

                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
//                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));

                    }
                }


                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Dialog quantityDialogue = new Dialog(itemView.getContext());
                        quantityDialogue.setContentView(R.layout.quantity_dialogue);
                        quantityDialogue.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialogue.setCancelable(false);

                        final EditText quantityNo = quantityDialogue.findViewById(R.id.quantity_no);
                        Button cancelBtn = quantityDialogue.findViewById(R.id.cancel_btn);
                        Button okBtn = quantityDialogue.findViewById(R.id.ok_btn);

                        quantityNo.setHint("Max " + String.valueOf(maxQuantity));

                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialogue.dismiss();
                            }
                        });

                        okBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityNo.getText())) {


                                    if (Long.parseLong(quantityNo.getText().toString()) <= maxQuantity && Long.parseLong(quantityNo.getText().toString()) != 0) {

                                        if (itemView.getContext() instanceof MainActivity) {
                                            cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));

                                        } else {


                                            if (DeliveryActivity.fromCart) {
                                                cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                            }
                                        }
                                        productQuantity.setText("Qty: " + quantityNo.getText());
                                        notifyItemChanged(cartItemModelList.size() - 1);

                                        if (!showDeleteButton) {
                                            DeliveryActivity.loadingDialogue.show();
                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(false);
                                            ////////////////////////
                                            final int initialQty = Integer.parseInt(quantity);
                                            final int finalQty = Integer.parseInt(quantityNo.getText().toString());
                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


                                            if (finalQty > initialQty) {

                                                for (int y = 0; y < finalQty - initialQty; y++) {

                                                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());

                                                    final int finalY = y;

                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timeStamp)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.add(quantityDocumentName);
                                                                    if (finalY + 1 == finalQty - initialQty) {


                                                                        firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty)
                                                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {

                                                                                    List<String> serverQuantity = new ArrayList<>();

                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                        serverQuantity.add(queryDocumentSnapshot.getId());

                                                                                    }

                                                                                    long availableQty = 0;

                                                                                    for (String qtyId : qtyIds) {

                                                                                        if (!serverQuantity.contains(qtyId)) {


                                                                                            DeliveryActivity.cartItemModelList.get(position).setQtyError(true);
                                                                                            DeliveryActivity.cartItemModelList.get(position).setMaxQuantity(availableQty);
                                                                                            Toast.makeText(itemView.getContext(), "All products may not be available at required quantity", Toast.LENGTH_SHORT).show();


                                                                                        } else {
                                                                                            availableQty++;
                                                                                        }

                                                                                    }
                                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                                } else {

                                                                                    String error = task.getException().getMessage();
                                                                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();


                                                                                }
                                                                                DeliveryActivity.loadingDialogue.dismiss();
                                                                            }
                                                                        });


                                                                    }
                                                                }
                                                            });

                                                }

                                            } else if (initialQty > finalQty) {
                                                for (int x = 0; x < initialQty - finalQty; x++) {

                                                    final String qtyId = qtyIds.get(qtyIds.size() - 1 - x);
                                                    final int finalX = x;

                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.remove(qtyId);

                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                    if (finalX == initialQty - finalQty) {
                                                                        DeliveryActivity.loadingDialogue.dismiss();
                                                                    }

                                                                }
                                                            });

                                                }
                                            }

                                            ///////////////////////
                                        }


                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity : " + maxQuantity.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                quantityDialogue.dismiss();


                            }
                        });
                        quantityDialogue.show();

                    }
                });


                if (offersAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmount = String.valueOf(Long.valueOf(cuttingPriceText) - Long.valueOf(productPriceText));


                    offersApplied.setText("Offer Applied - Rs." + offerDiscountedAmount + "/-");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);

                }


            } else {

                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                cuttingPrice.setText(" ");
                couponRedemptionLayout.setVisibility(View.GONE);
                freeCoupons.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                offersApplied.setVisibility(View.GONE);
                freeCouponIcon.setVisibility(View.INVISIBLE);


            }


            if (showDeleteButton) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);

            }


            ///////////////////////////////////////////////////////////redeem button///////////////////////////////////////

            reedemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (RewardModel rewardModel : DBQueries.rewardModelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                            rewardModel.setAllReadyUsed(false);
                        }
                    }

                    checkCouponPrice.show();


                }
            });

            ///////////////////////////////////////////////////////////redeem button///////////////////////////////////////


            ///////////////////////////////////////////////////////////delete button///////////////////////////////////////

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (RewardModel rewardModel : DBQueries.rewardModelList) {
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                rewardModel.setAllReadyUsed(false);
                            }
                        }

                    }


                    if (!ProductsDetailsActivity.running_cart_query) {
                        ProductsDetailsActivity.running_cart_query = true;
                        DBQueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                    }
                }
            });

            ///////////////////////////////////////////////////////////delete button///////////////////////////////////////

        }

        private void showDialogueRecyclerView() {

            if (couponsrecyclerView.getVisibility() == View.GONE) {
                couponsrecyclerView.setVisibility(View.VISIBLE);
                selectedCoupon.setVisibility(View.GONE);
            } else {
                couponsrecyclerView.setVisibility(View.GONE);
                selectedCoupon.setVisibility(View.VISIBLE);
            }
        }

    }

    class CartTotalAmountViewHolder extends RecyclerView.ViewHolder {
        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;


        public CartTotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_item_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);

            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);


        }

        private void setAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Price(" + totalItemText + " items)");
            totalItemPrice.setText("Rs." + totalItemPriceText + "/-");

            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            } else {
                deliveryPrice.setText("Rs." + deliveryPriceText + "/-");
            }

            totalAmount.setText("Rs." + totalAmountText + "/-");
            cartTotalAmount.setText("Rs." + totalAmountText + "/-");
            savedAmount.setText("You saved Rs." + savedAmountText + "/-  on this order");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();

            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                    DeliveryActivity.cartItemModelList.remove(cartItemModelList.size() - 1);

                }

                if (showDeleteButton) {
                    cartItemModelList.remove(cartItemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }

        }
    }
}
