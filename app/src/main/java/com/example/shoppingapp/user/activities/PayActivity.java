package com.example.shoppingapp.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;

import com.example.shoppingapp.user.adapters.MyCartAdapters;
import com.example.shoppingapp.user.models.MyCartModel;
import com.example.shoppingapp.user.ui.cart.MyCartsFragment;
import com.example.shoppingapp.zalo.CreateOrder;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {

    ImageView goBack;
    RecyclerView product;
    TextView nameConfirm, numberConfirm, addressConfirm, priceConfirm;
    Button cashPay, zaloPay;

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

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PayActivity.this, MyCartsFragment.class));
            }
        });

        cashPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this, PlaceOrderActivity.class);
                intent.putExtra("itemList", getIntent().getSerializableExtra("itemList"));
                intent.putExtra("address", getIntent().getStringExtra("address"));
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