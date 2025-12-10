package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.foodapp.Activity.DetailActivity;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FoodListAdapter  extends RecyclerView.Adapter<FoodListAdapter.viewholder> {
    ArrayList<Foods> items;
    Context context;

    public FoodListAdapter(ArrayList<Foods> items){
        this.items=items;
    }
    @NonNull
    @Override
    public FoodListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(context) .inflate(R.layout.viewholder_list_food,parent,false);
        return new viewholder(inflate);
    }



    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.viewholder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.timeTxt.setText(items.get(position).getTimeValue()+" min");
        holder.priceTxt.setText(items.get(position).getPrice()+"D" );
        holder.rateTxt.setText(""+items.get(position).getStar());

        Glide.with(context)
                .load(items.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, DetailActivity.class);
                i.putExtra("obj",items.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt,priceTxt,rateTxt,timeTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView){
            super (itemView);
            timeTxt=itemView.findViewById(R.id.timeTxt);
            rateTxt=itemView.findViewById(R.id.rateTxt);
            priceTxt=itemView.findViewById(R.id.priceTxt);
            titleTxt=itemView.findViewById(R.id.titleTxt);
            pic=itemView.findViewById(R.id.img);
        }
    }
}
