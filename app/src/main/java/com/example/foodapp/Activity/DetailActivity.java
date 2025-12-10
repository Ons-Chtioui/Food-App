package com.example.foodapp.Activity;

import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;


import com.example.foodapp.Domain.Foods;
import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityDetailBinding;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods obj;
    private int num=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        getIntentExtra();
        setVariable();
    }
    private void setVariable(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(DetailActivity.this)
                .load(obj.getImagePath())
                .into(binding.pic);
        binding.priceTxt.setText(obj.getPrice()+"D");
        binding.titleTxt.setText(obj.getTitle());
        binding.descriptionTxt.setText(obj.getDescription());
        binding.rateTxt.setText(obj.getStar()+" Rating");
        binding.ratingBar.setRating((float)obj.getStar());
        binding.totalTxt.setText(num*obj.getPrice()+"D");
    }
    private void getIntentExtra(){
        obj=(Foods) getIntent().getSerializableExtra("obj");
    }
}