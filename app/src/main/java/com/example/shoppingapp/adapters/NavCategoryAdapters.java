package com.example.shoppingapp.adapters;

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
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.NavCategoryModel;

import java.util.List;

public class NavCategoryAdapters extends RecyclerView.Adapter<NavCategoryAdapters.ViewHolder> {
    Context context;
    List<NavCategoryModel> navCategoryList ;


    public NavCategoryAdapters(Context context, List<NavCategoryModel> navCategoryList) {
        this.context = context;
        this.navCategoryList = navCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_nav_item,parent,false));

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NavCategoryModel navCategory = navCategoryList.get(position);
        Glide.with(context).load(navCategory.getImg_url()).into(holder.imgNavCat);
        holder.tvNavDesCat.setText(navCategory.getDescription());
        holder.tvNavNameCat.setText(navCategory.getName());
        holder.tvNavDisCat.setText(navCategory.getDiscount());
    }
    @Override
    public int getItemCount() {
        return navCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNavNameCat,tvNavDesCat ,tvNavDisCat;
        ImageView imgNavCat ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNavDesCat = itemView.findViewById(R.id.cat_item_des);
            tvNavNameCat=itemView.findViewById(R.id.cat_item_name);
            tvNavDisCat =itemView.findViewById(R.id.cat_item_discount);
            imgNavCat = itemView.findViewById(R.id.cat_item_img);
        }
    }
}
