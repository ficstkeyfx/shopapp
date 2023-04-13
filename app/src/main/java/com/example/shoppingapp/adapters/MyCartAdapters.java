package com.example.shoppingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.MyCartModel;

import java.util.List;

public class MyCartAdapters extends RecyclerView.Adapter<MyCartAdapters.ViewHolder> {

    List<MyCartModel> myCartModels;
    Context context;

    public MyCartAdapters(List<MyCartModel> myCartModels, Context context) {
        this.myCartModels = myCartModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(myCartModels.get(position).getImg_url()).into(holder.cartImg);
        holder.cartName.setText(myCartModels.get(position).getProductName());
        holder.cartTime.setText(myCartModels.get(position).getProductTime());
        holder.cartDate.setText("Ngày: " + myCartModels.get(position).getProductDate());
        holder.cartPrice.setText("Giá: " + myCartModels.get(position).getProductPrice()+"vnđ");
        holder.cartTotalPrice.setText(myCartModels.get(position).getTotalPrice()+"vnđ");
        holder.cartTotalQuantity.setText("Số lượng: " + myCartModels.get(position).getTotalQuantity());
    }

    @Override
    public int getItemCount() {
        return myCartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartName, cartTotalQuantity, cartPrice, cartTotalPrice;
        TextView cartDate, cartTime;
        ImageView cartImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartName = itemView.findViewById(R.id.ProductName_cart);
            cartTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity_Cart);
            cartPrice = itemView.findViewById(R.id.tvPrice_cart);
            cartTotalPrice = itemView.findViewById(R.id.tvtotalPrice_cart);
            cartDate = itemView.findViewById(R.id.tvDate);
            cartTime = itemView.findViewById(R.id.tvTime);
            cartImg = itemView.findViewById(R.id.img_cart);

        }
    }
}
