package com.example.shoppingapp.ui.admin.manageAccount;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shoppingapp.R;

import java.util.ArrayList;

public class AccountListViewAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<account> listAccount;

    AccountListViewAdapter(ArrayList<account> listAccount)
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
        return listAccount.get(position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.account_item, null);
        }
        else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        account acc = (account) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.email)).setText(acc.email);
        return viewProduct;
    }

}

