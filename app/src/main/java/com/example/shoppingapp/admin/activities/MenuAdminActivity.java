package com.example.shoppingapp.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.admin.activities.statistic.StatisticActivity;
import com.example.shoppingapp.user.activities.LoginActivity;
import com.example.shoppingapp.admin.activities.addProduct.AddProduct;
import com.example.shoppingapp.admin.activities.manageAccount.ManageAccountActivity;
import com.example.shoppingapp.admin.activities.manageOrder.ManageOrderActivity;
import com.example.shoppingapp.admin.activities.updateProduct.searchProduct;

public class MenuAdminActivity extends AppCompatActivity {

    Button manageAccount, addProduct, updateProduct, manageOrder, statistic;

    TextView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        manageAccount = findViewById(R.id.manageAccount);
        addProduct = findViewById(R.id.addProduct);
        updateProduct = findViewById(R.id.updateProduct);
        manageOrder = findViewById(R.id.manageOrder);
        logout = findViewById(R.id.logout);
        statistic = findViewById(R.id.statistic);

        manageAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, ManageAccountActivity.class));
            }
        });

        manageOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, ManageOrderActivity.class));
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, AddProduct.class));
            }
        });

        updateProduct.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, searchProduct.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, LoginActivity.class));
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuAdminActivity.this, StatisticActivity.class));
            }
        });

    }
}