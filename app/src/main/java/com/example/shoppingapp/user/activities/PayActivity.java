package com.example.shoppingapp.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;

import com.example.shoppingapp.user.adapters.MyCartAdapters;
import com.example.shoppingapp.user.models.MyCartModel;
import com.example.shoppingapp.user.ui.cart.MyCartsFragment;
import com.example.shoppingapp.zalo.CreateOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {

    ImageView goBack;
    RecyclerView product;
    TextView nameConfirm, numberConfirm, addressConfirm, priceConfirm;
    Button cashPay, zaloPay;
    FirebaseFirestore firebaseFirestore;
    String current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        goBack = findViewById(R.id.goBack);

        product = findViewById(R.id.productConfirm);
        product.setLayoutManager(new LinearLayoutManager(PayActivity.this));
        product.setAdapter(new MyCartAdapters((ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList"), PayActivity.this));

        nameConfirm = findViewById(R.id.nameConfirm);
        numberConfirm = findViewById(R.id.numberConfirm);
        addressConfirm = findViewById(R.id.addressConfirm);
        priceConfirm = findViewById(R.id.priceConfirm);

        cashPay = findViewById(R.id.payBtn);
        zaloPay = findViewById(R.id.zaloPayBtn);

        // zaloPay
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        nameConfirm.setText(getIntent().getStringExtra("name"));
        numberConfirm.setText(getIntent().getStringExtra("number"));
        addressConfirm.setText("Địa chỉ:            " + getIntent().getStringExtra("address"));
        priceConfirm.setText("Tổng thanh toán:          "+ getIntent().getStringExtra("price") + "đ");

        firebaseFirestore = FirebaseFirestore.getInstance();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayActivity.this, MyCartsFragment.class));
            }
        });

        YearMonth thisMonth = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        current = thisMonth.format(monthYearFormatter);

        cashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this, PlaceOrderActivity.class);
                intent.putExtra("itemList", getIntent().getSerializableExtra("itemList"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
                Map<String, Object> stat = new HashMap<>();
                Map<String, Object> stat_type = new HashMap<>();
                Map<String, Object> revenue_month = new HashMap<>();
                Map<String, Object> revenue_type = new HashMap<>();
                firebaseFirestore.collection("Statistics")
                        .document("SoldProducts")
                        .collection("Count")
                        .document("Monthly")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         stat.put(current,task.getResult().getLong(current)+((ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")).size());
                         firebaseFirestore.collection("Statistics")
                                 .document("SoldProducts")
                                 .collection("Count")
                                 .document("Monthly")
                                 .update(stat);
                     }
                 });
                firebaseFirestore.collection("Statistics")
                        .document("SoldProducts")
                        .collection("Count")
                        .document("Type")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int adidas = 0;
                                int nike = 0;
                                int converse = 0;
                                int newbalance = 0;
                                int gucci = 0;
                                for(MyCartModel model:(ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                    if(model.getProductType().equals("adidas")) {
                                        adidas++;
                                    }
                                    if(model.getProductType().equals("nike")) {
                                        nike++;
                                    }
                                    if(model.getProductType().equals("converse")) {
                                        converse++;
                                    }
                                    if(model.getProductType().equals("newbalance")) {
                                        newbalance++;
                                    }
                                    if(model.getProductType().equals("gucci")) {
                                        gucci++;
                                    }
                                }
                                stat_type.put("adidas",task.getResult().getLong("adidas")+adidas);
                                stat_type.put("nike",task.getResult().getLong("nike")+nike);
                                stat_type.put("converse",task.getResult().getLong("converse")+converse);
                                stat_type.put("newbalance",task.getResult().getLong("newbalance")+newbalance);
                                stat_type.put("gucci",task.getResult().getLong("gucci")+gucci);
                                firebaseFirestore.collection("Statistics")
                                        .document("SoldProducts")
                                        .collection("Count")
                                        .document("Type")
                                        .update(stat_type);
                            }
                        });
                firebaseFirestore.collection("Statistics")
                        .document("SoldProducts")
                        .collection("Revenue")
                        .document("Monthly")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int revenue = 0;
                                for(MyCartModel model: (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                    revenue += model.getProductPrice();
                                }
                                revenue_month.put(current,task.getResult().getLong(current) + revenue);
                                firebaseFirestore.collection("Statistics")
                                        .document("SoldProducts")
                                        .collection("Revenue")
                                        .document("Monthly")
                                        .update(revenue_month);
                            }
                        });
                firebaseFirestore.collection("Statistics")
                        .document("SoldProducts")
                        .collection("Revenue")
                        .document("Type")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int adidas = 0;
                                int nike = 0;
                                int converse = 0;
                                int newbalance = 0;
                                int gucci = 0;
                                for(MyCartModel model:(ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                    if(model.getProductType().equals("adidas")) {
                                        adidas += model.getProductPrice();
                                    }
                                    if(model.getProductType().equals("nike")) {
                                        nike += model.getProductPrice();
                                    }
                                    if(model.getProductType().equals("converse")) {
                                        converse += model.getProductPrice();
                                    }
                                    if(model.getProductType().equals("newbalance")) {
                                        newbalance += model.getProductPrice();
                                    }
                                    if(model.getProductType().equals("gucci")) {
                                        gucci += model.getProductPrice();
                                    }
                                }
                                revenue_type.put("adidas",task.getResult().getLong("adidas")+adidas);
                                revenue_type.put("nike",task.getResult().getLong("nike")+nike);
                                revenue_type.put("converse",task.getResult().getLong("converse")+converse);
                                revenue_type.put("newbalance",task.getResult().getLong("newbalance")+newbalance);
                                revenue_type.put("gucci",task.getResult().getLong("gucci")+gucci);
                                firebaseFirestore.collection("Statistics")
                                        .document("SoldProducts")
                                        .collection("Revenue")
                                        .document("Type")
                                        .update(revenue_type);
                            }
                        });
                startActivity(intent);
            }
        });

        zaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestZalo();
            }
        });
    }

    private void requestZalo() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(getIntent().getStringExtra("price"));
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");

                ZaloPaySDK.getInstance().payOrder(PayActivity.this, token, "demozpdk://app", new PayOrderListener() {

                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        Intent intent = new Intent(PayActivity.this, PlaceOrderActivity.class);
                        intent.putExtra("itemList", getIntent().getSerializableExtra("itemList"));
                        intent.putExtra("address", getIntent().getStringExtra("address"));

                        Map<String, Object> stat = new HashMap<>();
                        Map<String, Object> stat_type = new HashMap<>();
                        Map<String, Object> revenue_month = new HashMap<>();
                        Map<String, Object> revenue_type = new HashMap<>();
                        firebaseFirestore.collection("Statistics")
                                .document("SoldProducts")
                                .collection("Count")
                                .document("Monthly")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        stat.put(current,task.getResult().getLong(current)+((ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")).size());
                                        firebaseFirestore.collection("Statistics")
                                                .document("SoldProducts")
                                                .collection("Count")
                                                .document("Monthly")
                                                .update(stat);
                                    }
                                });
                        firebaseFirestore.collection("Statistics")
                                .document("SoldProducts")
                                .collection("Count")
                                .document("Type")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        int adidas = 0;
                                        int nike = 0;
                                        int converse = 0;
                                        int newbalance = 0;
                                        int gucci = 0;
                                        for(MyCartModel model:(ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                            if(model.getProductType().equals("adidas")) {
                                                adidas++;
                                            }
                                            if(model.getProductType().equals("nike")) {
                                                nike++;
                                            }
                                            if(model.getProductType().equals("converse")) {
                                                converse++;
                                            }
                                            if(model.getProductType().equals("newbalance")) {
                                                newbalance++;
                                            }
                                            if(model.getProductType().equals("gucci")) {
                                                gucci++;
                                            }
                                        }
                                        stat_type.put("adidas",task.getResult().getLong("adidas")+adidas);
                                        stat_type.put("nike",task.getResult().getLong("nike")+nike);
                                        stat_type.put("converse",task.getResult().getLong("converse")+converse);
                                        stat_type.put("newbalance",task.getResult().getLong("newbalance")+newbalance);
                                        stat_type.put("gucci",task.getResult().getLong("gucci")+gucci);
                                        firebaseFirestore.collection("Statistics")
                                                .document("SoldProducts")
                                                .collection("Count")
                                                .document("Type")
                                                .update(stat_type);
                                    }
                                });
                        firebaseFirestore.collection("Statistics")
                                .document("SoldProducts")
                                .collection("Revenue")
                                .document("Monthly")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        int revenue = 0;
                                        for(MyCartModel model: (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                            revenue += model.getProductPrice();
                                        }
                                        revenue_month.put(current,task.getResult().getLong(current) + revenue);
                                        firebaseFirestore.collection("Statistics")
                                                .document("SoldProducts")
                                                .collection("Revenue")
                                                .document("Monthly")
                                                .update(revenue_month);
                                    }
                                });
                        firebaseFirestore.collection("Statistics")
                                .document("SoldProducts")
                                .collection("Revenue")
                                .document("Type")
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        int adidas = 0;
                                        int nike = 0;
                                        int converse = 0;
                                        int newbalance = 0;
                                        int gucci = 0;
                                        for(MyCartModel model:(ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList")){
                                            if(model.getProductType().equals("adidas")) {
                                                adidas += model.getProductPrice();
                                            }
                                            if(model.getProductType().equals("nike")) {
                                                nike += model.getProductPrice();
                                            }
                                            if(model.getProductType().equals("converse")) {
                                                converse += model.getProductPrice();
                                            }
                                            if(model.getProductType().equals("newbalance")) {
                                                newbalance += model.getProductPrice();
                                            }
                                            if(model.getProductType().equals("gucci")) {
                                                gucci += model.getProductPrice();
                                            }
                                        }
                                        revenue_type.put("adidas",task.getResult().getLong("adidas")+adidas);
                                        revenue_type.put("nike",task.getResult().getLong("nike")+nike);
                                        revenue_type.put("converse",task.getResult().getLong("converse")+converse);
                                        revenue_type.put("newbalance",task.getResult().getLong("newbalance")+newbalance);
                                        revenue_type.put("gucci",task.getResult().getLong("gucci")+gucci);
                                        firebaseFirestore.collection("Statistics")
                                                .document("SoldProducts")
                                                .collection("Revenue")
                                                .document("Type")
                                                .update(revenue_type);
                                    }
                                });
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}