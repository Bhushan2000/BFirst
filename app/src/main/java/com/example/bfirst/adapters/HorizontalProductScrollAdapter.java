package com.example.bfirst.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.models.HorizontalProductScrollModel;
import com.example.bfirst.activities.ProductsDetailsActivity;
import com.example.bfirst.R;

import java.util.List;


public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHHolder> {
    private List<HorizontalProductScrollModel>horizontalProductScrollModelList;


    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public ViewHHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);;



        return new ViewHHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHHolder holder, int position) {
        String resource  = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductDescription();
        String price = horizontalProductScrollModelList.get(position).getProductPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();
        holder.setData(productId,resource,title,description,price);
    }

    @Override
    public int getItemCount() {
        if(horizontalProductScrollModelList.size()>8){
             return 8;

        }else {
            return  horizontalProductScrollModelList.size();
         }

    }

    public class ViewHHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;



        public ViewHHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s__product_image);
            productTitle = itemView.findViewById(R.id.h_s__product_title);
            productDescription = itemView.findViewById(R.id.h_s__product_description);
            productPrice = itemView.findViewById(R.id.h_s__product_price);



        }

        private void setData(final String productId, String resource, String title, String description, String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.unload)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(description);
            productPrice.setText("Rs."+price+"/-");

            if(!title.equals("")){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(itemView.getContext(), ProductsDetailsActivity.class);
                        intent.putExtra("product_ID",productId);

                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }

    }
}
