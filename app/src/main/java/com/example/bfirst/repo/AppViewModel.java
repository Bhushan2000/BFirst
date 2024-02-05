package com.example.bfirst.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bfirst.models.AddressModel;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.models.NotificationModel;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.models.WishListModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AppViewModel extends ViewModel {
    MutableLiveData<List<CategoryModel>> categoryModelMutableLiveData;
    // MutableLiveData<List<CancelledOrders>> cancelledOrdersMutableLiveData;
    MutableLiveData<List<MyOrderItemModel>> ordersMutableLiveData;
    MutableLiveData<List<RewardModel>> rewardsMutableLiveData;
    MutableLiveData<List<NotificationModel>> notificationsMutableLiveData;
    MutableLiveData<List<AddressModel>> addressesMutableLiveData;
    MutableLiveData<List<WishListModel>> wishListMutableLiveData;
    // MutableLiveData<WishListModel> removeFromWishListMutableLiveData;
    MutableLiveData<List<MyMallModel>> homePageMutableLiveData;



    FirebaseFirestore firebaseFirestore;

    AppRepository appRepository;


    public AppViewModel() {
        appRepository = new AppRepository();

        categoryModelMutableLiveData = appRepository.getCategories();
        // cancelledOrdersMutableLiveData = appRepository.getCancelledOrders();
        ordersMutableLiveData = appRepository.getOrders();
        rewardsMutableLiveData = appRepository.getRewards();
        notificationsMutableLiveData = appRepository.getNotifications();
        addressesMutableLiveData = appRepository.getAddress();
        wishListMutableLiveData = appRepository.getWishList();
       // removeFromWishListMutableLiveData = appRepository.removeFromWishList(0);


        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public LiveData<List<CategoryModel>> getCategoriesLiveData() {
        return categoryModelMutableLiveData;
    }

    //     public LiveData<List<CancelledOrders>> getCancelledOrdersLiveData(){
//        return cancelledOrdersMutableLiveData;
//
//    }
    public LiveData<List<MyOrderItemModel>> getOrdersLiveData() {
        return ordersMutableLiveData;
    }

    public LiveData<List<RewardModel>> getRewardsLiveData() {

        return rewardsMutableLiveData;

    }

    public LiveData<List<NotificationModel>> getNotificationsLiveData() {

        return notificationsMutableLiveData;

    }

    public LiveData<List<AddressModel>> getAddressesLiveData() {

        return addressesMutableLiveData;
    }

    public LiveData<List<WishListModel>> getWishListLiveData(){
        return wishListMutableLiveData;
    }
//    public LiveData<WishListModel> removeFromWishListLiveData(){
//        return removeFromWishListMutableLiveData;
//    }
}
