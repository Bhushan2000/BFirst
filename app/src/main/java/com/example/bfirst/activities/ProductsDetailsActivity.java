package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.models.ProductSpecificationModel;
import com.example.bfirst.R;
import com.example.bfirst.fragments.SignInFragment;
import com.example.bfirst.fragments.SignUpFragment;
import com.example.bfirst.models.WishListModel;
import com.example.bfirst.adapters.MyRewardsAdapter;
import com.example.bfirst.adapters.ProductDetailsAdapter;
import com.example.bfirst.adapters.ProductImagesAdapter;
import com.example.bfirst.models.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.bfirst.activities.MainActivity.drawer;
import static com.example.bfirst.activities.MainActivity.showCart;
import static com.example.bfirst.activities.Register.setSignUpFragment;

public class ProductsDetailsActivity extends AppCompatActivity {

    public static boolean fromSearch = false;
    public static boolean running_wishList_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;


////////////////////////product description/////////////////

    private ViewPager productImagesViewpager;
    private TabLayout viewPagerIndicator;


    public static FloatingActionButton add_to_wish_list_button;
    public static boolean ALREADY_ADDED_TO_WISH_LIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    ////////////////////////product description/////////////////
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private TextView productOnlyDescriptionBody;

    private String productDescription;
    private String productOtherDetails;


    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();
////////////////////////product description/////////////////


    private TextView rewardTitle;
    private TextView rewardBody;


    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private String productOriginalPrice;
    private TextView cuttedPrice;
    private ImageView CODIndicator;
    private TextView tvCODIndicator;


/////////////rating layout//////////

    public static LinearLayout rate_Now_Container;
    private TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;

    private TextView averageRating;

    public static int initialRating;
    /////////////rating layout//////////
    private Button buyNowBtn;

    private LinearLayout couponReedemptionLayout;
    private Button couponReedemBtn;
    private FirebaseFirestore firebaseFirestore;


    ///////////////coupon dialogue/////////
    private TextView couponTitle;
    private TextView couponBody;
    private TextView couponExpiryDate;
    private RecyclerView couponsrecyclerView;
    private LinearLayout selectedCoupon;

    private TextView discountedPrice;
    private  TextView originalPrice;
///////////////coupon dialogue/////////

    private Dialog sign_in_dialogue;
    private LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    private FirebaseUser currentUser;
    public static String productID;

    private Dialog loadingDialogue;


    private DocumentSnapshot documentSnapshot;

    private TextView badgeCount;

    public static Activity productDetailActivity;
    private boolean inStock = false;

    private LinearLayout applyOrRemoveBtnContainer;
    private static final String TAG = "ProductsDetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productImagesViewpager = findViewById(R.id.products_images_viewpager);
        viewPagerIndicator = findViewById(R.id.viewPager_Indicator);
        add_to_wish_list_button = findViewById(R.id.add_to_wish_list_button);


        productDetailsViewPager = findViewById(R.id.product_detail_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tablayout);

        buyNowBtn = findViewById(R.id.buy_now_button);




        productTitle = findViewById(R.id.productTitle);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniView);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);

        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        CODIndicator = findViewById(R.id.COD__indicator_imageview);
        tvCODIndicator = findViewById(R.id.textView_tv_cod_indicator);

        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);

        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product__details_container);

        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNoContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total__ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progressbar_container);
        averageRating = findViewById(R.id.average_rating);

        couponReedemBtn = findViewById(R.id.tv_coupon_redemption_button);
        addToCartBtn = findViewById(R.id.add_to_cart_button);
        couponReedemptionLayout = findViewById(R.id.coupon_redemption_layout);

        initialRating = -1;


/////////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(ProductsDetailsActivity.this);
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

/////////////////////////////////////loading dialogue/////////////////////////////////////////////


        ///////////////coupon dialogue///////////

        final Dialog checkCouponPrice = new Dialog(ProductsDetailsActivity.this);
        checkCouponPrice.setContentView(R.layout.coupons_redemption_dialogue);
        checkCouponPrice.setCancelable(true);
        checkCouponPrice.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView toogleRecyclerView = checkCouponPrice.findViewById(R.id.toogle_recyclerview);
        couponsrecyclerView = checkCouponPrice.findViewById(R.id.coupons_recyclerView);
        selectedCoupon = checkCouponPrice.findViewById(R.id.selected_coupon);

        couponTitle = checkCouponPrice.findViewById(R.id.coupon_title);
        couponBody = checkCouponPrice.findViewById(R.id.coupon_body);

        applyOrRemoveBtnContainer = checkCouponPrice.findViewById(R.id.apply_or_remove_buttons_container);
        applyOrRemoveBtnContainer.setVisibility(View.GONE);

        couponExpiryDate = checkCouponPrice.findViewById(R.id.coupon_validity);


        originalPrice = checkCouponPrice.findViewById(R.id.original_price);
        discountedPrice = checkCouponPrice.findViewById(R.id.discounted_price);



        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductsDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        couponsrecyclerView.setLayoutManager(layoutManager);


        toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogueRecyclerView();
            }
        });


        ////////////////coupon dialogue////////////////


        ////////////////////////////////////////////////////////////////

        firebaseFirestore = FirebaseFirestore.getInstance();
        final List<String> productsImages = new ArrayList<>();
        productID = getIntent().getStringExtra("product_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            documentSnapshot = task.getResult();


                            /////////////////////////////////
                            firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {


                                                for (long x = 1; x < (long) documentSnapshot.get("no_of_products_images") + 1; x++) {

                                                    productsImages.add(documentSnapshot.get("product_image_" + x).toString());
                                                }

                                                ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productsImages);
                                                productImagesViewpager.setAdapter(productImagesAdapter);


                                                productTitle.setText(documentSnapshot.get("product_title").toString());
                                                averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                                totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                                productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");


                                                ///////// coupon dialogue

                                                originalPrice.setText(productPrice.getText());
                                                productOriginalPrice = documentSnapshot.get("product_price").toString();

                                                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBQueries.rewardModelList, true, couponsrecyclerView, selectedCoupon,
                                                        productOriginalPrice, couponTitle, couponExpiryDate, couponBody, discountedPrice);
                                                couponsrecyclerView.setAdapter(myRewardsAdapter);
                                                myRewardsAdapter.notifyDataSetChanged();


                                                //////////

                                                cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");


                                                if ((Boolean) documentSnapshot.get("COD")) {
                                                    CODIndicator.setVisibility(View.VISIBLE);
                                                    tvCODIndicator.setVisibility(View.VISIBLE);

                                                } else {

                                                    CODIndicator.setVisibility(View.INVISIBLE);
                                                    tvCODIndicator.setVisibility(View.INVISIBLE);
                                                }
                                                rewardTitle.setText((long) documentSnapshot.get("free_coupons") + documentSnapshot.get("free_coupon_title").toString());
                                                rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());


                                                if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                                    productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                                    productDetailsOnlyContainer.setVisibility(View.GONE);
                                                    productDescription = documentSnapshot.get("product_description").toString();


                                                    productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                                    for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                        productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                        for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {

                                                            productSpecificationModelList.add(new ProductSpecificationModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));


                                                        }
                                                    }


                                                } else {

                                                    productDetailsTabsContainer.setVisibility(View.GONE);
                                                    productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                                    productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                                                }
                                                totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                                for (int x = 0; x < 5; x++) {
                                                    TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                                    rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                                    ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                    int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));

                                                    progressBar.setMax(maxProgress);
                                                    progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                                                }
                                                totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                                averageRating.setText(documentSnapshot.get("average_rating").toString());
                                                productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));


                                                if (currentUser != null) {

                                                    if (DBQueries.myRatings.size() == 0) {
                                                        DBQueries.loadRatingList(ProductsDetailsActivity.this);

                                                    }


                                                    if (DBQueries.cartList.size() == 0) {
                                                        DBQueries.loadCartList(ProductsDetailsActivity.this, loadingDialogue, false, badgeCount, new TextView(ProductsDetailsActivity.this));

                                                    }


                                                    if (DBQueries.wishList.size() == 0) {
                                                        DBQueries.loadWishList(ProductsDetailsActivity.this, loadingDialogue, false);

                                                    }
                                                    if (DBQueries.rewardModelList.size() == 0) {
                                                        DBQueries.loadRewards(ProductsDetailsActivity.this, loadingDialogue, false);
                                                    }

                                                    if (DBQueries.cartList.size() != 0 && DBQueries.wishList.size() != 0 && DBQueries.rewardModelList.size() != 0) {
                                                        loadingDialogue.dismiss();
                                                    }


                                                } else {

                                                    loadingDialogue.dismiss();
                                                }


                                                if (DBQueries.myRatedIds.contains(productID)) {
                                                    int index = DBQueries.myRatedIds.indexOf(productID);
                                                    initialRating = Integer.parseInt(String.valueOf(DBQueries.myRatings.get(index))) - 1;
                                                    setRating(initialRating);
                                                }


                                                if (DBQueries.cartList.contains(productID)) {
                                                    ALREADY_ADDED_TO_CART = true;
                                                } else {

                                                    ALREADY_ADDED_TO_CART = false;
                                                }


                                                if (DBQueries.wishList.contains(productID)) {
                                                    ALREADY_ADDED_TO_WISH_LIST = true;
                                                    add_to_wish_list_button.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                                } else {
                                                    add_to_wish_list_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                                                    ALREADY_ADDED_TO_WISH_LIST = false;
                                                }


                                                if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                                    inStock = true;
                                                    buyNowBtn.setVisibility(View.VISIBLE);

                                                    ////////////////////////// ADD TO CART ////////////////////////////////

                                                    addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (currentUser == null) {
                                                                sign_in_dialogue.show();
                                                            } else {

                                                                if (!running_cart_query) {
                                                                    running_cart_query = true;


                                                                    if (ALREADY_ADDED_TO_CART) {
                                                                        running_cart_query = false;

                                                                        Toast.makeText(ProductsDetailsActivity.this, "Allready added to cart", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        final Map<String, Object> addProduct = new HashMap<>();
                                                                        addProduct.put("product_ID_" + String.valueOf(DBQueries.cartList.size()), productID);
                                                                        addProduct.put("list_size", (long) (DBQueries.cartList.size() + 1));


                                                                        firebaseFirestore.collection("USERS").
                                                                                document(currentUser.getUid()).
                                                                                collection("USER_DATA").
                                                                                document("MY_CART").update(addProduct).
                                                                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {


                                                                                            if (DBQueries.cartItemModelList.size() != 0) {
                                                                                                DBQueries.cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                                        , (long) documentSnapshot.get("free_coupons")
                                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                                        , (long) 1
                                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                                        , (long) 0
                                                                                                        , inStock
                                                                                                        , (long) documentSnapshot.get("max_quantity")
                                                                                                        , (long) documentSnapshot.get("stock_quantity")
                                                                                                        ,documentSnapshot.getBoolean("COD")));
                                                                                            }

                                                                                            ALREADY_ADDED_TO_CART = true;

                                                                                            DBQueries.cartList.add(productID);

                                                                                            Toast.makeText(ProductsDetailsActivity.this, "Product added to Cart Successfully", Toast.LENGTH_SHORT).show();

                                                                                            invalidateOptionsMenu();

                                                                                            running_cart_query = false;


                                                                                        } else {


                                                                                            running_cart_query = false;
                                                                                            String error = task.getException().getMessage();
                                                                                            Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                                                                        }
                                                                                    }
                                                                                });


                                                                    }
                                                                }


                                                            }

                                                        }
                                                    });


                                                    ////////////////////////// ADD TO CART ////////////////////////////////


                                                } else {
                                                    inStock = false;
                                                    buyNowBtn.setVisibility(View.GONE);

                                                    TextView outOffStock = (TextView) addToCartBtn.getChildAt(0);
                                                    outOffStock.setText("Out of Stock");
                                                    outOffStock.setTextColor(getResources().getColor(R.color.red));
                                                    outOffStock.setCompoundDrawables(null, null, null, null);
                                                }

                                            } else {

                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    });

                            ///////////////////////////////////

                        } else {
                            loadingDialogue.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                        }


                    }
                });

        viewPagerIndicator.setupWithViewPager(productImagesViewpager, true);

        ////////////////////// ADD TO WISH LIST BUTTON //////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////////////////
        add_to_wish_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser == null) {
                    sign_in_dialogue.show();
                } else {



                    if (!running_wishList_query) {
                        running_wishList_query = true;


                        if (ALREADY_ADDED_TO_WISH_LIST) {
                            int index = DBQueries.wishList.indexOf(productID);
                            DBQueries.removeFromWishList(index, ProductsDetailsActivity.this);


                            add_to_wish_list_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                        } else {
                            add_to_wish_list_button.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                            final Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBQueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBQueries.wishList.size() + 1));


                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                    .document("MY_WISHLIST").update(addProduct)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                if (DBQueries.wishListModelList.size() != 0) {
                                                    DBQueries.wishListModelList.add(new WishListModel(productID, documentSnapshot.get("product_image_1").toString()
                                                            , documentSnapshot.get("product_title").toString()
                                                            , (long) documentSnapshot.get("free_coupons")
                                                            , documentSnapshot.get("average_rating").toString()
                                                            , (long) documentSnapshot.get("total_ratings")
                                                            , documentSnapshot.get("product_price").toString()
                                                            , documentSnapshot.get("cutted_price").toString()
                                                            , (boolean) documentSnapshot.get("COD")
                                                            , inStock));
                                                }

                                                ALREADY_ADDED_TO_WISH_LIST = true;

                                                add_to_wish_list_button.setSupportImageTintList(getResources().getColorStateList(R.color.red));
                                                DBQueries.wishList.add(productID);

                                                Toast.makeText(ProductsDetailsActivity.this, "Product added to Wishlist Successfully", Toast.LENGTH_SHORT).show();


                                            } else {

                                                add_to_wish_list_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();

                                            }
                                            running_wishList_query = false;
                                        }
                                    });


                        }
                    }
                }

            }
        });


        ////////////////////// ADD TO WISH LIST BUTTON //////////////////////////


        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        /////////////rating layout//////////


        rate_Now_Container = findViewById(R.id.rate_now_container);

        for (int x = 0; x < rate_Now_Container.getChildCount(); x++) {
            final int startPosition = x;
            rate_Now_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (currentUser == null) {
                        sign_in_dialogue.show();
                    } else {
                        if (startPosition != initialRating) {

                            if (!running_rating_query) {
                                running_rating_query = true;

                                setRating(startPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBQueries.myRatedIds.contains(productID)) {

                                    TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);


                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(startPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) startPosition - initialRating, true));


                                } else {


                                    updateRating.put(startPosition + 1 + "_star", (long) documentSnapshot.get(startPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) startPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            final Map<String, Object> myRating = new HashMap<>();
                                            if (DBQueries.myRatedIds.contains(productID)) {

                                                myRating.put("rating_" + DBQueries.myRatedIds.indexOf(productID), (long) startPosition + 1);


                                            } else {

                                                myRating.put("list_size", (long) DBQueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBQueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBQueries.myRatedIds.size(), (long) startPosition + 1);
                                            }


                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if (DBQueries.myRatedIds.contains(productID)) {
                                                            DBQueries.myRatings.set(DBQueries.myRatedIds.indexOf(productID), (long) startPosition + 1);


                                                            TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);

                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));


                                                        } else {

                                                            DBQueries.myRatedIds.add(productID);
                                                            DBQueries.myRatings.add((long) startPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - startPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));
                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                            Toast.makeText(ProductsDetailsActivity.this, "Thank You ! For Rating ", Toast.LENGTH_SHORT).show();
                                                        }


                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingFigures = (TextView) ratingsNoContainer.getChildAt(x);


                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);


                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());

                                                            progressBar.setMax(maxProgress);


                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));

                                                        }

                                                        initialRating = startPosition;
                                                        averageRating.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBQueries.wishList.contains(productID) && DBQueries.wishListModelList.size() != 0) {
                                                            int index = DBQueries.wishList.indexOf(productID);


                                                            DBQueries.wishListModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBQueries.wishListModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));

                                                        }

                                                    } else {
                                                        setRating(initialRating);

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;

                                                }
                                            });

                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductsDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }


                        }
                    }

                }
            });

        }

        /////////////rating layout//////////


        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser == null) {
                    sign_in_dialogue.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialogue.show();

                    productDetailActivity = ProductsDetailsActivity.this;

                    DeliveryActivity.cartItemModelList = new ArrayList<>();

                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupons")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) documentSnapshot.get("offers_applied")
                            , (long) 0
                            , inStock
                            , (long) documentSnapshot.get("max_quantity")
                            , (long) documentSnapshot.get("stock_quantity")
                            ,documentSnapshot.getBoolean("COD")));
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                    if (DBQueries.addressModelList.size() == 0) {

                        DBQueries.loadAddresses(ProductsDetailsActivity.this, loadingDialogue,true);
                    } else {
                        loadingDialogue.dismiss();
                        Intent intent = new Intent(ProductsDetailsActivity.this, DeliveryActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        couponReedemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCouponPrice.show();

            }
        });
        couponsrecyclerView.setVisibility(View.GONE);


        /////////////////////////////////////////////SIGN IN DIALOGUE///////


        sign_in_dialogue = new Dialog(ProductsDetailsActivity.this);
        sign_in_dialogue.setContentView(R.layout.sign_in_dialogue);
        sign_in_dialogue.setCancelable(true);
        sign_in_dialogue.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogueSignInBtn = sign_in_dialogue.findViewById(R.id.sign_in_btn);
        Button dialogueSignUpBtn = sign_in_dialogue.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(this, Register.class);

        dialogueSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseButton = true;
                SignInFragment.disableCloseButton = true;
                sign_in_dialogue.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);

            }
        });
        dialogueSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseButton = true;
                SignInFragment.disableCloseButton = true;
                sign_in_dialogue.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponReedemptionLayout.setVisibility(View.GONE);

        } else {
            couponReedemptionLayout.setVisibility(View.VISIBLE);

        }


        if (currentUser != null) {

            if (DBQueries.myRatings.size() == 0) {
                DBQueries.loadRatingList(ProductsDetailsActivity.this);

            }




            if (DBQueries.wishList.size() == 0) {
                DBQueries.loadWishList(ProductsDetailsActivity.this, loadingDialogue, false);

            }
            if (DBQueries.rewardModelList.size() == 0) {
                DBQueries.loadRewards(ProductsDetailsActivity.this, loadingDialogue, false);
            }

            if (DBQueries.cartList.size() != 0 && DBQueries.wishList.size() != 0 && DBQueries.rewardModelList.size() != 0) {
                loadingDialogue.dismiss();
            }


        } else {

            loadingDialogue.dismiss();
        }




        if (DBQueries.myRatedIds.contains(productID)) {
            int index = DBQueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBQueries.myRatings.get(index))) - 1;
            setRating(initialRating);
        }


        if (DBQueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {

            ALREADY_ADDED_TO_CART = false;
        }


        if (DBQueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISH_LIST = true;
            add_to_wish_list_button.setSupportImageTintList(getResources().getColorStateList(R.color.red));
        } else {
            add_to_wish_list_button.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

            ALREADY_ADDED_TO_WISH_LIST = false;
        }

        invalidateOptionsMenu();
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


    public static void setRating(int startPosition) {


        for (int x = 0; x < rate_Now_Container.getChildCount(); x++) {
            ImageView startButton = (ImageView) rate_Now_Container.getChildAt(x);
            startButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= startPosition) {
                startButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }


    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);

        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);

        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);

        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.action_cart);

        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
        if (currentUser != null) {
            if (DBQueries.cartList.size() == 0) {
                DBQueries.loadCartList(ProductsDetailsActivity.this, loadingDialogue, false, badgeCount, new TextView(ProductsDetailsActivity.this));

            } else {
                badgeCount.setVisibility(View.VISIBLE);
                if (DBQueries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBQueries.cartList.size()));
                } else {
                    badgeCount.setText("99+");
                }
            }
        }

        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    sign_in_dialogue.show();
                } else {
                    Intent cartIntent = new Intent(ProductsDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);

                }

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            productDetailActivity = null;
            finish();
            return true;


        }

        else if (id == R.id.action_search) {

            if (fromSearch){
                    finish();
            }else{

                Intent searchIntent = new Intent(this,SearchActivity.class);
                startActivity(searchIntent);
            }

            return true;

        }

        else if (id == R.id.action_cart) {
            if (currentUser == null) {
                sign_in_dialogue.show();
            } else {
                Intent cartIntent = new Intent(ProductsDetailsActivity.this, MainActivity.class);

                showCart = true;
                startActivity(cartIntent);


                drawer.closeDrawer(GravityCompat.START);
                return true;


            }


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailActivity = null;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;

    }
}
