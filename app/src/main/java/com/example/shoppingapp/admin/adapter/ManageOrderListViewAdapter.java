package com.example.shoppingapp.admin.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageOrderPeopleModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ManageOrderListViewAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<ManageOrderPeopleModel> listAccount;

    public ManageOrderListViewAdapter(ArrayList<ManageOrderPeopleModel> listAccount)
    {
        this.listAccount = listAccount;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listAccount.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listAccount.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listAccount.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.activity_order_item, null);
        }
        else viewProduct = convertView;
        ImageView avatar = viewProduct.findViewById(R.id.avatar);
        ImageView productAva = viewProduct.findViewById(R.id.productAva);
        Button accept = viewProduct.findViewById(R.id.xacNhan);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        //Bind dữ liệu phần tử vào View
        ManageOrderPeopleModel orderPeople = (ManageOrderPeopleModel) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.name)).setText(orderPeople.getName());
        ((TextView) viewProduct.findViewById(R.id.address)).setText(orderPeople.getAddress());
        ((TextView) viewProduct.findViewById(R.id.phone)).setText(orderPeople.getPhone());
        ((TextView) viewProduct.findViewById(R.id.productName)).setText(orderPeople.getProductName());
        ((TextView) viewProduct.findViewById(R.id.productTime)).setText("Ngày đặt hàng:" + orderPeople.getProductTime());

        ((TextView) viewProduct.findViewById(R.id.productPrice)).setText("Giá: " + orderPeople.getProductPrice()+"000đ");
        ((TextView) viewProduct.findViewById(R.id.productQuantity)).setText("Số lượng: " + orderPeople.getProductQuantity());
        ((TextView) viewProduct.findViewById(R.id.TotalPrice)).setText("Tổng tiền thanh toán: " + orderPeople.getTotalPrice() + "000đ");

        if(orderPeople.getStatus() == null||orderPeople.getStatus().equals("0")||orderPeople.getStatus()==""){
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setText("Trạng thái: Đang chờ xác nhận");
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setTextColor(Color.BLUE);
        }else if(orderPeople.getStatus().equals("1")){
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setText("Trạng thái: Đang vận chuyển");
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setTextColor(Color.GREEN);
        }else {
            System.out.println(orderPeople.getStatus());
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setText("Trạng thái: Đã giao hàng");
            ((TextView) viewProduct.findViewById(R.id.statusOrder)).setTextColor(Color.RED);
        }


        Glide.with(viewProduct).load(orderPeople.getAvatar()).into(avatar);
        Glide.with(viewProduct).load(orderPeople.getProductAva()).into(productAva);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("CurrentUser").document(orderPeople.getKey()).collection("Order")
                        .document(orderPeople.getProductID()).update("status",1);
                viewProduct.refreshDrawableState();
            }
        });

        return viewProduct;
    }

}
