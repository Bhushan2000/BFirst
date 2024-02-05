package com.example.bfirst.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.R;
import com.example.bfirst.models.SliderModel;
import com.example.bfirst.models.WishListModel;
import com.example.bfirst.activities.MainActivity;
import com.example.bfirst.adapters.Category_Adapter;
import com.example.bfirst.adapters.MyMallAdapter;
import com.example.bfirst.models.CategoryModel;
import com.example.bfirst.models.HorizontalProductScrollModel;
import com.example.bfirst.repo.AppViewModel;
import com.example.bfirst.repo.Categories;

import java.util.ArrayList;
import java.util.List;

import static com.example.bfirst.database.DBQueries.category_modelList;
import static com.example.bfirst.database.DBQueries.lists;
import static com.example.bfirst.database.DBQueries.loadCategories;
import static com.example.bfirst.database.DBQueries.loadFragmentData;
import static com.example.bfirst.database.DBQueries.loadedCategoriesName;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyMallFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private RecyclerView myMallRecyclerview;




    private MyMallAdapter adapter;
    private Category_Adapter category_adapter;


    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private AppViewModel appViewModel;


    public static MyMallFragment newInstance(String param1, String param2) {
        MyMallFragment fragment = new MyMallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_my_mall, container, false);

        initViews(view);



        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.blue)
                , getContext().getResources().getColor(R.color.red)
                , getContext().getResources().getColor(R.color.yellow));


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myMallRecyclerview.setLayoutManager(testingLayoutManager);










        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);


            categoryRecyclerView.setVisibility(View.VISIBLE);
            myMallRecyclerview.setVisibility(View.VISIBLE);

            if (category_modelList.size() == 0) {
                loadCategories(categoryRecyclerView, getContext());

            } else {
                category_adapter = new Category_Adapter(category_modelList);
                category_adapter.notifyDataSetChanged();

                // getCategories();

            }
            categoryRecyclerView.setAdapter(category_adapter);


            if (lists.size() == 0) {


                loadedCategoriesName.add("HOME");
                lists.add(new ArrayList<MyMallModel>());

                loadFragmentData(myMallRecyclerview, getContext(), 0, "Home");

            } else {
                adapter = new MyMallAdapter(lists.get(0));
                adapter.notifyDataSetChanged();

                ////////////////////////////////////////
            }

            myMallRecyclerview.setAdapter(adapter);


        } else {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            categoryRecyclerView.setVisibility(View.GONE);
            myMallRecyclerview.setVisibility(View.GONE);



        }


        ///////////////////////////////swipe refresh/////////////////
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });

        ///////////////////////////////swipe refresh/////////////////

        return view;

    }

    private void initViews(View view){
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        myMallRecyclerview = view.findViewById(R.id.homePageRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

    }

    private void getCategories() {
        appViewModel.getCategoriesLiveData().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categories) {

                Log.d(getClass().getSimpleName(), "categories: ........... " + categories.size());

                category_adapter = new Category_Adapter(categories);
                category_adapter.notifyDataSetChanged();

            }
        });

    }

    private void reloadPage() {

        networkInfo = connectivityManager.getActiveNetworkInfo();

//        category_modelList.clear();
//        lists.clear();
//        loadedCategoriesName.clear();
        DBQueries.clearData();





        if (networkInfo != null && networkInfo.isConnected() == true) {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);



            categoryRecyclerView.setVisibility(View.VISIBLE);
            myMallRecyclerview.setVisibility(View.VISIBLE);


            categoryRecyclerView.setAdapter(category_adapter);
            myMallRecyclerview.setAdapter(adapter);


            loadCategories(categoryRecyclerView, getContext());

            loadedCategoriesName.add("HOME");
            lists.add(new ArrayList<MyMallModel>());
            loadFragmentData(myMallRecyclerview, getContext(), 0, "Home");





        } else {
            MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            Toast.makeText(getContext(),"No internet connection ",Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            myMallRecyclerview.setVisibility(View.GONE);


            swipeRefreshLayout.setRefreshing(false);

        }

    }

}
