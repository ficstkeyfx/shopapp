package com.example.shoppingapp.user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.updateProduct.updateProduct;
import com.example.shoppingapp.user.models.CommentModel;
import com.example.shoppingapp.user.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

public class AddCommentActivity extends AppCompatActivity {
    ImageView avatar, back;
    TextView name;
    RatingBar rating;
    EditText cmt;
    Button save,clear;
    FirebaseDatabase database;
    FirebaseAuth auth;

    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        cmt = findViewById(R.id.cmt);
        save = findViewById(R.id.save);
        clear = findViewById(R.id.clear);
        back = findViewById(R.id.back);
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String id = intent.getStringExtra("key");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        CommentModel commentModel = new CommentModel();

        database.getReference().child("Users").child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if(userModel!=null||userModel.getAvatar()!=null||userModel.getAvatar().length()!=0)  {
                            Glide.with(AddCommentActivity.this).load(userModel.getAvatar()).into(avatar);
                        }else {
                            Glide.with(AddCommentActivity.this).load("https://firebasestorage.googleapis.com/v0/b/shoppingapp-3f5b0.appspot.com/o/avatar.png?alt=media&token=0be702f2-aa4e-4a7c-b7a0-768924e21882").into(avatar);
                        }
                        commentModel.setAvatar(userModel.getAvatar());
                        commentModel.setName(userModel.getName());
                        name.setText(userModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(cmt.getText().toString()))
                {
                    Toast.makeText(AddCommentActivity.this, "Phần đánh giá còn trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                float ratingStar = rating.getRating();
                commentModel.setRating(ratingStar);
                String date = "" + java.time.LocalDate.now();
                commentModel.setDate(date);
                commentModel.setComment(String.valueOf(cmt.getText()));
                firestore.collection("AllProducts").document(id).collection("Comments").add(commentModel);
                Toast.makeText(AddCommentActivity.this, "Đánh giá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddCommentActivity.this, DetailedActivity.class));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating.setRating(0);
                cmt.setText("");
                cmt.setHint("Đánh giá");
            }
        });

    }
}