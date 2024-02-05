package com.example.bfirst.repo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.bfirst.models.AddressModel;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.models.MyOrderItemModel;

import java.util.List;

import com.example.bfirst.R;
import com.example.bfirst.models.NotificationModel;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.models.WishListModel;

public class CheckActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    // MVVM
    AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // MVVM
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        getOrders();
        getRewards();
        getWishList();
        getCategories();
        getNotifications();
        getAddresses();

    }


    public void getAddresses() {
        appViewModel.getAddressesLiveData().observe(this, new Observer<List<AddressModel>>() {
            @Override
            public void onChanged(List<AddressModel> addressModels) {
                Log.d(TAG, "address:  ....." + addressModels.size());
                for (int i = 0; i < addressModels.size(); i++) {
                    System.out.println(addressModels.get(i));
                }
            }
        });
    }

    public void getWishList(){
        appViewModel.getWishListLiveData().observe(this, new Observer<List<WishListModel>>() {
            @Override
            public void onChanged(List<WishListModel> wishListModels) {
                Log.d(TAG, "wishlist:  ....." + wishListModels.size());
                for (int i = 0; i < wishListModels.size(); i++) {
                    System.out.println(wishListModels.get(i));
                }
            }
        });
    }

    private void getOrders() {
        appViewModel.getOrdersLiveData().observe(this, new Observer<List<MyOrderItemModel>>() {
            @Override
            public void onChanged(List<MyOrderItemModel> myOrderItemModels) {
                Log.d(TAG, "orders:  ....." + myOrderItemModels.size());
                for (int i = 0; i < myOrderItemModels.size(); i++) {
                    System.out.println(myOrderItemModels.get(i));
                }
            }
        });

    }

    private void getNotifications() {
        appViewModel.getNotificationsLiveData().observe(this, new Observer<List<NotificationModel>>() {
            @Override
            public void onChanged(List<NotificationModel> notificationModels) {
                Log.d(TAG, "notifications:  ....." + notificationModels.size());
                for (int i = 0; i < notificationModels.size(); i++) {
                    System.out.println(notificationModels.get(i));
                }
            }
        });

    }

    private void getRewards() {
        appViewModel.getRewardsLiveData().observe(this, new Observer<List<RewardModel>>() {
            @Override
            public void onChanged(List<RewardModel> rewardModels) {
                Log.d(TAG, "rewards:  ....." + rewardModels.size());
                for (int i = 0; i < rewardModels.size(); i++) {
                    System.out.println(rewardModels.get(i));
                }
            }
        });

    }

    private void getCategories() {
        appViewModel.getCategoriesLiveData().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categories) {

                Log.d(getLocalClassName(), "categories: ........... " + categories.size());

                for (int i = 0; i < categories.size(); i++) {
                    System.out.println(categories.get(i));
                }

            }
        });

    }

    private void getSlider(){

    }
}