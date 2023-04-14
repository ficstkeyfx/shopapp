package com.example.shoppingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shoppingapp.R;
import com.example.shoppingapp.models.MyOrderModel;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;

    List<MyOrderModel> myOrderList ;
    public MyOrderAdapter(Context context,List<MyOrderModel> myOrderList) {
        this.myOrderList = myOrderList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyOrderModel myOrder = myOrderList.get(position);
        if(myOrder==null) return;
        holder.tvOrderId.setText("Mã: "+myOrder.getOrder_ID());
        holder.tvQuan_order.setText("SL sản phẩm: " + myOrder.getTotalQuantity());
        holder.tvDateOrder.setText("Ngày đặt: "+myOrder.getProductDate()+", "+myOrder.getProductTime());
        holder.tvAddress.setText("Địa chỉ: "+myOrder.getAddress());
        holder.tvBillTotal.setText("Thanh toán: "+myOrder.getTotalPrice()+"000đ");
        holder.tvName.setText(myOrder.getProductName());

        if(myOrder.getStatus() ==0){
            holder.tvStatus.setTextColor(Color.BLUE);
            holder.tvStatus.setText("Đang chờ xác nhận");

        }else if(myOrder.getStatus() == 1){
            holder.tvStatus.setText("Đang vận chuyển");
        }else {
            holder.tvStatus.setText("Đã nhận hàng");
        }
    }
    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvQuan_order,tvDateOrder ,tvAddress,tvBillTotal,tvStatus, tvName ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderName);
            tvOrderId =itemView.findViewById(R.id.tvOrderID);
            tvQuan_order =itemView.findViewById(R.id.tvquantity_order);
            tvDateOrder =itemView.findViewById(R.id.tvdate_order);
            tvAddress=itemView.findViewById(R.id.tvAddress_order);
            tvBillTotal =itemView.findViewById(R.id.tv_payorder);
            tvStatus =itemView.findViewById(R.id.tvStatus);
        }
    }
}
