package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.shoppingapp.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }else startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}