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
    TextView salePrice,originPrice,rating, description, binhLuan, detailed_rating;
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
            rating.setText(viewAllModel.getRating());
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
                addedToCart();
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

                            }
                            else
                            {
                                ratingBar.setRating(5);
                                detailed_rating.setText("5.0");
                            }
                        }

                    });
                        break;
                    }
                }
            }
        });

//        firestore.collection("AllProducts").document(id).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (DocumentSnapshot doc: task.getResult())
//                {
//                    String name = doc.getString("name");
//                    String ava = doc.getString("img_url");
//                    String date = doc.getString("date");
//                    String comment = doc.getString("comment");
//                    float rating = Float.parseFloat( doc.getString("rating"));
//                    CommentModel commentModel = new CommentModel(ava, name, date, comment, rating);
//                    lstComment.add(commentModel);
//                }
//            }
//        });


        lstView.setAdapter(commentAdapters);

    }

    private void addedToCart() {
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