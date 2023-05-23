package com.example.shoppingapp.user.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.MyCartModel;
import com.example.shoppingapp.user.ui.cart.MyCartsFragment;
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
        holder.cartPrice.setText("Giá: " + myCartModels.get(position).getProductPrice()+"000đ");
        holder.cartTotalPrice.setText(myCartModels.get(position).getTotalPrice()+"000đ");
        holder.cartTotalQuantity.setText("Số lượng: " + myCartModels.get(position).getTotalQuantity());
        holder.size.setText("Size: " + myCartModels.get(position).getSize());


        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                        .collection("Cart")
                                        .document(myCartModels.get(position).getDocumentId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    myCartModels.remove(myCartModels.get(position));
                                                    int price = 0;
                                                    for(MyCartModel model:myCartModels){
                                                        price += model.getTotalPrice();
                                                    }
                                                    MyCartsFragment.updateView(price);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context,"Đã xóa sản phẩm ra khỏi giỏ hàng",Toast.LENGTH_SHORT).show();
                                                }else Toast.makeText(context,"Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                notifyDataSetChanged();
                                dialog.cancel();



                            }
                        });
                builder1.setNegativeButton(
                        "Hủy bỏ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myCartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartName, cartTotalQuantity, cartPrice, cartTotalPrice;
        TextView cartDate, cartTime, size;
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
            size = itemView.findViewById(R.id.size);

        }
    }
}
