package com.example.bfirst.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bfirst.models.ProductSpecificationModel;
import com.example.bfirst.R;
import com.example.bfirst.adapters.ProductSpecificationAdapter;

import java.util.List;



public class ProductSpecificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }


    private RecyclerView product_specification_recyclerview;
    public List<ProductSpecificationModel>productSpecificationModelList;





     public static ProductSpecificationFragment newInstance(String param1, String param2) {
        ProductSpecificationFragment fragment = new ProductSpecificationFragment();
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
        View view= inflater.inflate(R.layout.fragment_product_specification, container, false);
        product_specification_recyclerview = view.findViewById(R.id.product_specification_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        product_specification_recyclerview.setLayoutManager(linearLayoutManager);

//
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"General"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(0,"Storage"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//
//        productSpecificationModelList.add(new ProductSpecificationModel(1,"RAM","2GB"));
//


        ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter( productSpecificationModelList);
        product_specification_recyclerview.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;

    }
}
