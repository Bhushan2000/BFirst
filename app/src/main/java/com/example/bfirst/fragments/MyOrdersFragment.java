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
import com.example.bfirst.adapters.MyOrderAdapter;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.repo.AppViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MyOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView myOrderRecyclerView;
    private MyOrderAdapter myOrderAdapter;
    private AppViewModel appViewModel;

    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


        myOrderRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrderRecyclerView.setLayoutManager(layoutManager);


        appViewModel.getOrdersLiveData().observe(this, new Observer<List<MyOrderItemModel>>() {
            @Override
            public void onChanged(List<MyOrderItemModel> myOrderItemModels) {
                if (myOrderItemModels != null && !myOrderItemModels.isEmpty()) {
                    Log.d(getClass().getSimpleName(), "orders:  ....." + myOrderItemModels.size());
                    myOrderAdapter = new MyOrderAdapter(myOrderItemModels);
                    myOrderRecyclerView.setAdapter(myOrderAdapter);
                    myOrderAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(myOrderRecyclerView, "Error While fetching", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return view;

    }


}
