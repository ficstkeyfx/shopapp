package com.example.shoppingapp.admin.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.CSKH.CSKH_Activity;
import com.example.shoppingapp.admin.activities.CSKH.Detail_CSKH_Activity;
import com.example.shoppingapp.admin.model.CSKH_Model;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.example.shoppingapp.user.models.ChatModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CSKH_Adapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final List<CSKH_Model> listCSKH;
    FirebaseDatabase database;
    FirebaseFirestore firestore;

    public CSKH_Adapter(List<CSKH_Model> listCSKH)
    {
        this.listCSKH = listCSKH;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listCSKH.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listCSKH.get(position);
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
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.activity_cskh_item, null);
        }
        else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        CSKH_Model cskh = (CSKH_Model) getItem(position);
        TextView msg = viewProduct.findViewById(R.id.msg);
        TextView name = viewProduct.findViewById(R.id.name);
        TextView time = viewProduct.findViewById(R.id.time);
        CircleImageView ava = viewProduct.findViewById(R.id.avatar);
        ImageView next = viewProduct.findViewById(R.id.next);
        ImageView circle = viewProduct.findViewById(R.id.circle);
        Glide.with(viewProduct.getContext()).load(cskh.getAva()).into(ava);
        name.setText(cskh.getName());
        msg.setText(cskh.getMsg());
        time.setText(cskh.getTime());

        if (cskh.getType() == 0)
        {
            circle.setVisibility(View.VISIBLE);
        }
        else
            circle.setVisibility(View.GONE);

        viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cskh.setType(1);
                Intent intent = new Intent(viewProduct.getContext(), Detail_CSKH_Activity.class);
                intent.putExtra("detail", (Serializable) listCSKH.get(position));
                viewProduct.getContext().startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return viewProduct;
    }

}