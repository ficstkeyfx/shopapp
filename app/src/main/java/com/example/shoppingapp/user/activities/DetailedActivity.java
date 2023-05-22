package com.example.shoppingapp.user.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.user.adapters.CommentAdapters;
import com.example.shoppingapp.user.models.CommentModel;
import com.example.shoppingapp.user.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    TextView quantity, name;
    int totalQuantity = 1;
    int totalPrice = 0;
    ImageView detailImg,addItem,removeItem, addComment;
    TextView salePrice,originPrice,rating, description, binhLuan, detailed_rating, size39, size40,size41 ,size42 ,size43 ,size44;
    Button addToCart;
    ViewAllModel viewAllModel = null;
    ListView lstView;
    ImageView back;
    RatingBar ratingBar;
    String id = "";
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseDatabase database;
    float sum = 0;
    int s39, s40, s41, s42, s43, s44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        size39 = findViewById(R.id.size39);
        size40 = findViewById(R.id.size40);
        size41 = findViewById(R.id.size41);
        size42 = findViewById(R.id.size42);
        size43 = findViewById(R.id.size43);
        size44 = findViewById(R.id.size44);
        detailed_rating = findViewById(R.id.detailed_rating);
        ratingBar = findViewById(R.id.rating);
        addComment = findViewById(R.id.addComment);
        binhLuan = findViewById(R.id.binhLuan);
        detailImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        salePrice = findViewById(R.id.detailed_sale_price);
        originPrice = findViewById(R.id.detailed_origin_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_description);
        addToCart = findViewById(R.id.add_to_cart);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.productTitle);
        back = findViewById(R.id.goBack);

        lstView = findViewById(R.id.lstComment);

        if(viewAllModel!=null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailImg);
            rating.setText(String.valueOf(viewAllModel.getRating()));
            description.setText(viewAllModel.getDescription());
            salePrice.setText(viewAllModel.getPrice()+"000đ");
            originPrice.setText((viewAllModel.getPrice()+100)+"000đ");
            name.setText(viewAllModel.getName());
        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice = totalQuantity * viewAllModel.getPrice();
                if (s39 == 0 && s40 == 0 && s41 == 0 && s42 == 0 && s43 == 0 && s44 == 0)
                {
                    Toast.makeText(DetailedActivity.this, "Bạn chưa chọn size giày", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s39 == 1)
                    addedToCart(39);
                else if (s40 == 1)
                    addedToCart(40);
                else if (s41 == 1)
                    addedToCart(41);
                else if (s42 == 1)
                    addedToCart(42);
                else if (s43 == 1)
                    addedToCart(43);
                else if (s44 == 1)
                    addedToCart(44);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivity.this, AddCommentActivity.class);
                intent.putExtra("key", id);
                startActivity(intent);
            }
        });

        ArrayList<CommentModel> lstComment = new ArrayList<CommentModel>();
        CommentAdapters commentAdapters;
        commentAdapters = new CommentAdapters(lstComment);
        firestore.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc: task.getResult())
                {
                    String nameP = doc.getString("name");
                    if (viewAllModel.getName().equalsIgnoreCase(nameP))
                    {
                        id = doc.getId();
                        System.out.println(id);
                        firestore.collection("AllProducts").document(id).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot doc: task.getResult())
                            {
                                String name = doc.getString("name");
                                String ava = doc.getString("avatar");
                                String date = doc.getString("date");
                                String comment = doc.getString("comment");

                                float rating = Float.parseFloat( doc.get("rating").toString());
                                sum += rating;
                                CommentModel commentModel = new CommentModel(ava, name, date, comment, rating);
                                lstComment.add(commentModel);
                                commentAdapters.notifyDataSetChanged();
                                binhLuan.setText("Bình luận (" + lstComment.size() + ")");
                            }
                            if (lstComment.size() != 0)
                            {
                                float danhgia = sum / lstComment.size();
                                ratingBar.setRating(danhgia);
                                detailed_rating.setText(String.valueOf(danhgia));
                                firestore.collection("AllProducts").document(id).update("rating", danhgia);
                            }
                            else
                            {
                                ratingBar.setRating(5);
                                detailed_rating.setText("5.0");
                                firestore.collection("AllProducts").document(id).update("rating", 5);
                            }
                        }

                    });
                        break;
                    }
                }
            }
        });

        lstView.setAdapter(commentAdapters);
        size39.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.backsizegiay);
                size40.setBackgroundResource(R.drawable.back);
                size41.setBackgroundResource(R.drawable.back);
                size42.setBackgroundResource(R.drawable.back);
                size43.setBackgroundResource(R.drawable.back);
                size44.setBackgroundResource(R.drawable.back);
                s39 = 1;
                s40 = s41 = s42 = s43 = s44 = 0;
            }
        });

        size40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.back);
                size40.setBackgroundResource(R.drawable.backsizegiay);
                size41.setBackgroundResource(R.drawable.back);
                size42.setBackgroundResource(R.drawable.back);
                size43.setBackgroundResource(R.drawable.back);
                size44.setBackgroundResource(R.drawable.back);
                s40 = 1;
                s39 = s41 = s42 = s43 = s44 = 0;
            }
        });

        size41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.back);
                size40.setBackgroundResource(R.drawable.back);
                size41.setBackgroundResource(R.drawable.backsizegiay);
                size42.setBackgroundResource(R.drawable.back);
                size43.setBackgroundResource(R.drawable.back);
                size44.setBackgroundResource(R.drawable.back);
                s41 = 1;
                s39 = s40 = s42 = s43 = s44 = 0;
            }
        });

        size42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.back);
                size40.setBackgroundResource(R.drawable.back);
                size41.setBackgroundResource(R.drawable.back);
                size42.setBackgroundResource(R.drawable.backsizegiay);
                size43.setBackgroundResource(R.drawable.back);
                size44.setBackgroundResource(R.drawable.back);
                s42 = 1;
                s39 = s40 = s41 = s43 = s44 = 0;
            }
        });
        size43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.back);
                size40.setBackgroundResource(R.drawable.back);
                size41.setBackgroundResource(R.drawable.back);
                size42.setBackgroundResource(R.drawable.back);
                size43.setBackgroundResource(R.drawable.backsizegiay);
                size44.setBackgroundResource(R.drawable.back);
                s43 = 1;
                s39 = s40 = s41 = s42 = s44 = 0;
            }
        });


        size44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size39.setBackgroundResource(R.drawable.back);
                size40.setBackgroundResource(R.drawable.back);
                size41.setBackgroundResource(R.drawable.back);
                size42.setBackgroundResource(R.drawable.back);
                size43.setBackgroundResource(R.drawable.back);
                size44.setBackgroundResource(R.drawable.backsizegiay);
                s44 = 1;
                s39 = s40 = s41 = s42 = s43 = 0;
            }
        });
    }

    private void addedToCart(int size) {
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productPrice",viewAllModel.getPrice());
        cartMap.put("productType",viewAllModel.getType());
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("productDate",saveCurrentDate);
        cartMap.put("productTime",saveCurrentTime);
        cartMap.put("totalPrice",totalPrice);
        cartMap.put("img_url",viewAllModel.getImg_url());
        cartMap.put("size",size);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Cart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }


}