package com.example.shoppingapp.user.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.example.shoppingapp.user.activities.PayActivity;
import com.example.shoppingapp.user.models.VoucherModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VoucherAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final List<VoucherModel> listVoucher;
    FirebaseDatabase database;

    public VoucherAdapter(List<VoucherModel> listVoucher)
    {
        this.listVoucher = listVoucher;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listVoucher.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listVoucher.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listVoucher.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.voucher_item, null);
        }
        else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        VoucherModel voucherModel = (VoucherModel) getItem(position);
        TextView reduce = viewProduct.findViewById(R.id.reduce);
        TextView minimum = viewProduct.findViewById(R.id.minimum);
        TextView quantity = viewProduct.findViewById(R.id.quantity);
        TextView date = viewProduct.findViewById(R.id.date);
        Button use = viewProduct.findViewById(R.id.use);

        reduce.setText("Giảm " + voucherModel.getDiscount() + "k");
        minimum.setText("Đơn tối thiểu " + voucherModel.getMinimum()+ "k");
        quantity.setText("Số lượng: " + voucherModel.getQuantity());
        date.setText("Ngày hết hạn: " + voucherModel.getExpiration_date().toString());



        use.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(viewProduct.getContext(), PayActivity.class);
                intent.putExtra("discount", voucherModel.getDiscount());
                intent.putExtra("minimum", voucherModel.getMinimum());
                intent.putExtra("quantity", voucherModel.getQuantity());
                PayActivity.updateVoucher(String.valueOf(voucherModel.getDiscount()), String.valueOf(voucherModel.getMinimum()),String.valueOf(voucherModel.getQuantity()));
                viewProduct.getContext().startActivity(intent);
            }
        });

        return viewProduct;
    }

}

