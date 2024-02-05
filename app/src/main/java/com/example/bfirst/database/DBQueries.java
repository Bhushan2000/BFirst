package com.example.bfirst.database;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bfirst.R;
import com.example.bfirst.activities.AddAddressActivity;
import com.example.bfirst.activities.DeliveryActivity;
import com.example.bfirst.activities.NotificationActivity;
import com.example.bfirst.activities.ProductsDetailsActivity;
import com.example.bfirst.adapters.Category_Adapter;
import com.example.bfirst.adapters.MyMallAdapter;
import com.example.bfirst.adapters.MyOrderAdapter;
import com.example.bfirst.fragments.MyCartFragment;
import com.example.bfirst.fragments.MyMallFragment;
import com.example.bfirst.fragments.MyOrdersFragment;
import com.example.bfirst.fragments.MyRewardsFragment;
import com.example.bfirst.fragments.MyWishListFragment;
import com.example.bfirst.models.AddressModel;
import com.example.bfirst.models.CartItemModel;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.models.HorizontalProductScrollModel;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.models.MyOrderItemModel;

import com.example.bfirst.models.NotificationModel;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.models.SliderModel;
import com.example.bfirst.models.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bfirst.activities.ProductsDetailsActivity.productID;

public class DBQueries {

    public static final String TAG = "DBQueries";
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> category_modelList = new ArrayList<>();

    public static List<List<MyMallModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesName = new ArrayList<>();
    public static List<String> wishList = new ArrayList<>();
    public static List<WishListModel> wishListModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRatings = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();
    public static List<AddressModel> addressModelList = new ArrayList<>();
    public static int selectedAddress = -1;
    public static List<RewardModel> rewardModelList = new ArrayList<>();
    public static List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();
    public static ListenerRegistration registration;

    public static String email, profile, fullname;


    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        category_modelList.clear();

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                category_modelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));


                            }
                            Category_Adapter category_adapter = new Category_Adapter(category_modelList);
                            categoryRecyclerView.setAdapter(category_adapter);
                            category_adapter.notifyDataSetChanged();

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(final RecyclerView myMallRecyclerView, final Context context, final int index, String categoryName) {


        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

//
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_" + x).toString()
                                                , documentSnapshot.get("banner_" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new MyMallModel(0, sliderModelList));
//
                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new MyMallModel(1, documentSnapshot.get("strip_ad_banner").toString()
                                            , documentSnapshot.get("background").toString()));

                                } else if ((long) documentSnapshot.get("view_type") == 2) {
                                    List<WishListModel> viewAllProductList = new ArrayList<>();

                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();


                                    ArrayList<String> productsIds = (ArrayList<String>) documentSnapshot.get("products");


                                    for (String productsId : productsIds) {

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(productsId
                                                , ""
                                                , ""
                                                , ""
                                                , ""));


                                        viewAllProductList.add(new WishListModel(productsId
                                                , ""
                                                , ""
                                                , 0
                                                , ""
                                                , 0
                                                , ""
                                                , ""
                                                , false
                                                , false));

                                    }


                                    //////////////////   Error              ..........................


                                    lists.get(index).add(new MyMallModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, viewAllProductList));


                                    //////////////////   Error              ..........................


                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> gridLayout = new ArrayList<>();

                                    ArrayList<String> productsIds = (ArrayList<String>) documentSnapshot.get("products");


                                    for (String productsId : productsIds) {

                                        gridLayout.add(new HorizontalProductScrollModel(productsId
                                                , ""
                                                , ""
                                                , ""
                                                , ""));

                                    }
                                    lists.get(index).add(new MyMallModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), gridLayout));


                                }

                            }
                            MyMallAdapter myMallAdapter = new MyMallAdapter(lists.get(index));
                            myMallRecyclerView.setAdapter(myMallAdapter);

                            myMallAdapter.notifyDataSetChanged();
                            MyMallFragment.swipeRefreshLayout.setRefreshing(false);

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public static void loadWishList(final Context context, final Dialog dialog, final boolean loadProductData) {
        wishList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                        wishList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBQueries.wishList.contains(productID)) {
                            ProductsDetailsActivity.ALREADY_ADDED_TO_WISH_LIST = true;

                            if (ProductsDetailsActivity.add_to_wish_list_button != null) {
                                ProductsDetailsActivity.add_to_wish_list_button.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));

                            }
                        } else {
                            if (ProductsDetailsActivity.add_to_wish_list_button != null) {

                                ProductsDetailsActivity.add_to_wish_list_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            ProductsDetailsActivity.ALREADY_ADDED_TO_WISH_LIST = false;
                        }


                        if (loadProductData) {
                            wishListModelList.clear();
                            final String productID = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").
                                    document(productID).
                                    get().
                                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        final DocumentSnapshot documentSnapshot = task.getResult();

                                        FirebaseFirestore.getInstance().collection("PRODUCTS").
                                                document(productID).
                                                collection("QUANTITY").
                                                orderBy("time", Query.Direction.ASCENDING).
                                                get().
                                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {


                                                                wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                        , documentSnapshot.get("average_rating").toString()
                                                                        , (long) documentSnapshot.get("total_ratings")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (boolean) documentSnapshot.get("COD")
                                                                        , true));

                                                            } else {

                                                                wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                        , documentSnapshot.get("average_rating").toString()
                                                                        , (long) documentSnapshot.get("total_ratings")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (boolean) documentSnapshot.get("COD")
                                                                        , false));

                                                            }
                                                          //  MyWishListFragment.wishListAdapter.notifyDataSetChanged();

                                                        } else {

                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();


                                                        }
                                                    }
                                                });


                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public static void removeFromWishList(final int index, final Context context) {
        final String removeProductId = wishList.get(index);

        wishList.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();
        for (int x = 0; x < wishList.size(); x++) {
            updateWishList.put("product_ID_" + x, wishList.get(x));

        }
        updateWishList.put("list_size", (long) wishList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (wishListModelList.size() != 0) {
                        wishListModelList.remove(index);
                       // MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                    }
                    ProductsDetailsActivity.ALREADY_ADDED_TO_WISH_LIST = false;
                    Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();

                } else {

                    if (ProductsDetailsActivity.add_to_wish_list_button != null) {

                        ProductsDetailsActivity.add_to_wish_list_button.setSupportImageTintList(context.getResources().getColorStateList(R.color.red));
                    }
                    wishList.add(index, removeProductId);

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }

                ProductsDetailsActivity.running_wishList_query = false;
            }

        });

    }

    public static void clearData() {
        category_modelList.clear();
        lists.clear();
        loadedCategoriesName.clear();
        wishList.clear();
        wishListModelList.clear();
        cartList.clear();
        cartItemModelList.clear();
        myRatedIds.clear();
        myRatings.clear();
        addressModelList.clear();
        rewardModelList.clear();
        myOrderItemModelList.clear();
    }

    public static void loadRatingList(final Context context) {
        if (!ProductsDetailsActivity.running_rating_query) {

            ProductsDetailsActivity.running_rating_query = true;

            myRatedIds.clear();
            myRatings.clear();

            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        List<String> orderProductsIds = new ArrayList<>();
                        for (int z = 0; z < myOrderItemModelList.size(); z++) {

                            orderProductsIds.add(myOrderItemModelList.get(z).getProductId());

                        }
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {

                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRatings.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductsDetailsActivity.productID)) {
                                ProductsDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductsDetailsActivity.rate_Now_Container != null) {
                                    ProductsDetailsActivity.setRating(ProductsDetailsActivity.initialRating);
                                }

                            }

                            if (orderProductsIds.contains(task.getResult().get("product_ID_" + x).toString())) {
                                myOrderItemModelList.get(orderProductsIds.indexOf(task.getResult().get("product_ID_" + x).toString())).setRating(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);

                            }

                        }
//                        if (MyOrdersFragment.myOrderAdapter != null) {
//                            MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
//                        }

                    } else {
//                        String error = task.getException().getMessage();
//                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }

                    ProductsDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount) {
        cartList.clear();

        firebaseFirestore.collection("USERS").
                document(FirebaseAuth.getInstance().
                        getUid()).
                collection("USER_DATA").
                document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {


                        cartList.add(task.getResult().get("product_ID_" + x).toString());

                        if (DBQueries.cartList.contains(productID)) {
                            ProductsDetailsActivity.ALREADY_ADDED_TO_CART = true;


                        } else {

                            ProductsDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }


                        if (loadProductData) {
                            cartItemModelList.clear();
                            final String productID = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productID)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {


                                        /////////////////////////////////////////////////////////
                                        final DocumentSnapshot documentSnapshot = task.getResult();

                                        FirebaseFirestore.getInstance().collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            int index = 0;
                                                            if (cartList.size() >= 2) {
                                                                index = cartList.size() - 2;
                                                            }


                                                            if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {

                                                                cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (long) 1
                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                        , (long) 0
                                                                        , true
                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                        , (long) documentSnapshot.get("stock_quantity")
                                                                        , documentSnapshot.getBoolean("COD")));


                                                            } else {

                                                                cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                        , documentSnapshot.get("product_title").toString()
                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                        , documentSnapshot.get("product_price").toString()
                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                        , (long) 1
                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                        , (long) 0
                                                                        , false
                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                        , (long) documentSnapshot.get("stock_quantity")
                                                                        , documentSnapshot.getBoolean("COD")));

                                                            }

                                                            if (cartList.size() == 1) {
                                                                cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                                parent.setVisibility(View.VISIBLE);
                                                            }

                                                            if (cartList.size() == 0) {
                                                                cartItemModelList.clear();

                                                            }

                                                            MyCartFragment.cartAdapter.notifyDataSetChanged();


                                                        } else {

                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();


                                                        }
                                                    }
                                                });
                                        ////////////////////////////////////////////////////////////


                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                    if (cartList.size() != 0) {
                        badgeCount.setVisibility(View.VISIBLE);
                    } else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }

                    if (DBQueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBQueries.cartList.size()));
                    } else {
                        badgeCount.setText("99+");
                    }


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }


    public static void removeFromCart(final int index, final Context context, final TextView cartTotalAmount) {
        final String removeProductId = cartList.get(index);

        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();
        for (int x = 0; x < cartList.size(); x++) {
            updateCartList.put("product_ID_" + x, cartList.get(x));

        }
        updateCartList.put("list_size", (long) cartList.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (cartItemModelList.size() != 0) {
                        cartItemModelList.remove(index);
                        MyCartFragment.cartAdapter.notifyDataSetChanged();
                    }

                    if (cartList.size() == 0) {

                        LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                        parent.setVisibility(View.GONE);

                        cartItemModelList.clear();
                    }

                    Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();

                } else {


                    cartList.add(index, removeProductId);

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                }

                ProductsDetailsActivity.running_cart_query = false;
            }

        });

    }

    public static void loadAddresses(final Context context, final Dialog loadingDialogue, final boolean gotoDeliveryActivity) {
        addressModelList.clear();


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    Intent deliveryIntent = null;

                    if ((long) task.getResult().get("list_size") == 0) {
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    } else {

                        for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {

                            addressModelList.add(new AddressModel(task.getResult().getBoolean("selected_" + x)
                                    , task.getResult().getString("city_" + x)
                                    , task.getResult().getString("locality_" + x)
                                    , task.getResult().getString("flat_no_" + x)
                                    , task.getResult().getString("pin_code_" + x)
                                    , task.getResult().getString("landmark_" + x)
                                    , task.getResult().getString("name_" + x)
                                    , task.getResult().getString("mobile_no_" + x)
                                    , task.getResult().getString("alternate_mobile_no_" + x)
                                    , task.getResult().getString("state_" + x)


                            ));

                            if ((boolean) task.getResult().get("selected_" + x)) {
                                selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                            }
                        }
                        if (gotoDeliveryActivity) {

                            deliveryIntent = new Intent(context, DeliveryActivity.class);
                        }

                    }
                    if (gotoDeliveryActivity) {

                        context.startActivity(deliveryIntent);
                    }

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }

                loadingDialogue.dismiss();
            }
        });
    }


    public static void loadRewards(final Context context, final Dialog loadingDialogue, final boolean onRewardFragment) {


        rewardModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Date lastSeenDate = task.getResult().getDate("Last seen");


                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                    if (documentSnapshot.get("type").toString().equals("Discount") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(), documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("percentage").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , documentSnapshot.getDate("validity")
                                                                , (boolean) documentSnapshot.get("already_used")


                                                        ));
                                                    } else if (documentSnapshot.get("type").toString().equals("Flat Rs. * OFF") && lastSeenDate.before(documentSnapshot.getDate("validity"))) {

                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(), documentSnapshot.get("type").toString()
                                                                , documentSnapshot.get("lower_limit").toString()
                                                                , documentSnapshot.get("upper_limit").toString()
                                                                , documentSnapshot.get("amount").toString()
                                                                , documentSnapshot.get("body").toString()
                                                                , documentSnapshot.getDate("validity")
                                                                , (boolean) documentSnapshot.get("already_used")

                                                        ));

                                                    }
                                                }

                                                if (onRewardFragment) {

                                                  //  MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                                                }


                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();

                                            }
                                            loadingDialogue.dismiss();
                                        }
                                    });


                        } else {
                            loadingDialogue.dismiss();

                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static void loadOrders(final Context context, @Nullable final MyOrderAdapter myOrderAdapter, final Dialog loadingDialogue) {
        myOrderItemModelList.clear();

        firebaseFirestore.collection("USERS").
                document(FirebaseAuth.getInstance().getUid()).
                collection("USER_ORDERS").
                orderBy("time", Query.Direction.DESCENDING).
                get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                firebaseFirestore.collection("ORDERS").
                                        document(documentSnapshot.getString("order_id")).
                                        collection("OrderItems").
                                        get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {

                                                        MyOrderItemModel myOrderItemModel = new MyOrderItemModel(
                                                                orderItems.getString("Product Id")
                                                                , orderItems.getString("Product Title")
                                                                , orderItems.getString("Product Image")
                                                                , orderItems.getString("Ordered status")

                                                                , orderItems.getString("Address")
                                                                , orderItems.getString("Coupon Id")
                                                                , orderItems.getString("Cutted Price")


                                                                , orderItems.getDate("Ordered date")
                                                                , orderItems.getDate("Packed date")
                                                                , orderItems.getDate("Shipped date")
                                                                , orderItems.getDate("Delivered date")
                                                                , orderItems.getDate("Cancelled date")


                                                                , orderItems.getString("Discounted Price")
                                                                , orderItems.getLong("free coupons")
                                                                , orderItems.getString("Fullname")
                                                                , orderItems.getString("order_id")
                                                                , orderItems.getString("Payment Method")
                                                                , orderItems.getString("pincode")
                                                                , orderItems.getString("Product Price")
                                                                , orderItems.getLong("Product Quantity")
                                                                , orderItems.getString("User Id")

                                                                , orderItems.getString("Delivery price")
                                                                , orderItems.getBoolean("Cancellation requested")

                                                        );


                                                        myOrderItemModelList.add(myOrderItemModel);

                                                    }

                                                    loadRatingList(context);
                                                    if (myOrderAdapter != null) {

                                                        myOrderAdapter.notifyDataSetChanged();
                                                    }


                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                                loadingDialogue.dismiss();
                                            }
                                        });
                            }
                        } else {
                            loadingDialogue.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static void checkNotifications(boolean remove, @Nullable final TextView notifyCount) {
        if (remove) {

            registration.remove();

        } else {
            registration = firebaseFirestore.collection("USERS").
                    document(FirebaseAuth.getInstance().getUid()).
                    collection("USER_DATA").
                    document("MY_NOTIFICATIONS").
                    addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

                            if (snapshot != null && snapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;

                                for (long x = 0; x < (long) snapshot.get("list_size"); x++) {
                                    notificationModelList.add(0, new NotificationModel(
                                            snapshot.get("Body_" + x).toString(),
                                            snapshot.get("Image_" + x).toString(),
                                            snapshot.getBoolean("Readed_" + x)));


                                    if (!snapshot.getBoolean("Readed_" + x)) {
                                        unread++;
                                        if (notifyCount != null) {
                                            if (unread > 0) {
                                                notifyCount.setVisibility(View.VISIBLE);
                                                if (unread < 99) {
                                                    notifyCount.setText(String.valueOf(unread));
                                                } else {
                                                    notifyCount.setText("99+");
                                                }
                                            } else {
                                                notifyCount.setVisibility(View.INVISIBLE);


                                            }

                                        }
                                    }


                                }
                                if (NotificationActivity.adapter != null) {

                                    NotificationActivity.adapter.notifyDataSetChanged();
                                }


                            }

                        }
                    });
        }


    }
}
