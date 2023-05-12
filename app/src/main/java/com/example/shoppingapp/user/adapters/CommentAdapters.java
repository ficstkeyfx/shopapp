package com.example.shoppingapp.user.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.model.ManageAccountModel;
import com.example.shoppingapp.admin.model.ManageProductModel;
import com.example.shoppingapp.user.models.CommentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapters extends BaseAdapter
{
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    final ArrayList<CommentModel> listComment;
    FirebaseDatabase database;

    public CommentAdapters(ArrayList<CommentModel> listComment)
    {
        this.listComment = listComment;
    }

    @Override
    public int getCount() {
        //Trả về tổng số phần tử, nó được gọi bởi ListView
        return listComment.size();
    }

    @Override
    public Object getItem(int position) {
        //Trả về dữ liệu ở vị trí position của Adapter, tương ứng là phần tử
        //có chỉ số position trong listProduct
        return listComment.get(position);
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
            viewProduct = View.inflate(parent.getContext(), R.layout.activity_comment, null);
        }
        else viewProduct = convertView;
        ImageView ava = viewProduct.findViewById(R.id.avatar);

        //Bind sữ liệu phần tử vào View
        CommentModel commentModel = (CommentModel) getItem(position);
        RatingBar rating = viewProduct.findViewById(R.id.rating);
        rating.setRating(commentModel.getRating());
        ((TextView) viewProduct.findViewById(R.id.dayTime)).setText(commentModel.getDate());
        ((TextView) viewProduct.findViewById(R.id.name)).setText(commentModel.getName());
        ((TextView) viewProduct.findViewById(R.id.comment)).setText(commentModel.getComment());
        Glide.with(viewProduct).load(commentModel.getAvatar()).into(ava);

        return viewProduct;
    }

}