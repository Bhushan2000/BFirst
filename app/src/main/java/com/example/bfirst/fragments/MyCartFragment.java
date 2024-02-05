package com.example.bfirst.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.activities.DeliveryActivity;
import com.example.bfirst.adapters.CartAdapter;
import com.example.bfirst.models.CartItemModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    public MyCartFragment() {
        // Required empty public constructor
    }

    private RecyclerView cartItemsRecyclerView;
    private Button continueBtn;
    private Dialog loadingDialogue;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;
    private LinearLayout cartTotalAndContinueLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        ///////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(getContext());
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

        /////////////////////////////////////loading dialogue/////////////////////////////////////////////


        cartItemsRecyclerView = view.findViewById(R.id.cart_recyclerview);
        continueBtn = view.findViewById(R.id.cart_continue_button);

        totalAmount = view.findViewById(R.id.total_cart_amount);

        cartTotalAndContinueLayout = view.findViewById(R.id.linearLayout6);
        cartTotalAndContinueLayout.setVisibility(View.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemsRecyclerView.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(DBQueries.cartItemModelList, totalAmount, true);
        cartItemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;

                for (int x = 0; x < DBQueries.cartItemModelList.size(); x++) {

                    CartItemModel cartItemModel = DBQueries.cartItemModelList.get(x);

                    if (cartItemModel.isInStock()) {

                        DeliveryActivity.cartItemModelList.add(cartItemModel);

                    }
                }
                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


                loadingDialogue.show();
                if (DBQueries.addressModelList.size() == 0) {

                    DBQueries.loadAddresses(getContext(), loadingDialogue,true);
                } else {
                    loadingDialogue.dismiss();
                    Intent intent = new Intent(getContext(), DeliveryActivity.class);
                    startActivity(intent);
                }

            }
        });


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();


        cartAdapter.notifyDataSetChanged();

        if (DBQueries.rewardModelList.size() == 0) {
            loadingDialogue.show();
            DBQueries.loadRewards(getContext(), loadingDialogue, false);
        }


        if (DBQueries.cartItemModelList.size() == 0) {
            DBQueries.cartList.clear();
            DBQueries.loadCartList(getContext(), loadingDialogue, true, new TextView(getContext()), totalAmount);
        } else {
            if (DBQueries.cartItemModelList.get(DBQueries.cartItemModelList.size() - 1).getType() == CartItemModel.TOTAL_AMOUNT) {
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialogue.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (CartItemModel cartItemModel : DBQueries.cartItemModelList) {

            if (!TextUtils.isEmpty(cartItemModel.getSelectedCouponId())) {

                for (RewardModel rewardModel : DBQueries.rewardModelList) {

                    if (rewardModel.getCouponId().equals(cartItemModel.getSelectedCouponId())) {
                        rewardModel.setAllReadyUsed(false);
                    }
                }

                cartItemModel.setSelectedCouponId(null);
//                if (MyRewardsFragment.myRewardsAdapter != null){
//
//                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
//                }

            }
        }
    }
}
