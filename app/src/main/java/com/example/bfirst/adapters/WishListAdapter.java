package com.example.bfirst.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.models.WishListModel;
import com.example.bfirst.activities.ProductsDetailsActivity;

import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    private boolean fromSearch;

    private List<WishListModel>wishListModelList;
    private boolean wishList;
    private int lastPosition=-1;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public List<WishListModel> getWishListModelList() {
        return wishListModelList;
    }

    public void setWishListModelList(List<WishListModel> wishListModelList) {
        this.wishListModelList = wishListModelList;
    }

    public WishListAdapter(List<WishListModel> wishListModelList, boolean wishList) {
        this.wishListModelList = wishListModelList;
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);


      return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String productID = wishListModelList.get(position).getProductID();
        String resource = wishListModelList.get(position).getProductImage();
        String title= wishListModelList.get(position).getProductTitle();
        long freeCoupon= wishListModelList.get(position).getFreeCoupons();
        String ratings= wishListModelList.get(position).getRating();
        Long totalRatings= wishListModelList.get(position).getTotalRatings();
        String productPrice= wishListModelList.get(position).getProductPrice();
        String cuttingPrice= wishListModelList.get(position).getCuttingPrice();
        boolean paymentMethod= wishListModelList.get(position).isCOD();
        boolean inStock= wishListModelList.get(position).isInStock();



        holder.setData(productID,resource,title,freeCoupon,ratings,totalRatings,productPrice,cuttingPrice,paymentMethod,position,inStock);



        if(lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition=position;

        }



    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupons;
        private ImageView couponIcon;

        private TextView rating;
        private TextView totalRatings;
        private View priceCut;
        private ImageButton deleteButton;
        private TextView productPrice;
        private TextView cuttingPrice;
        private TextView paymentMethod;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.free_coupon);
            couponIcon = itemView.findViewById(R.id.cupon_icon);
            rating = itemView.findViewById(R.id.tv_product_rating_miniView);
            totalRatings = itemView.findViewById(R.id.total_ratings);
            priceCut = itemView.findViewById(R.id.price_cut);
            deleteButton = itemView.findViewById(R.id.delete_button);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttingPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);


        }
        private void setData(final String productID, String resource, String title, long freeCouponsNo, String averageRating, long totalRatingsNo, String price, String cuttedPriceValue, boolean COD, final int index,boolean inStock){




            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.ic_small_unload_24dp)).into(productImage);



            productTitle.setText(title);
            if(freeCouponsNo!=0 && inStock){
                couponIcon.setVisibility(View.VISIBLE);
                if(freeCouponsNo==1){
                    freeCoupons.setText("free "+freeCouponsNo+" coupon");

                }else {
                    freeCoupons.setText("free "+freeCouponsNo+" coupon");

                }
            }else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);

            }

            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if(inStock){

                rating.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttingPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                rating.setText(averageRating);
                totalRatings.setText("("+totalRatingsNo+")ratings");
                productPrice.setText("Rs."+price+"/-");
                cuttingPrice.setText("Rs."+cuttedPriceValue+"/-");

                if(COD){
                    paymentMethod.setVisibility(View.VISIBLE);

                }else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else{
                linearLayout.setVisibility(View.INVISIBLE);

                paymentMethod.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("OUT OF STOCK");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                cuttingPrice.setVisibility(View.INVISIBLE);


            }


            if(wishList){
                deleteButton.setVisibility(View.VISIBLE);
            }else {
                deleteButton.setVisibility(View.GONE);

            }
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(! ProductsDetailsActivity.running_wishList_query){
//
//                        ProductsDetailsActivity.running_wishList_query=true;
//                        DBQueries.removeFromWishList(index,itemView.getContext());
//
//                    }
//
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromSearch){
                        ProductsDetailsActivity.fromSearch = true;

                    }
                    Intent intent = new Intent(itemView.getContext(),ProductsDetailsActivity.class);
                    intent.putExtra("product_ID",productID);

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
