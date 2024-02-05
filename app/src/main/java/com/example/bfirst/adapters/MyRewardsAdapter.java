package com.example.bfirst.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bfirst.R;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.models.CartItemModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {
    private List<RewardModel> rewardModelList;
    private boolean useMiniLayout = false;
    private RecyclerView couponsrecyclerView;
    private LinearLayout selectedCoupon;
    private String productOriginalPrice;

    private TextView selectedCouponTitle;
    private TextView selectedCouponExpiryDate;
    private TextView selectedCouponBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModelList;



    public MyRewardsAdapter(List<RewardModel> rewardModelList, boolean useMiniLayout ) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;

    }

    public MyRewardsAdapter(List<RewardModel> rewardModelList, boolean useMiniLayout, RecyclerView couponsrecyclerView, LinearLayout selectedCoupon, String productOriginalPrice , TextView couponTitle, TextView couponExpiryDate, TextView couponBody ,   TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsrecyclerView = couponsrecyclerView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCouponTitle = couponTitle;
        this.selectedCouponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;


    }

    public MyRewardsAdapter(int cartItemPosition,List<RewardModel> rewardModelList, boolean useMiniLayout, RecyclerView couponsrecyclerView, LinearLayout selectedCoupon, String productOriginalPrice , TextView couponTitle, TextView couponExpiryDate, TextView couponBody ,   TextView discountedPrice,List<CartItemModel> cartItemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.couponsrecyclerView = couponsrecyclerView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.selectedCouponTitle = couponTitle;
        this.selectedCouponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModelList = cartItemModelList;




    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);

        }


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String couponId = rewardModelList.get(position).getCouponId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getValidity();
        String body = rewardModelList.get(position).getCouponBody();
        String lower_limit = rewardModelList.get(position).getLower_limit();
        String upper_limit = rewardModelList.get(position).getUpper_limit();
        String disOrAmo = rewardModelList.get(position).getDisOrAmo();
        boolean alReadyUsed = rewardModelList.get(position).isAllReadyUsed();

        holder.setData(couponId,type, validity,body, lower_limit, upper_limit, disOrAmo,alReadyUsed);


    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView couponTitle;
        private TextView couponExpiryDate;
        private TextView couponBody;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.coupon_title);
            couponExpiryDate = itemView.findViewById(R.id.coupon_validity);
            couponBody = itemView.findViewById(R.id.coupon_body);


        }

        private void setData(final  String couponId,final String type, final Date validity, final String body, final String lowerLimit, final String upperLimit, final String disOrAmount, final boolean alReadyUsed) {

            if (type.equals("Discount")) {
                couponTitle.setText(type);
            } else {
                couponTitle.setText("Flat Rs. " + disOrAmount + "OFF");
            }


            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if(alReadyUsed){
                couponExpiryDate.setText( "Already used");
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                couponBody.setTextColor(Color.parseColor("#50ffffff"));
                couponTitle.setTextColor(Color.parseColor("#50ffffff"));

            }else{

                couponBody.setTextColor(Color.parseColor("#ffffff"));
                couponTitle.setTextColor(Color.parseColor("#ffffff"));
                couponExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.couponColor));
                couponExpiryDate.setText( simpleDateFormat.format(validity));
            }



            couponBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (! alReadyUsed){

                         selectedCouponTitle.setText(type);
                         selectedCouponExpiryDate.setText(simpleDateFormat.format(validity));
                         selectedCouponBody.setText(body);

                         if(Long.valueOf(productOriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productOriginalPrice) < Long.valueOf(upperLimit)){

                             if (type.equals("Discount")) {

                                 Long discountAmount = Long.valueOf(productOriginalPrice)*Long.valueOf(disOrAmount) / 100;
                                 discountedPrice.setText("Rs."+ String.valueOf(Long.valueOf(productOriginalPrice)- discountAmount) + "/-");

                             }else{
                                 discountedPrice.setText("Rs."+ String.valueOf(Long.valueOf(productOriginalPrice)- Long.valueOf(disOrAmount)) + "/-");

                             }

                            if (cartItemPosition != -1){

                                cartItemModelList.get(cartItemPosition).setSelectedCouponId(couponId);
                            }


                         }else{
                             if (cartItemPosition != -1) {
                                  cartItemModelList.get(cartItemPosition).setSelectedCouponId(null);
                             }

                             discountedPrice.setText("Invalid");
                             Toast.makeText(itemView.getContext(), "Sorry ! Product does not matches the coupon terms", Toast.LENGTH_SHORT).show();
                         }

                        if (couponsrecyclerView.getVisibility() == View.GONE) {
                            couponsrecyclerView.setVisibility(View.VISIBLE);
                            selectedCoupon.setVisibility(View.GONE);
                        } else {
                            couponsrecyclerView.setVisibility(View.GONE);
                            selectedCoupon.setVisibility(View.VISIBLE);
                        }

                        }
                    }
                });
            }
        }

    }
}
