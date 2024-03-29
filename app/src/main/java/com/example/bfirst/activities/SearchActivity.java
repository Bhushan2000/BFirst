package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfirst.R;
import com.example.bfirst.adapters.WishListAdapter;
import com.example.bfirst.models.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

private SearchView searchView;
private TextView textView;
private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerview);
        searchView = findViewById(R.id.searchView);



        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        final List<WishListModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(list,false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.clear();
                ids.clear();




                final String[] tags =  query.toLowerCase().split(" ");
                for (final String tag:tags){
                    tag.trim();

                    FirebaseFirestore.getInstance().collection("PRODUCTS").whereArrayContains("tags",tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                                 WishListModel model = new WishListModel(documentSnapshot.getId(), documentSnapshot.get("product_image_1").toString()
                                            , documentSnapshot.get("product_title").toString()
                                            , (long) documentSnapshot.get("free_coupons")
                                            , documentSnapshot.get("average_rating").toString()
                                            , (long) documentSnapshot.get("total_ratings")
                                            , documentSnapshot.get("product_price").toString()
                                            , documentSnapshot.get("cutted_price").toString()
                                            , (boolean) documentSnapshot.get("COD")
                                            , true);

                                 model.setTags((ArrayList<String>)documentSnapshot.get("tags"));

                                 if (! ids.contains(model.getProductID())){
                                     list.add(model);
                                     ids.add(model.getProductID());

                                 }

                                }

                                if (tag.equals(tags[tags.length-1])){
                                    if (list.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else{
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(query);
                                    }
                                }

                            }else {
                                Toast.makeText(SearchActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




    }
    class Adapter extends WishListAdapter implements Filterable{
        private List<WishListModel> originalList;

        public Adapter(List<WishListModel> wishListModelList, boolean wishList) {
            super(wishListModelList, wishList);

             originalList = wishListModelList;


        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // filter result logic
                    FilterResults results = new FilterResults();
                    List<WishListModel> filteredList = new ArrayList<>();


                    final String[] tags =  constraint.toString().toLowerCase().split(" ");
                    for (WishListModel model: originalList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for (String tag: tags){
                            if (model.getTags().contains(tag)){
                                presentTags.add(tag);

                            }
                        }

                        model.setTags(presentTags);
                    }
                    for (int i = tags.length; i > 0; i-- ){
                        for (WishListModel model: originalList){
                                if (model.getTags().size() == i){
                                    filteredList.add(model);

                                }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();



                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results.count > 0 ){
                        setWishListModelList((List<WishListModel>)results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }
}