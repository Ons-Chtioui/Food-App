package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.FoodListAdapter;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Helper.DatabaseHelper;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityListFoodsBinding;

import java.util.ArrayList;
import java.util.Queue;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private Boolean isSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();

        initList();

    }
    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<Foods> list;

        // 🔍 Si recherche active
        if (isSearch) {
            list = db.searchFoods(searchText);
        }
        // 🟦 Sinon : filtrer par catégorie
        else {
            list = db.getFoodsByCategory(categoryId);
        }

        //  Affichage RecyclerView
        if (list.size() > 0) {
            binding.foodListView.setLayoutManager(
                    new GridLayoutManager(ListFoodsActivity.this, 2)
            );
            adapterListFood = new FoodListAdapter(list);
            binding.foodListView.setAdapter(adapterListFood);
        }

        binding.progressBar.setVisibility(View.GONE);
    }

    private void getIntentExtra(){
        categoryId=(getIntent().getIntExtra("CategoryId",0))-1;
        categoryName=getIntent().getStringExtra("Category");
        searchText=getIntent().getStringExtra("text");
        isSearch=getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i=new Intent(ListFoodsActivity.this,MainActivity.class);
                    startActivity(i);
                  finish();
            }
        });
    }
}