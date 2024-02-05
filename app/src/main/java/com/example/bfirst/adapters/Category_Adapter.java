package com.example.bfirst.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.activities.CategoryActivity;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.R;

import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    private List<CategoryModel> category_modelList;
    private int lastPosition = -1;

    public Category_Adapter(List<CategoryModel> category_modelList) {
        this.category_modelList = category_modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String icon = category_modelList.get(position).getCategory_icon_link();
        String name = category_modelList.get(position).getCategory_name();
        viewHolder.setCategory(name, position);
        viewHolder.setCategoryIcon(icon);

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;

        }

    }

    @Override
    public int getItemCount() {
        return category_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryIcon;
        private TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
        }

        private void setCategoryIcon(String iconURL) {

            //todo:set category icon
            if (!iconURL.equals("null")) {
                Glide.with(itemView.getContext()).load(iconURL).apply(new RequestOptions().placeholder(R.drawable.ic_small_unload_24dp)).into(categoryIcon);

            } else {
                categoryIcon.setImageResource(R.drawable.ic_small_unload_24dp);
            }


        }

        private void setCategory(final String name, final int position) {
            //todo:set category icon
            categoryName.setText(name);
            if (!name.equals("")) {

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {


                            Intent intent = new Intent(itemView.getContext(), CategoryActivity.class);
                            intent.putExtra("Category", name);

                            itemView.getContext().startActivity(intent);
                        }
                    }
                });


            }

        }
    }
}
