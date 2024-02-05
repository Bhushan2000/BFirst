package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.bfirst.database.DBQueries;
import com.example.bfirst.adapters.NotificationAdapter;
import com.example.bfirst.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static NotificationAdapter adapter;
    private boolean runQuery = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

          adapter = new NotificationAdapter(DBQueries.notificationModelList);
        recyclerView.setAdapter(adapter);

        Map<String,Object> readNao = new HashMap<>();

        for (int i = 0; i < DBQueries.notificationModelList.size(); i++){
            if (! DBQueries.notificationModelList.get(i).isReaded()){
                runQuery = true;

            }
            readNao.put("Readed_"+ i,true);

        }

        if (runQuery){

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_NOTIFICATIONS")
                    .update(readNao);

        }





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < DBQueries.notificationModelList.size(); i++){
             DBQueries.notificationModelList.get(i).setReaded(true);


            }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
             finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }




}