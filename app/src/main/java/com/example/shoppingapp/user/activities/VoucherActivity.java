package com.example.shoppingapp.user.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.updateProduct.searchProduct;
import com.example.shoppingapp.admin.activities.updateProduct.updateProduct;
import com.example.shoppingapp.admin.model.ManageProductModel;
import com.example.shoppingapp.user.adapters.VoucherAdapter;
import com.example.shoppingapp.user.models.ViewAllModel;
import com.example.shoppingapp.user.models.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    ListView lstView;
    VoucherAdapter voucherAdapter;

    FirebaseFirestore firebaseFirestore;

    ImageView goBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_voucher);
        lstView = findViewById(R.id.lstView);
        goBack = findViewById(R.id.goBack);
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("Voucher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                    VoucherModel voucherModel = documentSnapshot.toObject(VoucherModel.class);
                    String nowDate;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                    nowDate = currentDate.format(calendar.getTime());
                    String expDate = voucherModel.getExpiration_date();
                    try {
                        if (currentDate.parse(expDate).before(currentDate.parse(nowDate)))
                        {
                            String id = documentSnapshot.getId();
                            firebaseFirestore.collection("Voucher").document(id).delete();
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        List<VoucherModel> lstVoucher = new ArrayList<>();
        firebaseFirestore.collection("Voucher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                    VoucherModel voucherModel = documentSnapshot.toObject(VoucherModel.class);
                    int minimum = Integer.parseInt(getIntent().getStringExtra("minimum"));
                    if (Integer.parseInt(voucherModel.getMinimum() + "000") <= minimum)
                    {
                        lstVoucher.add(voucherModel);
                    }
                }
                voucherAdapter = new VoucherAdapter(lstVoucher);
                lstView.setAdapter(voucherAdapter);

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                VoucherModel voucherModel = (VoucherModel)  voucherAdapter.getItem(position);
//                System.out.println(voucherModel.getDiscount() + "----------------");
//                Intent intent = new Intent(VoucherActivity.this, PayActivity.class);
//                intent.putExtra("discount", voucherModel.getDiscount());
//                startActivity(intent);
//            }
//        });
//        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                VoucherModel voucherModel = (VoucherModel)  voucherAdapter.getItem(i);
//                System.out.println(voucherModel.getDiscount() + "----------------");
//                Intent intent = new Intent(VoucherActivity.this, PayActivity.class);
//                intent.putExtra("discount", voucherModel.getDiscount());
//                startActivity(intent);
//            }
//        });
    }
}