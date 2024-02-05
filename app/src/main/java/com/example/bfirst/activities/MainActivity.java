package com.example.bfirst.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.fragments.MyAccountFragment;
import com.example.bfirst.fragments.MyCartFragment;
import com.example.bfirst.fragments.MyMallFragment;
import com.example.bfirst.fragments.MyOrdersFragment;
import com.example.bfirst.fragments.MyRewardsFragment;
import com.example.bfirst.fragments.MyWishListFragment;
import com.example.bfirst.R;
import com.example.bfirst.fragments.SignInFragment;
import com.example.bfirst.fragments.SignUpFragment;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.repo.AppViewModel;
import com.example.bfirst.repo.CancelledOrders;
import com.example.bfirst.repo.Categories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.bfirst.activities.Register.setSignUpFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISH_LIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;


    private FirebaseAuth firebaseAuth;
    private static final String TAG = "MainActivity";
    private Window window;
    private Toolbar toolbar;

    private long backPressedTime;
    private Toast backToast;

    private Dialog sign_in_dialogue;


    private FrameLayout frameLayout;

    private NavigationView navigationView;
    private ImageView actionbarLogo;


    private int currentFragment = -1;
    public static DrawerLayout drawer;
    private FirebaseUser currentUser;

    public static boolean showCart = false;
    public static boolean resetMainActivity = false;

    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    public static Activity mainActivity;

    // account
    private CircleImageView main_profile_photo;
    private TextView email, fullName;
    private ImageView addProfileIcon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        actionbarLogo = findViewById(R.id.action_bar_logo);
        currentFragment = -1;

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();
        drawer = findViewById(R.id.drawer_layout);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        navigationView.setCheckedItem(R.id.nav_my_mall);


        frameLayout = findViewById(R.id.mainFrameLayout);


        // account fragment
        main_profile_photo = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        email = navigationView.getHeaderView(0).findViewById(R.id.email);
        fullName = navigationView.getHeaderView(0).findViewById(R.id.name);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);
        addProfileIcon.setVisibility(View.GONE);


        ////////////////sign in dialogue///////////////
        sign_in_dialogue = new Dialog(MainActivity.this);
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


        if (showCart) {
            mainActivity = this;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);


        } else {

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar
                    , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            setFragment(new MyMallFragment(), HOME_FRAGMENT);


        }






    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);

        } else {


            if (DBQueries.email == null) {


                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBQueries.fullname = task.getResult().getString("fullname");
                            DBQueries.email = task.getResult().getString("email");
                            DBQueries.profile = task.getResult().getString("profile");
                            email.setText(DBQueries.email);
                            fullName.setText(DBQueries.fullname);
                            if (DBQueries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);

                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);

                                Glide.with(MainActivity.this).load(DBQueries.profile).apply(new RequestOptions().placeholder(R.drawable.unload)).into(main_profile_photo);
                            }

                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                email.setText(DBQueries.email);
                fullName.setText(DBQueries.fullname);
                if (DBQueries.profile.equals("")) {
                    main_profile_photo.setImageResource(R.drawable.ic_account_circle_black_24dp);
                    addProfileIcon.setVisibility(View.VISIBLE);

                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);

                    Glide.with(MainActivity.this).load(DBQueries.profile).apply(new RequestOptions().placeholder(R.drawable.unload)).into(main_profile_photo);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);

        }
        if (resetMainActivity) {

            resetMainActivity = false;
            setFragment(new MyMallFragment(), HOME_FRAGMENT);
            actionbarLogo.setVisibility(View.VISIBLE);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem = menu.findItem(R.id.action_cart);

            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser != null) {

                if (DBQueries.cartList.size() == 0) {
                    DBQueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, badgeCount, new TextView(MainActivity.this));

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
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                        showCart = true;
                    }

                }
            });

            MenuItem notifuItem = menu.findItem(R.id.action_notification);

            notifuItem.setActionView(R.layout.badge_layout);
            ImageView notifuIcon = notifuItem.getActionView().findViewById(R.id.badge_icon);
            notifuIcon.setImageResource(R.drawable.ic_notifications_black_24dp);
            TextView notifyCount = notifuItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser != null) {
                DBQueries.checkNotifications(false, notifyCount);
            }
            notifuItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });

        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);

            return true;

        } else if (id == R.id.action_notification) {
            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            startActivity(notificationIntent);

            return true;

        } else if (id == R.id.action_cart) {
            if (currentUser == null) {
                sign_in_dialogue.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

            }


            return true;

        } else if (id == android.R.id.home) {
            if (showCart) {
                mainActivity = this;
                showCart = false;
                finish();


                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {

        actionbarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);

        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);
        if (fragmentNo == CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);

        } else {
            params.setScrollFlags(scrollFlags);

        }


    }

    MenuItem menuItem;

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        if (currentUser != null) {


            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);

                    switch (menuItem.getItemId()) {


                        case R.id.nav_my_mall:


                            actionbarLogo.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                            setFragment(new MyMallFragment(), HOME_FRAGMENT);

                            break;

                        case R.id.nav_orders:

                            gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                            break;

                        case R.id.nav_rewards:

                            gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);

                            break;

                        case R.id.nav_cart:

//                    sign_in_dialogue.show();


                            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);


                            break;
                        case R.id.nav_my_wish_list:
                            gotoFragment("My Wish List", new MyWishListFragment(), WISH_LIST_FRAGMENT);


                            break;
                        case R.id.nav_my_account:
                            gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                            break;
                        case R.id.nav_sign_out:
                            Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();

                            firebaseAuth.getInstance().signOut();
                            DBQueries.clearData();
                            Intent registerIntent = new Intent(MainActivity.this, Register.class);
                            startActivity(registerIntent);
                            finish();

                            break;

                        default:
                            break;

                    }

                    drawer.removeDrawerListener(this);

                }
            });


            return true;

        } else {

            sign_in_dialogue.show();
            return false;

        }


    }


    private void setFragment(Fragment fragment, int fragmentNo) {

        if (fragmentNo != currentFragment) {
            if (fragmentNo == REWARDS_FRAGMENT) {
                window.setStatusBarColor(Color.parseColor("#3006BA"));
                toolbar.setBackgroundColor(Color.parseColor("#3006BA"));

            } else {
                window.setStatusBarColor(getResources().getColor(R.color.red));
                toolbar.setBackgroundColor(getResources().getColor(R.color.red));

            }

            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;

                super.onBackPressed();


            } else {
                if (showCart) {

                    mainActivity = this;
                    showCart = false;
                    finish();


                } else {


                    actionbarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new MyMallFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }


        }
    }


    private void goMainScreen() {
        Intent intent = new Intent(this, Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBQueries.checkNotifications(true, null);

    }


}
