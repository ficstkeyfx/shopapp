package com.example.shoppingapp.admin.activities.statistic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shoppingapp.R;

public class StatisticActivity extends AppCompatActivity {

    Button statCountButton,StatSoldProduct, statRevenue, statAccount;
    ImageView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        goBack = findViewById(R.id.goBack);

        statCountButton = findViewById(R.id.statCount);
        statCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticActivity.this,StatCountProductActivity.class));
            }
        });

        StatSoldProduct = findViewById(R.id.statCountSold);
        StatSoldProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticActivity.this,StatSoldProductActivity.class));
            }
        });

        statRevenue = findViewById(R.id.revenueStat);
        statRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticActivity.this, StatRevenueActivity.class));
            }
        });

        statAccount = findViewById(R.id.statAccount);
        statAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticActivity.this, StatAccountActivity.class));
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}