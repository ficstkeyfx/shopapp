package com.example.shoppingapp.user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.user.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_oder);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        finish = findViewById(R.id.btnFinish);

        List<MyCartModel> myCartModelList = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");
        String address = getIntent().getStringExtra("address");

        if(myCartModelList != null && myCartModelList.size()>0){
            for(MyCartModel model: myCartModelList){
                final HashMap<String,Object> cartMap = new HashMap<>();
                cartMap.put("productName",model.getProductName());
                cartMap.put("productPrice",model.getProductPrice());
                cartMap.put("totalQuantity",model.getTotalQuantity().toString());
                cartMap.put("productDate",model.getProductDate());
                cartMap.put("productTime",model.getProductTime());
                cartMap.put("totalPrice",model.getTotalPrice()+"");
                cartMap.put("img_url",model.getImg_url());
                cartMap.put("address",address);
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Order").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(PlaceOrderActivity.this, "Đã xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}