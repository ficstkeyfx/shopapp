package com.example.shoppingapp.admin.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageProductModel;

import java.util.ArrayList;

public class ManageProductListViewAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<ManageProductModel> listProduct;


    public ManageProductListViewAdapter(ArrayList<ManageProductModel> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listProduct.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listProduct.get(position).getID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.activity_product, null);
        }
        else viewProduct = convertView;
        ImageView imgProduct = viewProduct.findViewById(R.id.img);

        //Bind sữ liệu phần tử vào View
        ManageProductModel product = (ManageProductModel) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.nameProduct)).setText(product.getName());
        ((TextView) viewProduct.findViewById(R.id.typeProduct)).setText(product.getType());
        Glide.with(viewProduct).load(product.getImg()).into(imgProduct);

        return viewProduct;
    }

}