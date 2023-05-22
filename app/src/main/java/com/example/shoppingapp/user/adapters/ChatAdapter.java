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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.example.shoppingapp.user.activities.PayActivity;
import com.example.shoppingapp.user.models.ChatModel;
import com.example.shoppingapp.user.models.VoucherModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    List<ChatModel> listChat;
    FirebaseDatabase database;


    public ChatAdapter(List<ChatModel> listChat)
    {
        this.listChat = listChat;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listChat.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listChat.get(position);
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

        View viewProduct;
        if (convertView == null)
        {
            viewProduct = View.inflate(parent.getContext(), R.layout.chat_item, null);
        }
        else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        ChatModel chatModel = (ChatModel) getItem(position);
        TextView time = viewProduct.findViewById(R.id.time);
        time.setText(chatModel.getTime());
        TextView name = viewProduct.findViewById(R.id.name);
        name.setText(chatModel.getName());
        TextView msg = viewProduct.findViewById(R.id.msg);
        msg.setText(chatModel.getMsg());
        CircleImageView avatar = viewProduct.findViewById(R.id.avatar);
        if (!msg.getText().equals("Admin"))
            Glide.with(viewProduct.getContext()).load(chatModel.getAvatar()).into(avatar);


        return viewProduct;
    }

}

