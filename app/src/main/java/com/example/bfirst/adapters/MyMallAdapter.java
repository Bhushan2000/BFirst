package com.example.bfirst.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.Utils.Constants;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.activities.ProductsDetailsActivity;
import com.example.bfirst.R;
import com.example.bfirst.models.SliderModel;
import com.example.bfirst.activities.ViewAllActivity;
import com.example.bfirst.models.WishListModel;
import com.example.bfirst.models.HorizontalProductScrollModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyMallAdapter extends RecyclerView.Adapter {

    private List<MyMallModel> myMallModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    public MyMallAdapter(List<MyMallModel> homePageModelList) {
        this.myMallModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (myMallModelList.get(position).getType()) {
            case 0:
                return Constants.BANNER_SLIDER;
            case 1:
                return Constants.STRIP_AD_BANNER;
            case 2:
                return Constants.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return Constants.GRID_PRODUCT_VIEW;

            default:
                return -1;

        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);
            case Constants.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdBannerViewHolder(stripAdView);
            case Constants.HORIZONTAL_PRODUCT_VIEW:
                View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layourt, parent, false);
                return new HorizontalProductViewHolder(horizontalProductView);
            case Constants.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product__layout, parent, false);
                return new GridProductViewHolder(gridProductView);


            default:
                return null;

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (myMallModelList.get(position).getType()) {
            case Constants.BANNER_SLIDER:
                List<SliderModel> sliderModelList = myMallModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case Constants.STRIP_AD_BANNER:
                String resource = myMallModelList.get(position).getResource();
                String color = myMallModelList.get(position).getBackgroundColor();
                ((StripAdBannerViewHolder) holder).setStripAd(resource, color);
                break;
            case Constants.HORIZONTAL_PRODUCT_VIEW:
                String layoutColor = myMallModelList.get(position).getBackgroundColor();
                String horizontalLayoutTitle = myMallModelList.get(position).getTitle();
                List<WishListModel> viewAllProductList = myMallModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = myMallModelList.get(position).getHorizontalProductScrollModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalProductScrollModelList, horizontalLayoutTitle, layoutColor, viewAllProductList);
                break;
            case Constants.GRID_PRODUCT_VIEW:
                String gridLayoutColor = myMallModelList.get(position).getBackgroundColor();
                String gridLayoutTitle = myMallModelList.get(position).getTitle();
                List<HorizontalProductScrollModel> gridProductScrollModelList = myMallModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewHolder) holder).setGridProductLayout(gridProductScrollModelList, gridLayoutTitle, gridLayoutColor);
                break;
            default:
                return;
        }

        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;

        }

    }

    @Override
    public int getItemCount() {
        return myMallModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager bannerSliderViewPager;
        private List<SliderModel> sliderModelList;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangeList;


        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.banner_slider_view_pager);


        }

        ////////////////////////////////banner slider
        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }


            arrangeList = new ArrayList<>();
            for (int x = 0; x < sliderModelList.size(); x++) {
                arrangeList.add(x, sliderModelList.get(x));

            }
            arrangeList.add(0, sliderModelList.get(sliderModelList.size() - 2));
            arrangeList.add(1, sliderModelList.get(sliderModelList.size() - 1));
            arrangeList.add(sliderModelList.get(0));
            arrangeList.add(sliderModelList.get(1));


            SliderAdapter sliderAdapter = new SliderAdapter(arrangeList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);
            bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int i) {
                    currentPage = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooper(arrangeList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);
            startBannerSlideShow(arrangeList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pageLooper(arrangeList);
                    stopBannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        startBannerSlideShow(arrangeList);
                    }
                    return false;
                }
            });

        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
        }

        private void startBannerSlideShow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;

                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);

                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopBannerSlideShow() {
            timer.cancel();

        }
        ////////////////////////////////banner slider


    }

    ////////////////////stripAdBanner/////////////////
    public class StripAdBannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public StripAdBannerViewHolder(@NonNull View itemView) {
            super(itemView);

            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.unload)).into(stripAdImage);

            stripAdImage.setBackgroundColor(Color.parseColor(color));
        }
    }
    ////////////////////stripAdBanner/////////////////

    /////////////////Horizontal product view//////////////////
    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllBtn;
        private RecyclerView horizontalRecyclerView;
        private ConstraintLayout container;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllBtn = itemView.findViewById(R.id.horizntal__scroll_view_all_button);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            container = itemView.findViewById(R.id.container);

            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);

        }

        private void setHorizontalProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishListModel> viewAllProductList) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);


            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()) {

                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                model.setProductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));

                                WishListModel wishListModel = viewAllProductList.get(horizontalProductScrollModelList.indexOf(model));

                                wishListModel.setTotalRatings(task.getResult().getLong("total_ratings"));
                                wishListModel.setRating(task.getResult().getString("average_rating"));
                                wishListModel.setProductTitle(task.getResult().getString("product_title"));
                                wishListModel.setProductPrice(task.getResult().getString("product_price"));
                                wishListModel.setProductImage(task.getResult().getString("product_image_1"));
                                wishListModel.setFreeCoupons(task.getResult().getLong("free_coupons"));

                                wishListModel.setCuttingPrice(task.getResult().getString("cutted_price"));
                                wishListModel.setCOD(task.getResult().getBoolean("COD"));
                                wishListModel.setInStock(task.getResult().getLong("stock_quantity") > 0);

                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {
                                    if (horizontalRecyclerView.getAdapter() != null) {
                                        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                }

                            } else {
                                // do nothing
                            }
                        }
                    });

                }

            }


            if (horizontalProductScrollModelList.size() > 8) {
                horizontalLayoutViewAllBtn.setVisibility(View.VISIBLE);
                horizontalLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishListModelList = viewAllProductList;


                        Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        intent.putExtra("layout_code", 0);
                        intent.putExtra("title", title);
                        itemView.getContext().startActivity(intent);
                    }
                });
            } else {
                horizontalLayoutViewAllBtn.setVisibility(View.INVISIBLE);

            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(linearLayoutManager);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();


        }

    }
    ////////////// /////////////////Horizontal product vie////


    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private TextView gridLayoutTitle;
        private Button gridLayoutButton;
        private GridLayout gridProductLayout;
        private ConstraintLayout container;


        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle = itemView.findViewById(R.id.gridlProductlayout_title);
            gridLayoutButton = itemView.findViewById(R.id.gridlProductlayout_viewAllButton);
            gridProductLayout = itemView.findViewById(R.id.gridlayout);
            container = itemView.findViewById(R.id.container);


        }

        private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color) {
            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);


            for (final HorizontalProductScrollModel model : horizontalProductScrollModelList) {
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()) {


                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                model.setProductTitle(task.getResult().getString("product_title"));
                                model.setProductImage(task.getResult().getString("product_image_3"));
                                model.setProductPrice(task.getResult().getString("product_price"));


                                if (horizontalProductScrollModelList.indexOf(model) == horizontalProductScrollModelList.size() - 1) {


                                    setGridData(title, horizontalProductScrollModelList);

                                    if (!title.equals("")) {

                                        gridLayoutButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                                                Intent intent = new Intent(itemView.getContext(), ViewAllActivity.class);
                                                intent.putExtra("layout_code", 1);
                                                intent.putExtra("title", title);

                                                itemView.getContext().startActivity(intent);
                                            }
                                        });
                                    }
                                }

                            } else {
//                                        do nothing
                            }
                        }
                    });

                }

            }

            setGridData(title, horizontalProductScrollModelList);

        }

        private void setGridData(final String title, final List<HorizontalProductScrollModel> horizontalProductScrollModelList) {

            for (int x = 0; x < 4; x++) {
                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.h_s__product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.h_s__product_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.h_s__product_description);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.h_s__product_price);


                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.unload)).into(productImage);


                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getProductDescription());

                productPrice.setText("Rs." + horizontalProductScrollModelList.get(x).getProductPrice() + "/-");
                gridProductLayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")) {

                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(itemView.getContext(), ProductsDetailsActivity.class);
                            intent.putExtra("product_ID", horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(intent);

                        }
                    });

                }


            }


        }
    }

}
