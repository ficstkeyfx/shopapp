package com.example.shoppingapp.user.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingapp.admin.activities.MenuAdminActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            if (auth.getCurrentUser().getEmail().equals("shopapp216@gmail.com"))
            {
                startActivity(new Intent(MainActivity.this, MenuAdminActivity.class));
            }
//
            else startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }else startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}