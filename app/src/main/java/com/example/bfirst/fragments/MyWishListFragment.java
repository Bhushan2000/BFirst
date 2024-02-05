package com.example.bfirst.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.adapters.WishListAdapter;
import com.example.bfirst.models.WishListModel;
import com.example.bfirst.repo.AppViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWishListFragment extends Fragment {

    public MyWishListFragment() {
        // Required empty public constructor
    }

    private RecyclerView wishListRecyclerView;
    private Dialog loadingDialogue;
    private WishListAdapter wishListAdapter;
    private AppViewModel appViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wish_list, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        wishListRecyclerView = view.findViewById(R.id.my_wish_list_recyclerview);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        wishListRecyclerView.setLayoutManager(linearLayoutManager);


//        if(DBQueries.wishListModelList.size()==0){
//            DBQueries.wishList.clear();
//            DBQueries.loadWishList(getContext(),loadingDialogue,true);
//        }else {
//            loadingDialogue.dismiss();
//        }

        getWishList();

        return view;


    }

    public void getWishList(){
        appViewModel.getWishListLiveData().observe(this, new Observer<List<WishListModel>>() {
            @Override
            public void onChanged(List<WishListModel> wishListModels) {
                Log.d(getClass().getSimpleName(), "wishlist:  ....." + wishListModels.size());

                wishListAdapter = new WishListAdapter(wishListModels, true);
                wishListRecyclerView.setAdapter(wishListAdapter);
                wishListAdapter.notifyDataSetChanged();
            }
        });
    }
}
