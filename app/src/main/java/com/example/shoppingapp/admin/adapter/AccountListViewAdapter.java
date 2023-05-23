package com.example.shoppingapp.admin.adapter;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountListViewAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<ManageAccountModel> listManagerAccountModel;
    FirebaseDatabase database;

    public AccountListViewAdapter(ArrayList<ManageAccountModel> listManagerAccountModel)
    {
        this.listManagerAccountModel = listManagerAccountModel;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listManagerAccountModel.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listManagerAccountModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref =  database.getReference().child("Users");

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.account_item, null);
        }
        else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        ManageAccountModel acc = (ManageAccountModel) getItem(position);
        TextView email = viewProduct.findViewById(R.id.email_account);
        TextView name = viewProduct.findViewById(R.id.account_name);
        CircleImageView img = viewProduct.findViewById(R.id.img_account);
        ImageButton delete = viewProduct.findViewById(R.id.delete_account);
        TextView lastOnline = viewProduct.findViewById(R.id.lastOnline);
        lastOnline.setText("Hoạt động gần nhất: " + acc.getLastOnline());
        email.setText(acc.getEmail());
        name.setText(acc.getName());
        if(acc.getUrl_img()!=null&&!acc.getUrl_img().equals("")) Glide.with(viewProduct).load(acc.getUrl_img()).into(img);



        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(delete.getContext());
                builder1.setMessage("Bạn chắc chắn muốn xóa tài khoản này.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đồng ý",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren())
                                        {
                                            String id = childSnapshot.getKey();
                                            System.out.println(childSnapshot.child("email").getValue());
                                            if (childSnapshot.child("email").getValue().toString().equals(acc.getEmail()))
                                            {
                                                DatabaseReference ref = database.getReference("Users").child(id);
                                                ref.removeValue();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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

        return viewProduct;
    }

}

