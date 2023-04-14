package com.example.shoppingapp.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.activities.LoginActivity;
import com.example.shoppingapp.ui.admin.manageAccount.ManageAccountActivity;

public class MenuAdminActivity extends AppCompatActivity {

    Button manageAccount, addProduct, updateProduct, manageOrder;

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

        manageAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MenuAdminActivity.this, ManageAccountActivity.class));
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
    }
}