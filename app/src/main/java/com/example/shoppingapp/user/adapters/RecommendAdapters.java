package com.example.shoppingapp.user.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.user.activities.DetailedActivity;
import com.example.shoppingapp.user.models.PopularModel;
import com.example.shoppingapp.user.models.RecommendModel;
import com.example.shoppingapp.user.models.ViewAllModel;

import java.util.List;

public class RecommendAdapters extends RecyclerView.Adapter<RecommendAdapters.ViewHolder> {

    Context context;
    List<ViewAllModel> recommendModelList;


    public RecommendAdapters(Context context, List<ViewAllModel> recommendModelList) {
        this.context = context;
        this.recommendModelList = recommendModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(recommendModelList.get(position).getImg_url()).into(holder.recImg);
        holder.name.setText(recommendModelList.get(position).getName());
        holder.description.setText(recommendModelList.get(position).getDescription());
        holder.rating.setText(String.valueOf(recommendModelList.get(position).getRating()));
        holder.ratingBar.setRating(recommendModelList.get(position).getRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detail",recommendModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recImg;
        TextView description, name, rating;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.rec_des);
            name = itemView.findViewById(R.id.rec_name);
            recImg = itemView.findViewById(R.id.rec_img);
            rating = itemView.findViewById(R.id.rec_rating);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }
}
