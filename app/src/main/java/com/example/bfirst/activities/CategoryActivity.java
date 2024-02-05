package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.bfirst.database.DBQueries.lists;
import static com.example.bfirst.database.DBQueries.loadFragmentData;
import static com.example.bfirst.database.DBQueries.loadedCategoriesName;

import com.example.bfirst.models.HorizontalProductScrollModel;
import com.example.bfirst.adapters.MyMallAdapter;
import com.example.bfirst.models.MyMallModel;
import com.example.bfirst.R;
import com.example.bfirst.models.SliderModel;
import com.example.bfirst.models.WishListModel;

public class CategoryActivity extends AppCompatActivity {
private RecyclerView category_recyclerView;
 private List<MyMallModel>myMallModelFakeList = new ArrayList<>();

private MyMallAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("Category");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //////////////////////////// home page  fake list//////////////////////////////////////////
        List<SliderModel>sliderModelFakeList = new ArrayList<>();


        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));
        sliderModelFakeList.add(new SliderModel("null","#ffffff"));


        List<HorizontalProductScrollModel>horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("","","","",""));

        myMallModelFakeList.add(new MyMallModel(0,sliderModelFakeList));
        myMallModelFakeList.add(new MyMallModel(1,"","#ffffff"));
        myMallModelFakeList.add(new MyMallModel(2,"","#ffffff",horizontalProductScrollModelFakeList,new ArrayList<WishListModel>() ));
        myMallModelFakeList.add(new MyMallModel(3, "","#ffffff",horizontalProductScrollModelFakeList));

        //////////////////////////// home page fake list//////////////////////////////////////////


        category_recyclerView = findViewById(R.id.category_recyclerView);





        //////////////////////////////////////////


         LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        category_recyclerView.setLayoutManager(testingLayoutManager);

        adapter = new MyMallAdapter(myMallModelFakeList);



        int listPosition = 0;
         for(int x=0;   x < loadedCategoriesName.size();x++){
             if(loadedCategoriesName.get(x).equals(title.toUpperCase())){
                 listPosition  =x;
             }
         }
         if(listPosition == 0){
             loadedCategoriesName.add(title.toUpperCase());
             lists.add(new ArrayList<MyMallModel>());

             loadFragmentData(category_recyclerView,this,loadedCategoriesName.size()-1,title);

         }else {
             adapter = new MyMallAdapter(lists.get(listPosition));

         }




        category_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_search){
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);

            return true;

        }
        else if(id==android.R.id.home){
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
