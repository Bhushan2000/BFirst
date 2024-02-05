package com.example.bfirst.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfirst.models.NotificationModel;
import com.example.bfirst.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private List<NotificationModel> notificationModelList;

    public NotificationAdapter(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_layout,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        String image = notificationModelList.get(position).getImage();
        String body = notificationModelList.get(position).getBody();
        boolean readed  = notificationModelList.get(position).isReaded();
        holder.setData(image,body,readed);

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView12);
            imageView = itemView.findViewById(R.id.imageView);

        }
        private void setData(String image,String body,boolean readed){
            Glide.with(imageView.getContext()).load(image).into(imageView);

            if (readed){
                textView.setAlpha(0.5f);
            }else{
                textView.setAlpha(1f);
            }
            textView.setText(body);

        }
    }
}

