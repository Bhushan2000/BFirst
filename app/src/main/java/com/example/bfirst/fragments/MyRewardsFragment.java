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

import com.example.bfirst.Utils.Utils;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.adapters.MyRewardsAdapter;
import com.example.bfirst.models.RewardModel;
import com.example.bfirst.repo.AppViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRewardsFragment extends Fragment {

    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardsRecyclerView;
    private MyRewardsAdapter myRewardsAdapter;
    private AppViewModel appViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);


        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);


//        if (DBQueries.rewardModelList.size() == 0) {
//            DBQueries.loadRewards(getContext(), loadingDialogue, true);
//        } else {
//            loadingDialogue.dismiss();
//        }

        appViewModel.getRewardsLiveData().observe(getViewLifecycleOwner(), new Observer<List<RewardModel>>() {
            @Override
            public void onChanged(List<RewardModel> rewardModels) {
                if (rewardModels != null && !rewardModels.isEmpty()) {
                    Log.d(getClass().getSimpleName(), "rewards:  ....." + rewardModels.size());

                    myRewardsAdapter = new MyRewardsAdapter(rewardModels, false);
                    rewardsRecyclerView.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();

                } else if (rewardModels.isEmpty()) {

                    Snackbar.make(rewardsRecyclerView, "No Rewards found!", Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(rewardsRecyclerView, "Error While fetching", Snackbar.LENGTH_LONG).show();
                }
            }
        });


        return view;

    }
}
