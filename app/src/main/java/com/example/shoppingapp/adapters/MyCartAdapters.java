package com.example.shoppingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapters extends RecyclerView.Adapter<MyCartAdapters.ViewHolder> {

    List<MyCartModel> myCartModels;
    Context context;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyCartAdapters(List<MyCartModel> myCartModels, Context context) {
        this.myCartModels = myCartModels;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(myCartModels.get(position).getImg_url()).into(holder.cartImg);
        holder.cartName.setText(myCartModels.get(position).getProductName());
        holder.cartTime.setText(myCartModels.get(position).getProductTime());
        holder.cartDate.setText("Ngày: " + myCartModels.get(position).getProductDate());
        holder.cartPrice.setText("Giá: " + myCartModels.get(position).getProductPrice()+"vnđ");
        holder.cartTotalPrice.setText(myCartModels.get(position).getTotalPrice()+"vnđ");
        holder.cartTotalQuantity.setText("Số lượng: " + myCartModels.get(position).getTotalQuantity());

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Cart")
                        .document(myCartModels.get(position).getDocumentId()).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    myCartModels.remove(myCartModels.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context,"Đã xóa sản phẩm ra khỏi giỏ hàng",Toast.LENGTH_SHORT).show();
                                }else Toast.makeText(context,"Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return myCartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartName, cartTotalQuantity, cartPrice, cartTotalPrice;
        TextView cartDate, cartTime;
        ImageView cartImg;
        ImageView deleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteItem = itemView.findViewById(R.id.imgbtn_delete);
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
