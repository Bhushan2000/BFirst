package com.example.bfirst.repo;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.bfirst.Utils.Constants;
import com.example.bfirst.Utils.Utils;
import com.example.bfirst.activities.AddAddressActivity;
import com.example.bfirst.activities.DeliveryActivity;
import com.example.bfirst.activities.NotificationActivity;
import com.example.bfirst.models.AddressModel;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.models.NotificationModel;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.models.SliderModel;
import com.example.bfirst.models.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRepository {

    // mutableLiveData
    private final MutableLiveData<List<CategoryModel>> categoryModelMutableLiveData;
    // private final MutableLiveData<List<CancelledOrders>> cancelledOrdersMutableLiveData;
    private final MutableLiveData<List<MyOrderItemModel>> ordersMutableLiveData;
    private final MutableLiveData<List<RewardModel>> rewardMutableLiveData;
    private final MutableLiveData<List<NotificationModel>> notificationsMutableLiveData;
    private final MutableLiveData<List<AddressModel>> addressesMutableLiveData;
    private final MutableLiveData<List<WishListModel>> wishListMutableLiveData;
    // private final MutableLiveData<WishListModel> removeFromWishListMutableLiveData;
    private final MutableLiveData<List<MyMallModel>> homePageMutableLiveData;


    // list
    private List<String> wishList = new ArrayList<>();

    // repository
    private AppRepository appRepository;

    // TAG
    private String TAG = getClass().getSimpleName();

    // firebase
    private final FirebaseFirestore firebaseFirestore;


//    We can get nested document by using below line
//    DocumentReference categoriesRef = firebaseFirestore.document( "CollectionName/DocumentName");


    public AppRepository() {
        // mutableLiveData
        categoryModelMutableLiveData = new MutableLiveData<>();
        // cancelledOrdersMutableLiveData = new MutableLiveData<>();
        ordersMutableLiveData = new MutableLiveData<>();
        rewardMutableLiveData = new MutableLiveData<>();
        notificationsMutableLiveData = new MutableLiveData<>();
        addressesMutableLiveData = new MutableLiveData<>();
        wishListMutableLiveData = new MutableLiveData<>();
        // removeFromWishListMutableLiveData = new MutableLiveData<WishListModel>();
        homePageMutableLiveData = new MutableLiveData<>();


        // firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public MutableLiveData<List<WishListModel>> getWishList() {
        firebaseFirestore.collection(Utils.USERS).
                document(FirebaseAuth.getInstance().getUid()).
                collection(Utils.USER_DATA).
                document(Utils.MY_WISHLIST).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    for (long x = 0; x < (long) task.getResult().get(Utils.LIST_SIZE); x++) {
                        wishList.add(task.getResult().get(Utils.PRODUCT_ID_ + x).toString());
                        final String productID = task.getResult().get(Utils.PRODUCT_ID_ + x).toString();
                        firebaseFirestore.collection(Utils.PRODUCTS).
                                document(productID).
                                get().
                                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            final DocumentSnapshot documentSnapshot = task.getResult();

                                            FirebaseFirestore.getInstance().collection(Utils.PRODUCTS).
                                                    document(productID).
                                                    collection(Utils.QUANTITY).
                                                    orderBy(Utils.TIME, Query.Direction.ASCENDING).
                                                    get().
                                                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {

                                                                List<WishListModel> wishListModelList = new ArrayList<>();

                                                                long image_list_size = (long) documentSnapshot.get(Utils.NO_OF_PRODUCTS_IMAGES);

//                                                                for (long i = 1; i <= image_list_size; i++) {


                                                                if (task.getResult().getDocuments().size() < (long) documentSnapshot.get(Utils.STOCK_QUANTITY)) {


                                                                    wishListModelList.add(new WishListModel(productID
                                                                            , documentSnapshot.get(Utils.PRODUCT_IMAGE_ + 1).toString()
                                                                            , documentSnapshot.get(Utils.PRODUCT_TITLE).toString()
                                                                            , (long) documentSnapshot.get(Utils.FREE_COUPONS)
                                                                            , documentSnapshot.get(Utils.AVERAGE_RATING).toString()
                                                                            , (long) documentSnapshot.get(Utils.TOTAL_RATINGS)
                                                                            , documentSnapshot.get(Utils.PRODUCT_PRICE).toString()
                                                                            , documentSnapshot.get(Utils.CUTTED_PRICE).toString()
                                                                            , (boolean) documentSnapshot.get(Utils.COD)
                                                                            , true));


                                                                } else {


                                                                    wishListModelList.add(new WishListModel(productID
                                                                            , documentSnapshot.get(Utils.PRODUCT_IMAGE_ + 1).toString()
                                                                            , documentSnapshot.get(Utils.PRODUCT_TITLE).toString()
                                                                            , (long) documentSnapshot.get(Utils.FREE_COUPONS)
                                                                            , documentSnapshot.get(Utils.AVERAGE_RATING).toString()
                                                                            , (long) documentSnapshot.get(Utils.TOTAL_RATINGS)
                                                                            , documentSnapshot.get(Utils.PRODUCT_PRICE).toString()
                                                                            , documentSnapshot.get(Utils.CUTTED_PRICE).toString()
                                                                            , (boolean) documentSnapshot.get(Utils.COD)
                                                                            , false));

                                                                }
//                                                                }

                                                                wishListMutableLiveData.postValue(wishListModelList);
                                                            } else {
                                                                wishListMutableLiveData.postValue(null);
                                                            }
                                                        }
                                                    });
                                        } else {
                                            wishListMutableLiveData.postValue(null);
                                        }
                                    }
                                });
                    }

                } else {
                    wishListMutableLiveData.postValue(null);
                }

            }
        });

        return wishListMutableLiveData;
    }

//    public MutableLiveData<WishListModel> removeFromWishList() {
//
//
//        firebaseFirestore.collection(Utils.USERS).
//                document(FirebaseAuth.getInstance().getUid()).
//                collection(Utils.USER_DATA).
//                document(Utils.MY_WISHLIST).
//                set(updateWishList).
//                addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: " + "Removed form wish list");
//                        } else {
//                            removeFromWishListMutableLiveData.postValue(null);
//                        }
//                    }
//                });
//        return removeFromWishListMutableLiveData;
//    }


    public MutableLiveData<List<AddressModel>> getAddress() {
        firebaseFirestore.collection(Utils.USERS).
                document(FirebaseAuth.getInstance().
                        getUid()).collection(Utils.USER_DATA).
                document(Utils.MY_ADDRESSES).
                get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            List<AddressModel> addressModelList = new ArrayList<>();


                            if ((long) task.getResult().get(Utils.LIST_SIZE) == 0) {

                                Log.d(TAG, "onComplete: ..... address 0");

                            } else {

                                for (long x = 1; x < (long) task.getResult().get(Utils.LIST_SIZE) + 1; x++) {

                                    addressModelList.add(new AddressModel(task.getResult().getBoolean(Utils.SELECTED_ + x)
                                            , task.getResult().getString(Utils.CITY_ + x)
                                            , task.getResult().getString(Utils.LOCALITY_ + x)
                                            , task.getResult().getString(Utils.FLAT_NO_ + x)
                                            , task.getResult().getString(Utils.PIN_CODE_ + x)
                                            , task.getResult().getString(Utils.LANDMARK_ + x)
                                            , task.getResult().getString(Utils.NAME_ + x)
                                            , task.getResult().getString(Utils.MOBILE_NO_ + x)
                                            , task.getResult().getString(Utils.ALTERNATE_MOBILE_NO_ + x)
                                            , task.getResult().getString(Utils.STATE_ + x)


                                    ));

                                }

                                addressesMutableLiveData.postValue(addressModelList);

                            }

                        } else {
                            addressesMutableLiveData.postValue(null);
                        }
                    }
                });
        return addressesMutableLiveData;
    }


    public MutableLiveData<List<CategoryModel>> getCategories() {
        firebaseFirestore.collection(Utils.CATEGORIES).
                orderBy(Utils.INDEX).
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<CategoryModel> categoryList = new ArrayList<>();
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                categoryList.add(new CategoryModel(
                                        doc.get("icon").toString(),
                                        doc.getString("categoryName").toString()
                                        // ,(long) doc.get("index")
                                ));

                            }
                            categoryModelMutableLiveData.postValue(categoryList);

                        } else {
                            Log.d(getClass().getSimpleName(), "onComplete: " + task.getException());

                        }
                    }
                });


        return categoryModelMutableLiveData;
    }

//    public MutableLiveData<List<CancelledOrders>> getCancelledOrders() {
//        firebaseFirestore.collection(Utils.CANCELLEDORDERS).
//                document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
//                get().
//                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        List<CancelledOrders> cancelledOrdersList = new ArrayList<>();
//                        if (task.isSuccessful()) {
//
//                            DocumentSnapshot doc = task.getResult();
//
//                            cancelledOrdersList.add(new CancelledOrders(
//                                    doc.getBoolean("Order cancelled"),
//                                    doc.getString("Ordered Id").toString(),
//                                    doc.getString("Product Id")
//                            ));
//
//                            Log.d(TAG, "onComplete: " + task.getResult());
//
//
//                            cancelledOrdersMutableLiveData.postValue(cancelledOrdersList);
//
//                        } else {
//
//                            Log.d(TAG, "onComplete: " + task.getException().getLocalizedMessage());
//                        }
//                    }
//                });
//
//        return cancelledOrdersMutableLiveData;
//    }


    public MutableLiveData<List<RewardModel>> getRewards() {

        firebaseFirestore.collection(Utils.USERS).
                document(FirebaseAuth.getInstance().getUid()).
                get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        final Date lastSeenDate = task.getResult().getDate(Utils.LAST_SEEN);

                        firebaseFirestore.collection(Utils.USERS).
                                document(FirebaseAuth.getInstance().getUid()).
                                collection(Utils.USER_REWARDS).
                                get().
                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<RewardModel> rewardModelList = new ArrayList<>();
                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                if (documentSnapshot.get(Utils.TYPE).toString().equals(Utils.DISCOUNT) &&
                                                        lastSeenDate.before(documentSnapshot.getDate(Utils.VALIDITY))) {

                                                    rewardModelList.add(new RewardModel(
                                                            documentSnapshot.getId()
                                                            , documentSnapshot.get(Utils.TYPE).toString()
                                                            , documentSnapshot.get(Utils.LOWER_LIMIT).toString()
                                                            , documentSnapshot.get(Utils.UPPER_LIMIT).toString()
                                                            , documentSnapshot.get(Utils.PERCENTAGE).toString()
                                                            , documentSnapshot.get(Utils.BODY).toString()
                                                            , documentSnapshot.getDate(Utils.VALIDITY)
                                                            , (boolean) documentSnapshot.get(Utils.ALREADY_USED)


                                                    ));

                                                } else if (documentSnapshot.get(Utils.TYPE).toString().equals(Utils.FLAT_RS_OFF) &&
                                                        lastSeenDate.before(documentSnapshot.getDate(Utils.VALIDITY))) {

                                                    rewardModelList.add(new RewardModel(documentSnapshot.getId()
                                                            , documentSnapshot.get(Utils.TYPE).toString()
                                                            , documentSnapshot.get(Utils.LOWER_LIMIT).toString()
                                                            , documentSnapshot.get(Utils.UPPER_LIMIT).toString()
                                                            , documentSnapshot.get(Utils.AMOUNT).toString()
                                                            , documentSnapshot.get(Utils.BODY).toString()
                                                            , documentSnapshot.getDate(Utils.VALIDITY)
                                                            , (boolean) documentSnapshot.get(Utils.ALREADY_USED)

                                                    ));

                                                }

                                            }

                                            rewardMutableLiveData.postValue(rewardModelList);

                                        } else {
                                            rewardMutableLiveData.postValue(null);
                                        }
                                    }
                                });


                    }
                });
        return rewardMutableLiveData;
    }

    public MutableLiveData<List<MyOrderItemModel>> getOrders() {
        firebaseFirestore.collection(Utils.USERS).
                document(FirebaseAuth.getInstance().getUid()).
                collection(Utils.USER_ORDERS).
                orderBy(Utils.TIME, Query.Direction.DESCENDING).
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                firebaseFirestore.collection(Utils.ORDERS).
                                        document(documentSnapshot.getString(Utils.ORDER_ID_SMALL)).
                                        collection(Utils.ORDERITEMS).
                                        get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {

                                                        MyOrderItemModel myOrderItemModel = new MyOrderItemModel(
                                                                orderItems.getString(Utils.PRODUCT_ID)
                                                                , orderItems.getString(Utils.PRODUCT_TITLE_)
                                                                , orderItems.getString(Utils.PRODUCT_IMAGE)
                                                                , orderItems.getString(Utils.ORDERED_STATUS)

                                                                , orderItems.getString(Utils.ADDRESS)
                                                                , orderItems.getString(Utils.COUPON_ID)
                                                                , orderItems.getString(Utils.CUTTED_PRICE_SPACE_SEPRATED)


                                                                , orderItems.getDate(Utils.ORDERED_DATE)
                                                                , orderItems.getDate(Utils.PACKED_DATE)
                                                                , orderItems.getDate(Utils.SHIPPED_DATE)
                                                                , orderItems.getDate(Utils.DELIVERED_DATE)
                                                                , orderItems.getDate(Utils.CANCELLATION_DATE)


                                                                , orderItems.getString(Utils.DISCOUNTED_PRICE)
                                                                , orderItems.getLong(Utils.FREE_COUPONS_SPACE_SEPRATED)
                                                                , orderItems.getString(Utils.FULLNAME)
                                                                , orderItems.getString(Utils.ORDER_ID_CAPITAL)
                                                                , orderItems.getString(Utils.PAYMENT_METHOD)
                                                                , orderItems.getString(Utils.PINCODE)
                                                                , orderItems.getString(Utils.PRODUCT_PRICE_SPACE_SEPRATED)
                                                                , orderItems.getLong(Utils.PRODUCT_QUANTIT)
                                                                , orderItems.getString(Utils.USER_ID)

                                                                , orderItems.getString(Utils.DELIVERY_PRICE)
                                                                , orderItems.getBoolean(Utils.CANCELLATION_REQUESTED)

                                                        );


                                                        myOrderItemModelList.add(myOrderItemModel);

                                                    }

                                                    ordersMutableLiveData.postValue(myOrderItemModelList);


                                                } else {
                                                    Log.d(TAG, "onComplete: " + task.getException().getLocalizedMessage());
                                                }

                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "onComplete: " + task.getException().getLocalizedMessage());
                        }
                    }
                });
        return ordersMutableLiveData;
    }

    public MutableLiveData<List<NotificationModel>> getNotifications() {

        firebaseFirestore.collection(Utils.USERS).
                document(FirebaseAuth.getInstance().getUid()).
                collection(Utils.USER_DATA).
                document(Utils.MY_NOTIFICATIONS).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                        if (snapshot != null && snapshot.exists()) {

                            List<NotificationModel> notificationsList = new ArrayList<>();

                            int unread = 0;

                            for (long x = 0; x < (long) snapshot.get(Utils.LIST_SIZE); x++) {

                                notificationsList.add(0, new NotificationModel(
                                        snapshot.get(Utils.BODY_ + x).toString(),
                                        snapshot.get(Utils.IMAGE_ + x).toString(),
                                        snapshot.getBoolean(Utils.READED_ + x)));


                            }

                            notificationsMutableLiveData.postValue(notificationsList);

                        } else {
                            notificationsMutableLiveData.postValue(null);
                        }

                    }
                });
        return notificationsMutableLiveData;
    }


    public MutableLiveData<List<MyMallModel>> getHomePageData(String categoryName) {
        firebaseFirestore.collection(Utils.CATEGORIES).
                document(categoryName.toUpperCase()).
                collection(Utils.TOP_DEALS).
                orderBy(Utils.INDEX).
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                                if ((long) queryDocumentSnapshot.get(Utils.VIEW_TYPE) == Constants.BANNER_SLIDER) {

                                    List<SliderModel> sliderModelList = new ArrayList<>();


                                }
                            }

                        } else {
                            homePageMutableLiveData.postValue(null);
                        }
                    }
                });

        return homePageMutableLiveData;
    }
}
